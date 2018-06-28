package pwcg.gui.dialogs;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class HelpDialog 
{
	public HelpDialog(String message)
	{
		JOptionPane.showMessageDialog(new JFrame(), message, "Help",
				JOptionPane.INFORMATION_MESSAGE);
	}
}
