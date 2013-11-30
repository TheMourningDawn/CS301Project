import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * Just going to make a quick GUI for selecting options.
 * Shouldn't be any harder than making it command line.
 */
public class UserInterface extends JFrame implements ActionListener {
	private static UserInterface myInterface;
	
	JFrame mainFrame;
	JButton openFile;
	
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
		mainFrame.setSize(new Dimension(400,470));
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
//		mainFrame.getContentPane().setBackground(Color.BLACK);
		mainFrame.setVisible(true);
	}
	
	public void fillMainFrameComponents()
	{
		//Create the open button and add a listener
		openFile = new JButton("Open file...");
		openFile.addActionListener(this);
		mainFrame.add(openFile);
		
		//Create the text area we will be sending our logs to
		logData = new JTextPane();
		logData.setText("Welcome! Please select a file!\n");
		
		//Create the scroll pane
		logScrollPane = new JScrollPane();
		logScrollPane.setPreferredSize(new Dimension(380,350));
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

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == openFile) {
	        int returnVal = openWekaFile.showOpenDialog(UserInterface.this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = openWekaFile.getSelectedFile();
	            //TODO: This is where a real application would open the file.
	            logData.setText(logData.getText() + "Opening: " + file.getName() + "\n" );
	            ////
	            ReadArffFile inputFileRead = new ReadArffFile(file);
	            inputFileRead.readFile();
	            inputFileRead.printRelation();
	        } else {
	        	logData.setText(logData.getText() + "Open command cancelled by user.\n");
	        }
	   }
		
	}
	
	
	
}
