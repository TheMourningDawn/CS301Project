import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;


/**
 * Just going to make a quick GUI for selecting options.
 * Shouldn't be any harder than making it command line.
 */
public class UserInterface implements ActionListener {
	
	public static JFrame mainFrame;
	
	//Default Constructor
	public UserInterface(){
		
	}
	
	// Main method
	public static void main(String[] args) {
		createMainFrame();
	}
	
	public static void createMainFrame()
	{
		//Set up the main frame
		mainFrame = new JFrame("Rule Induction From Coverings");
		mainFrame.setSize(new Dimension(400,470));
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
		mainFrame.getContentPane().setBackground(Color.BLACK);
		mainFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
