package RuleInductionFromCoverings;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Attribute {
	public String instanceName; // name of this attribute column
	public String instanceValueType; // e.g. numeric
	public List<String> instanceValues; // list of all values in this column
	
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
