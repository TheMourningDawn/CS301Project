import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Attribute {
	//These types are questionable to me right now.
	//Not so sure what we are going to be doing with them.
	//Probably nothing that requires math? So its likely safe.
	public String instanceName;
	public String instanceValueType;
	public List<String> instanceValues;
	
	public Set<String> uniqueValues;
	
	public Attribute() {
		// TODO Auto-generated constructor stub
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
