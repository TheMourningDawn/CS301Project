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
			if (!unique_values.contains(value)) {
				unique_values.add(value);
			}
		}
		
		return unique_values;
	}
	
	public Set<List<String>> computePartition(List<String> nonDec,List<String> dec){
		Set<List<Integer>> combinedNonDec = new HashSet<List<Integer>>();
		for(int i=0;i<relation.attributeData.get(0).instanceValues.size();i++){
			
		}
		return null;
	}
	
	// for testing
	public static <T> boolean isPartition(Set<List<T>> set, Set<List<T>> subset) {
		return true;
	}
	
	public boolean computeCoverings(Set<List<String>> nonDecPartition,Set<List<String>> decPartition){
//		boolean isCovering = true;
		boolean isSubsetOfOne = false;
		//For each partition part({x1,x2}...{x7,x8,x9}) of our non decision
		for(List<String> partition:nonDecPartition) {
			//Look to see if it can be a subset of any partition part of the decision partition
			for(List<String> decPart:decPartition) {
				//If it is a subset of any of them, set check true and break to next non decision
				if(decPart.containsAll(partition)){
					isSubsetOfOne = true;
					break;
				}	
			}
			//If one of the non-dec is not a subset of any decision....then then its not a subset of the whole thing...
			if(!isSubsetOfOne){
//				isCovering = false;
				return false;
			}
		}
		//If we got here, its a subset, I hope
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
	
	public void induceRules(){
		
	}
	
	public void isCovering(){
		
	}
}
