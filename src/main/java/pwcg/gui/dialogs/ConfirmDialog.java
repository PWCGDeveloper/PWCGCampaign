package pwcg.gui.dialogs;

import java.awt.Component;

import javax.swing.JOptionPane;

public class ConfirmDialog 
{
	static public int areYouSure(String message)
	{
        int result = JOptionPane.showConfirmDialog((Component) null, message,
                "Confirm Action", JOptionPane.YES_NO_OPTION);
        
        return result;
	}
}
