package RuleInductionFromCoverings;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * Just going to make a quick GUI for selecting options.
 * Shouldn't be any harder than making it command line.
 */
public class UserInterface extends JFrame implements ActionListener {
	private static UserInterface myInterface;
	
	//Some stuff required to read file and run algorithm!
	ReadArffFile inputFileRead;
	Execute runAlgorithm;

	JFrame mainFrame;
	JButton openFile;
	JButton runAlgorithmBtn;

	JLabel maxNumAttrLbl = new JLabel("Maximum number of attributes to consider:");
	JLabel minCoverageLbl = new JLabel("Minimum coverage for reporting:");
	JLabel decisionAttrLbl = new JLabel("Choose attribute(s) to be decision:");

	JComboBox<String> decisionAttrCbo;
	JComboBox<String> maxNumAttr;
	JComboBox<String> minCoverage;

	JCheckBox dropUnnecessary;

	JScrollPane logScrollPane;
	public static JTextPane logData; //Other classes are going to need to have access to this

	JFileChooser openWekaFile;


	//Default Constructor
	public UserInterface(){
		createMainFrame();
		fillMainFrameComponents();
	}

	// Main method
	public static void main(String[] args) {
		//Since this is the main, in order to skate around making everything static we just create an instance of this class with default constructor
		myInterface = new UserInterface();
	}

	public void createMainFrame()
	{
		//Set up the main frame
		mainFrame = new JFrame("Rule Induction From Coverings");
		mainFrame.setSize(new Dimension(500,600));
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
		mainFrame.setResizable(false);
		//		mainFrame.getContentPane().setBackground(Color.BLACK);
		mainFrame.setVisible(true);
	}

	public void fillMainFrameComponents()
	{
		//Create the open button and add a listener
		openFile = new JButton("Open file...");
		openFile.addActionListener(this);
		mainFrame.add(openFile);

		//Create a check box to dictate if user wants to drop unnecessary conditions
		dropUnnecessary = new JCheckBox("Drop Unnecessary Conditions");
		dropUnnecessary.setEnabled(false);//Make it unusable until the user selects the file so it gets filled
		dropUnnecessary.addActionListener(this);
		mainFrame.add(dropUnnecessary);
		
		//Create the label for the decision attributeness
		mainFrame.add(decisionAttrLbl);
		
		//Create the decision attribute choose 
		//TODO: Change this so we can use more than one decision attr + sort it somehow
		decisionAttrCbo = new JComboBox<String>();
		decisionAttrCbo.setPreferredSize(new Dimension(100,25));
		decisionAttrCbo.setEnabled(false);//Make it unusable until the user selects the file so it gets filled
		decisionAttrCbo.addActionListener(this);
		mainFrame.add(decisionAttrCbo);

		//Create the combo boxes for min coverage for rule to be reported and max num attributes to consider
		maxNumAttr = new JComboBox<String>();
		maxNumAttr.setPreferredSize(new Dimension(100,25));
		maxNumAttr.setEnabled(false);//Make it unusable until the user selects the file so it gets filled
		maxNumAttr.addActionListener(this);
		mainFrame.add(maxNumAttrLbl);
		mainFrame.add(maxNumAttr);

		minCoverage = new JComboBox<String>();
		String[] minCoverageOptions = new String[10];
		for(int i=0;i<minCoverageOptions.length;i++){
			minCoverageOptions[i]=Integer.toString(i+1);
		}
		DefaultComboBoxModel model2 = new DefaultComboBoxModel(minCoverageOptions);
		minCoverage.setModel(model2);
		minCoverage.setPreferredSize(new Dimension(100,25));
		minCoverage.setEnabled(false);//Make it unusable until the user selects the file so it gets filled
		minCoverage.addActionListener(this);
		mainFrame.add(minCoverageLbl);
		mainFrame.add(minCoverage);

		//Add the run button
		runAlgorithmBtn = new JButton("Run!");
		runAlgorithmBtn.setPreferredSize(new Dimension(200,25));
		runAlgorithmBtn.setEnabled(false);
		runAlgorithmBtn.addActionListener(this);
		mainFrame.add(runAlgorithmBtn);


		//Create the text area we will be sending our logs to
		logData = new JTextPane();
		logData.setText("Welcome! Please select a file!\n");

		//Create the scroll pane
		logScrollPane = new JScrollPane();
		logScrollPane.setPreferredSize(new Dimension(480,460));
		logScrollPane.setViewportView(logData);
		mainFrame.add(logScrollPane);

		//Create a file filter to only look for arff files
		FileNameExtensionFilter wekaFilter = new FileNameExtensionFilter("Weka Files", "arff");

		//Add the file chooser
		openWekaFile = new JFileChooser();
		openWekaFile.addChoosableFileFilter(wekaFilter);
		openWekaFile.setDialogTitle("Title");
		openWekaFile.setFileFilter(wekaFilter);
		
		mainFrame.validate();

	}

	public void setEnabled(boolean enabled){
		minCoverage.setEnabled(enabled);
		maxNumAttr.setEnabled(enabled);
		dropUnnecessary.setEnabled(enabled);
		runAlgorithmBtn.setEnabled(enabled);
		decisionAttrCbo.setEnabled(enabled);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == openFile) {
			int returnVal = openWekaFile.showOpenDialog(UserInterface.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = openWekaFile.getSelectedFile();
				logData.setText(logData.getText() + "Opening: " + file.getName() + "\n" );
				//Create a file read object
				inputFileRead = new ReadArffFile(file);
				//Create an execute object based on the Relation we will get out of the file read
				runAlgorithm = new Execute(inputFileRead.readFile());
				
				//Fill combo box with all attributes to select the decision attr
				DefaultComboBoxModel decisionAttrModel = new DefaultComboBoxModel(runAlgorithm.attributeIndexMap.keySet().toArray());
				decisionAttrCbo.setModel(decisionAttrModel);
				
				//Fill the max number of attr to use (Up to the max available (minus) decision attributes)
				String[] maxNumTemp = new String[runAlgorithm.attributeIndexMap.size()];
				for(int i=0;i<runAlgorithm.attributeIndexMap.size();i++){
					maxNumTemp[i] = Integer.toString(i+1);
					System.out.println(i+1);
				}
				DefaultComboBoxModel maxNumAttrModel = new DefaultComboBoxModel(maxNumTemp);
				maxNumAttr.setModel(maxNumAttrModel);
				
				//Set the controls to enabled so we can start our algorithm!
				//TODO: Have to actually fill these controls so they can do something
				setEnabled(true);
				inputFileRead.printRelation();
			} else {
				logData.setText(logData.getText() + "Open command cancelled by user.\n");
			}
		}
		if (e.getSource() == runAlgorithmBtn){
			System.out.println("Just pushed run!");
			//Going to try running with some dec attr
			List<String> decAttr = new ArrayList<String>();
			decAttr.add(decisionAttrCbo.getSelectedItem().toString());
			runAlgorithm.runOne(decAttr);
			runAlgorithm.printAllCovering();
			runAlgorithm.runRICO(runAlgorithm.allCoverings,Integer.parseInt(minCoverage.getSelectedItem().toString()));
		}

	}
}
