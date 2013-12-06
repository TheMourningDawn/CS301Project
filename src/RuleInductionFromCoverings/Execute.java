package RuleInductionFromCoverings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Execute {
	Relation relation = new Relation();
	HashMap<String,Integer> attributeIndexMap = new HashMap<String,Integer>();
	Set<List<String>> allCoverings = new HashSet<List<String>>();
	Set<List<Integer>> decisionPartition;
//	Set<String> nonDecisionAttributeSet;
	
	//I think we need some way to tell if a partition is minimal for its values...
	public Execute(Relation relate){
		relation = relate;
		for(int i=0;i<relate.attributeData.size();i++){
			attributeIndexMap.put(relate.attributeData.get(i).instanceName, i);
		}
	}
	  
	//Trying some stuff here
	//This method might turn into caculateAllCoverings...or something to that effect
	public Set<List<String>> runOne(List<String> decisionAttributes){
		//Get the partition for the decision attributes
		decisionPartition = computePartition(decisionAttributes);
		
		//Get the set of non decision attributes to run partitions on
		Set<String> nonDecisionAttributeSet = new HashSet<String>();
		for(Attribute oneAttr:relation.attributeData) {
			if(!decisionAttributes.contains(oneAttr.instanceName)) {
//				System.out.println("This one was non dec - put in set: " + oneAttr.instanceName);
				nonDecisionAttributeSet.add(oneAttr.instanceName);
			}	
		}
		
		for(Set one : powerSet(nonDecisionAttributeSet)){
			List<String> temp = new ArrayList<String>();
	    	temp.addAll(one);
	    	checkIsCovering(temp);
		}
		//Now iterate through all these combinations and calculate possible partitions --> coverings
		System.out.println("Checking on size: " + nonDecisionAttributeSet.toString());
//		processSubsets(nonDecisionAttributeSet, 2);
		return allCoverings;
	}
	
	public void checkIsCovering(List<String> oneAttrCombo){
//		System.out.println("Checking on size: " + oneAttrCombo.size() + " oneAttrCombo: " + oneAttrCombo.toString());
		//Get partition for this combination of non decision attributes
		Set<List<Integer>> singlePartition = computePartition(oneAttrCombo);
		System.out.println("Partition for:");
		//Check to see if it's a possible covering
		if(isCoveringCanidate(singlePartition, decisionPartition)) {
			System.out.println("Found one that could be a covering!");
			//TODO: Actually check if its minimal
			//For now, just put all covering canidates into our structure
			boolean foundLarger = false;
			boolean isLarger = false;
			List<String> temp = new ArrayList<String>();
			for(List<String> one:allCoverings) {
				if(one.containsAll(oneAttrCombo)) {
					foundLarger = true;
					temp = one;
				} 
				if(oneAttrCombo.containsAll(one)) {
					isLarger = true;
				}
			}
			if(foundLarger){
//				System.out.println("I've found a larger");
				allCoverings.remove(temp);
				allCoverings.add(oneAttrCombo);
			} else if(isLarger) {
				//Do nothing
			}
			else {
				allCoverings.add(oneAttrCombo);
			}
		}
	}
	
	public void printAllCovering(){
		System.out.println("Here are teh coverings");
		for(List<String> line:allCoverings){
			System.out.println(line.toString());
		}
	}
	
	
	public void processSubsets(List<String> set, int maxSize) {
		System.out.println("in processSubsets: " + set.size());	
		List<String> subset = new ArrayList<String>();
		processLargerSubsets(set, subset, maxSize, 0, 0);
	}

	public void processLargerSubsets(List<String> set, List<String> subset, int maxSize, int subsetSize, int nextIndex) {
		if (subsetSize == maxSize && subset.size() != 0) {
			//The method we want to call on all subsets (check for covering + whatnot)
			checkIsCovering(subset);
		} else {
			List<String> subsetList = new ArrayList<String>();
			for (int j = nextIndex; j < set.size(); j++) {
				subsetList.add(set.get(j));
				processLargerSubsets(set, subsetList, maxSize, subsetSize + 1, j + 1);
			}
		}
	}
	
	// Gets all possible values for a given attribute, for output and for ez partitioning
	public Set<String> getPossibleValuesFor(List<String> attribute_values) {
		Set<String> unique_values = new HashSet<String>();
		
		for (String value : attribute_values) {
			//TODO: Sets should only add a value if it's not already in there, so we probably don't have to do this check
			if (!unique_values.contains(value)) {
				unique_values.add(value);
			}
		}
		
		return unique_values;
	}
	
	public Set<List<Integer>> computePartition(List<String> attributesByName){
		Set<List<Integer>> partition = new HashSet<List<Integer>>();
		//Make a new list of integers to store some hashed strings (could maybe use array instead)
		List<Integer> hashedAttributes = new ArrayList<Integer>();
		//Concat (string) values of each nonDec attribute and hash it and put it in a list
		for(int i=0;i<relation.attributeData.get(0).instanceValues.size();i++){
			String comboToHash = "";
			for(int j=0;j<attributesByName.size();j++){
				//Holy cow
				comboToHash+= relation.attributeData.get(attributeIndexMap.get(attributesByName.get(j))).instanceValues.get(i);
			}
			hashedAttributes.add(comboToHash.hashCode());
		}
		
		//Need to look through each value in hashedAttributes and get the indexesss
//		System.out.println("hashedAttributes size: " + hashedAttributes.size() + " and values: " + hashedAttributes.toString());
		//Waste some more space to get the unique values in hashed attributes to find and destroy
		Object[] uniqueHash = new HashSet<Integer>(hashedAttributes).toArray();
		for(int i=0;i<uniqueHash.length;i++){ //For each unique value
//			System.out.println("We are on value: " + uniqueHash[i] + " iteration: " + i);
			List<Integer> singlePartition = new ArrayList<Integer>();
			for(int j=0;j<hashedAttributes.size();j++){//Find all occurrences in hashedAttributes
//				System.out.println("hashed of " + j + " = " + hashedAttributes.get(j));
				//If they match, do some stuff
				//NOTE: Not sure why I have to get the hash code to compare, I think I was checking
				//object references before, and uniqueHash contains one exact copy 
				if(hashedAttributes.get(j).hashCode()==uniqueHash[i].hashCode()){
//					System.out.println("It contains, and is at index of: " + j);
					singlePartition.add(j);	//Add the index number to one *list* or piece of a partition ({x1,x2})
				}	
			}
			partition.add(singlePartition); //Add each list ({x1,x2}) to the partition ( [{x3,x4},...,{x1,x2}] )
		}
		return partition;
	}
	
	/*
	 * Here we are checking:
	 *  1) If P is a subset of S (if our nonDecPartition is a subset of non decision attributes)
	 *  **Technically** we aren't actually check that...we assume our input will only be those...so g2g
	 * 	2) If P* <= R* (That all elements of nonDecPartition is a subset of at least one element of decPartition
	 * 
	 * We are NOT checking
	 * 	3) P is minimal
	 *		This will be done where we start computing covering candidates.
	 */
	public boolean isCoveringCanidate(Set<List<Integer>> nonDecPartition,Set<List<Integer>> decPartition){
		boolean isSubsetOfOne = false;
		//For each partition part({x1,x2}...{x7,x8,x9}) of our non decision
		for(List<Integer> partition:nonDecPartition) {
			//Look to see if it can be a subset of any partition part of the decision partition
			for(List<Integer> decPart:decPartition) {
				//If it is a subset of any of them, set check true and break to next non decision
				if(decPart.containsAll(partition)){
					isSubsetOfOne = true;
					break;
				}	
			}
			//If one of the non-dec is not a subset of any decision....then then its not a subset of the whole thing...
			if(!isSubsetOfOne){
				return false;
			}
		}
		//If we got here, its a subset, I hope
		return true;
	}
	
	/*
	public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
		Set<Set<T>> sets = new HashSet<Set<T>>();
		if (originalSet.isEmpty()) {
			sets.add(new HashSet<T>());
			return sets;
		}
		List<T> list = new ArrayList<T>(originalSet);
		T head = list.get(0);
		Set<T> rest = new HashSet<T>(list.subList(1, list.size())); 
		for (Set<T> set : powerSet(rest)) {
			Set<T> newSet = new HashSet<T>();
			newSet.add(head);
			newSet.addAll(set);
			sets.add(newSet);
			sets.add(set);
		}		
		return sets;
	}
	*/
	
	public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
		Set<Set<T>> ps = new HashSet<Set<T>>();
		ps.add(new HashSet<T>());
		for (T item : originalSet) {
			Set<Set<T>> newPs = new HashSet<Set<T>>();
		 
			for (Set<T> subset : ps) {
				newPs.add(subset);
		 
				Set<T> newSubset = new HashSet<T>(subset);
				newSubset.add(item);
				newPs.add(newSubset);
			}

			ps = newPs;
		  }
		  return ps;
	}
	
	public void runRICO(Set<List<String>> covering){
		
	}
}
