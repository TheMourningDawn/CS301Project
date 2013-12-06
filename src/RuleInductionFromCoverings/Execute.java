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
	
	public Execute(Relation relate){
		relation = relate;
		for(int i=0;i<relate.attributeData.size();i++){
			attributeIndexMap.put(relate.attributeData.get(i).instanceName, i);
		}
	}
	  
	public Set<List<String>> runOne(List<String> decisionAttributes, int maxNumAttrToConsider){
		//Print to the GUI the needed info from this section
		UserInterface.logData.setText(UserInterface.logData.getText() + "Decision Attributes: " + decisionAttributes + "\n");
		//Print out our possible values for all decision attributes to the GUI
		
		for(int i=0;i<decisionAttributes.size();i++){
			UserInterface.logData.setText(UserInterface.logData.getText() + "Distribution of values for attribute " + decisionAttributes.get(i) + "\n");
			HashMap<String,Integer> possibleValues = getPossibleValuesFor(relation.attributeData.get(attributeIndexMap.get(decisionAttributes.get(i))).instanceValues);
			for(String key:possibleValues.keySet()){
				UserInterface.logData.setText(UserInterface.logData.getText() + "Value: " + key + " Occurrences: " + possibleValues.get(key) + "\n");
			}
			UserInterface.logData.setText(UserInterface.logData.getText() + "\n");
		}
		
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
		
		//Now iterate through all these combinations and calculate possible partitions --> coverings
		for(Set<String> one : powerSet(nonDecisionAttributeSet)){
			List<String> temp = new ArrayList<String>();
			temp.addAll(one);
			//Only check if it is not empty and does not exceed the max to consider
			if(temp.size() != 0 && temp.size() <= maxNumAttrToConsider){checkIsCovering(temp);}
		}
		return allCoverings;
	}
	
	public void checkIsCovering(List<String> oneAttrCombo){
		//Get partition for this combination of non decision attributes
		Set<List<Integer>> singlePartition = computePartition(oneAttrCombo);
		//Check to see if it's a possible covering
		if(isCoveringCanidate(singlePartition, decisionPartition)) {
			System.out.println("Found one that could be a covering!");
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
	
	// for debugging, prints out all coverings detected
	public void printAllCovering(){
		for(List<String> line:allCoverings){
//			System.out.println(line.toString());
		}
	}
	
	// Gets all possible values for a given attribute, for output and for ez partitioning
	public HashMap<String, Integer> getPossibleValuesFor(List<String> attribute_values) {
		HashMap<String, Integer> frequency = new HashMap<String, Integer>();
		
		for (String value : attribute_values) {
			if (!frequency.containsKey(value)) {
				frequency.put(value, 1);
			} else {
				frequency.put(value, frequency.get(value) + 1);
			}
		}
		
		return frequency;
	}
	
	// Compute the partitions for a list of attributes
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
		Object[] uniqueHash = new HashSet<Integer>(hashedAttributes).toArray();
		for(int i=0;i<uniqueHash.length;i++){ //For each unique value
			List<Integer> singlePartition = new ArrayList<Integer>();
			for(int j=0;j<hashedAttributes.size();j++){//Find all occurrences in hashedAttributes
				//If they match, do some stuff
				if(hashedAttributes.get(j).hashCode()==uniqueHash[i].hashCode()){
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
	 * 	2) If P* <= R* (That all elements of nonDecPartition is a subset of at least one element of decPartition
	 * 	3) P is minimal
	 *		This will be done where we start computing covering candidates.
	 */
	public boolean isCoveringCanidate(Set<List<Integer>> nonDecPartition,Set<List<Integer>> decPartition){
		System.out.println(nonDecPartition + " what: " + decPartition);
		
		//For each partition part({x1,x2}...{x7,x8,x9}) of our non decision
		for(List<Integer> partition:nonDecPartition) {
			boolean isSubsetOfOne = false;
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
	
	//Run RICO to induce our rules!
	public void runRICO(Set<List<String>> covering,List<String> decision, int minCoverage, boolean dropConditions){
		//A hash map to store our results...this seems so unnecessary
		List<HashMap<List<HashMap<String,String>>,Integer>> results = new ArrayList<HashMap<List<HashMap<String,String>>,Integer>>();
		
		for(List<String> oneCovering:covering){
			//Initialize E to the set of all instances (For us, our original data)
			List<Attribute> E = relation.attributeData;
			//Make something to hold a single Hash of attributes and counts
			HashMap<List<HashMap<String,String>>,Integer> resultForSingleCovering = new HashMap<List<HashMap<String,String>>,Integer>();
			for(int i=0;i<E.get(0).instanceValues.size();i++){
				List<HashMap<String,String>> rules = new ArrayList<HashMap<String,String>>();
				for(int j=0;j<oneCovering.size();j++){
					HashMap<String,String> rulePair = new HashMap<String,String>();
					rulePair.put(oneCovering.get(j), E.get(attributeIndexMap.get(oneCovering.get(j))).instanceValues.get(i));
					rules.add(rulePair);
				}
				//Put in the decision as the last one
				for(int j=0;j<decision.size();j++){
					HashMap<String,String> rulePair = new HashMap<String,String>();
					rulePair.put(decision.get(j), E.get(attributeIndexMap.get(decision.get(j))).instanceValues.get(i));
					rules.add(rulePair);
				}
				if(!resultForSingleCovering.containsKey(rules)){
					resultForSingleCovering.put(rules,1);
				} else {
					int incCount = (resultForSingleCovering.get(rules)+1);
					resultForSingleCovering.put(rules, incCount);
				}
			}
			//Print more data to the GUI for the user
			UserInterface.logData.setText(UserInterface.logData.getText() + "Rules for covering " + oneCovering.toString() + "\n");
			for(List<HashMap<String, String>> oneResult:resultForSingleCovering.keySet()){//Something happens around here if the min coverage is higher than the highest covering
				if(dropConditions){

					//Here we want to prune!
					//This is gross. It can't be good.
					List<String> goodGuys = new ArrayList<String>();
					for(HashMap<String, String> mapOneResult:oneResult){
						for(String key:mapOneResult.keySet()){ //Should only be 1 in each of these
							if( !decision.contains(key) && checkNumOccurences(key,mapOneResult.get(key)) == resultForSingleCovering.get(oneResult)){
								goodGuys.add(key);
							}
						}
					}
					List<HashMap<String,String>> tempForPrint = new ArrayList<HashMap<String,String>>();
					for(HashMap<String,String> tired:oneResult){
						System.out.println("Trying: " + tired);
						tempForPrint.add((HashMap<String,String>)tired.clone());
					}
					boolean foundOne = false;
					for(HashMap<String,String> mapOneResult:oneResult){
						for(String key:mapOneResult.keySet()){
							if(!foundOne && !goodGuys.contains(key) && !decision.contains(key)){
								System.out.println("Jack just got replaced!");
								tempForPrint.get(oneResult.indexOf(mapOneResult)).put(key, "_");
								foundOne = true;
							}
						}	
					}

					//Only return results that are >= our minCoverage
					System.out.println("Was failing: " + resultForSingleCovering.get(oneResult) + " " + minCoverage);
					if(resultForSingleCovering.get(oneResult) < minCoverage){
						resultForSingleCovering.remove(oneResult);
					} else {
						UserInterface.logData.setText(UserInterface.logData.getText() + "[" + tempForPrint.toString() + ", " + resultForSingleCovering.get(oneResult) + "]\n");
					}
				} else {
					//Only return results that are >= our minCoverage
					System.out.println("Was failing: " + resultForSingleCovering.get(oneResult) + " " + minCoverage);
					if(resultForSingleCovering.get(oneResult) < minCoverage){
						resultForSingleCovering.remove(oneResult);
					} else {
						UserInterface.logData.setText(UserInterface.logData.getText() + "[" + oneResult.toString() + ", " + resultForSingleCovering.get(oneResult) + "]\n");
					}
				}
			}
			
			UserInterface.logData.setText(UserInterface.logData.getText() + "\n\n");
			results.add(resultForSingleCovering);
			allCoverings = new HashSet<List<String>>();
		}
	}
	
	// Gets the number of times value occurs within the attribute column attribute
	public int checkNumOccurences(String attribute, String value){
		int sum = 0;
		// Iterate over each value in the column and sum up the number of times each time it occurs
		for(int i=0;i<relation.attributeData.get(attributeIndexMap.get(attribute)).instanceValues.size();i++){
			if(relation.attributeData.get(attributeIndexMap.get(attribute)).instanceValues.get(i).equals(value)){
				sum++;
			}
		}
		return sum;
	}
}
