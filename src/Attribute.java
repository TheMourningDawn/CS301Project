
public class Attribute {
	//These types are questionable to me right now.
	//Not so sure what we are going to be doing with them.
	//Probably nothing that requires math? So its likely safe.
	public String instanceName;
	public String instanceValueType;
	public String instanceValue;
	
	//To store the number of possible values for a given attribute
	//TODO: instanceValueType will need to be parsed when a method of this class is called.
	/*
		It looks like we only have to deal with two formats for the data.
		1. numeric
		2. {A,B,C}
		
		This might be tricky. If the values are given like in two, its cool, if its numeric, we have to manually go
		through all instances and count unique values
	*/
	public int numPossibleValForType;
	public Attribute() {
		// TODO Auto-generated constructor stub
	}
	public Attribute(String instName,String instValType) {
		this.instanceName = instName;
		this.instanceValueType = instValType;
	}

}
