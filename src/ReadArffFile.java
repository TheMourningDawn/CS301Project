import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class ReadArffFile {
	File input;
	Relation lotsOData = new Relation(); //TODO:rename me...

	public ReadArffFile(File bestInput) {
		this.input = bestInput;
	}
	
	
	
	public void readFile(){
		UserInterface.logData.setText(UserInterface.logData.getText() + "Starting to read file: " + input.getName());
		
		BufferedReader reader;
		String currentLine;
		try {
			reader = new BufferedReader(new FileReader(input));
			
			//Read the first line, that will be the relation name
			lotsOData.relationName = reader.readLine().split(" ")[1];
			//Burn one - blank line
			reader.readLine();
			
			while((currentLine = reader.readLine()).contains("@attribute")){
				//Put in our attributeTypes
				Attribute oneAttribute = new Attribute(currentLine.split(" ")[1],currentLine.split(" ")[2]);
				lotsOData.attributeTypes.add(oneAttribute);
			}
			if(reader.readLine().equals("@data")){ //I dont NEED to do this, but im going to, to make sure
				while((currentLine = reader.readLine()) != null){ //Should be able to read till the end of the file
					//Make a new list of attributes called instance (eg x1,x2,...)
					List<Attribute> instance = new ArrayList<Attribute>();
					for(int i=0;i<lotsOData.attributeTypes.size();i++){
						Attribute oneAttribute = lotsOData.attributeTypes.get(i);
						oneAttribute.instanceValue = currentLine.split(",")[i];
						//TODO: Probably need to calculate the number of possible values for this instance type here
						instance.add(oneAttribute);
					}
					lotsOData.instances.add(instance);
				}
			} else {
				//TODO: Freak out, maybe print something later if this matters (No @data where expected)
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printRelation(){
		System.out.println("Relation name: " + lotsOData.relationName + "\n");
		for(List<Attribute> instance:lotsOData.instances){
			System.out.println("Instance: \n");
			for(Attribute attr:instance){
				System.out.println("    ColName: " + attr.instanceName);
				System.out.println("    ColType: " + attr.instanceValueType);
				System.out.println("    InstanceValue: " + attr.instanceValue);
			}
		}
	}
	

}
