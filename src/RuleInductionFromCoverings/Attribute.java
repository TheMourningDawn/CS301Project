package RuleInductionFromCoverings;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Attribute {
	public String instanceName;
	public String instanceValueType;
	public List<String> instanceValues;
	
	public Set<String> uniqueValues;
	
	public Attribute() {
	}
	
	public Attribute(String instName,String instValType) {
		this.instanceName = instName;
		this.instanceValueType = instValType;
		instanceValues = new ArrayList<String>();
	}
	
	public void getUniqueValues(){
		uniqueValues = new HashSet<String>(instanceValues);
	}

}
