package RuleInductionFromCoverings;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class ReadArffFile {
	File input;
	Relation storedRelation = new Relation(); //TODO:rename me...

	public ReadArffFile(File bestInput) {
		this.input = bestInput;
	}
	
	
	//TODO: Depending on what we decide needs to be done to this data, we might need to read it into different structures
	public Relation readFile(){
//		UserInterface.logData.setText(UserInterface.logData.getText() + "Starting to read file: " + input.getName());
		
		BufferedReader reader;
		String currentLine;
		try {
			reader = new BufferedReader(new FileReader(input));
			
			//Read the first line, that will be the relation name
			storedRelation.relationName = reader.readLine().split(" ")[1];
			//Burn one - blank line
			reader.readLine();
			
			while((currentLine = reader.readLine()).contains("@attribute")){
				//Put in our attributeTypes
				Attribute oneAttribute = new Attribute(currentLine.split(" ")[1],currentLine.split(" ")[2]);
				storedRelation.attributeData.add(oneAttribute);
			}
			if(reader.readLine().equals("@data")){ //I dont NEED to do this, but im going to, to make sure
				while((currentLine = reader.readLine()) != null){ //Should be able to read till the end of the file
					for(int i=0;i<storedRelation.attributeData.size();i++){
						//read in the actual data for each of the attributes {a,b,c..}
						storedRelation.attributeData.get(i).instanceValues.add(currentLine.split(",")[i]);
					}
				}
			} else {
				//TODO: Freak out, maybe print something later if this matters (No @data where expected)
			}
			
			//Now find the unique values since we have read in all the data
			//TODO: Not sure we need this at all. Keeping for now.
//			for(int i=0;i<lotsOData.attributeData.size();i++){
//				lotsOData.attributeData.get(i).getUniqueValues();
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return storedRelation;
//		Execute thisExecute = new Execute(lotsOData);
//		List<String> testAttrByName = new ArrayList<String>();
//		testAttrByName.add("a0");
//		testAttrByName.add("a1");
//		testAttrByName.add("a2");
//		testAttrByName.add("a3");
//		thisExecute.computePartition(testAttrByName);
	}
	
	//Method for printing out what I've read in.
	public void printRelation(){
		System.out.println("Relation name: " + storedRelation.relationName + "\n");
		String attrName = "   ";
		for(int j=0;j<storedRelation.attributeData.size();j++){
			attrName += " " + storedRelation.attributeData.get(j).instanceName;
		}
		System.out.println(attrName);
		for(int i=0;i<storedRelation.attributeData.get(0).instanceValues.size();i++){
			String oneLine = "x" + i + ":";
			for(int j=0;j<storedRelation.attributeData.size();j++){
				oneLine += " " + storedRelation.attributeData.get(j).instanceValues.get(i);
			}
			System.out.println(oneLine);
		}
	}
	

}
