package pwcg.gui.dialogs;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ErrorDialog 
{
	static public void internalError(String message)
	{
		String fullMessage = "Please see the error log file: PWCGErrorLog.txt and post error on the RoF forum.  " + message;
		new  ErrorDialog(fullMessage);
	}
	
	static public void userError(String message)
	{
		new  ErrorDialog(message);
	}
	
	private ErrorDialog(String message)
	{
		JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
				JOptionPane.ERROR_MESSAGE);
	}	
}
