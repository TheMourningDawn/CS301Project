package RuleInductionFromCoverings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Execute {
	Relation relation = new Relation();
	HashMap<String,Integer> attributeIndexMap = new HashMap<String,Integer>();
	
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
		Set<List<String>> allCoverings = new HashSet<List<String>>();
		//Get the partition for the decision attributes
		Set<List<Integer>> decisionPartition;
		decisionPartition = computePartition(decisionAttributes);
		
		//Get the set of non decision attributes to run partitions on
		Set<String> nonDecisionAttributeSet = new HashSet<String>();
		for(Attribute oneAttr:relation.attributeData){
			System.out.println("Looking throughAttr");
			if(!decisionAttributes.contains(oneAttr.instanceName)){
				System.out.println("This one was non dec - put in set");
				nonDecisionAttributeSet.add(oneAttr.instanceName);
			}	
		}
		//Compute the *power set* of the nonDecision attributes to get all combination
		//TODO: Need to make the current power set function to return smaller subsets (only 3 or 4 values max rather than the whole power set)
		Set<Set<String>> attributeCombinations = powerSet(nonDecisionAttributeSet);
		System.out.println("Just computed power set");
		//Now iterate through all these combinations and calculate possible partitions --> coverings
		for(Set<String> oneAttrCombo:attributeCombinations){
			System.out.println("Checking on oneAttrCombo: " + oneAttrCombo.toString());
			//Get partition for this combination of non decision attributes
			List<String> nonDecAsList = new ArrayList<String>(oneAttrCombo);
			Set<List<Integer>> singlePartition = computePartition(nonDecAsList);
			//Check to see if it's a possible covering
			if(isCoveringCanidate(singlePartition, decisionPartition)){
				System.out.println("Found one that could be a covering!");
				//TODO: Actually check if its minimal
				//For now, just put all covering canidates into our structure
				allCoverings.add(nonDecAsList);
			}
		}
		return allCoverings;
	}
	
	public void doSomethingForNow(){
		//TODO: have to make this only get the non-decision attributes!
		//Make a set of the attributes
		Set<String> attributeSet = new HashSet<String>();
		for(Attribute oneAttr:relation.attributeData){
			attributeSet.add(oneAttr.instanceName);
		}
		
		
		//Make the combinations of all values (power set for now, later could/should be smaller)
		Set<Set<String>> allCombo = powerSet(attributeSet);
		for(Set<String> s : allCombo){
			System.out.println(s);
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
		for(List<Integer> one:partition){
//			System.out.println(new String(one.toString()));
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
		//TODO: We aren't checking for minimalism here, so that must be done at some point
		return true;
	}
	
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
	
	public void runRICO(Set<List<String>> covering){
		
	}
}
