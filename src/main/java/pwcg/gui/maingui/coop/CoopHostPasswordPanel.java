package pwcg.gui.maingui.coop;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pwcg.coop.CoopUserManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;

public class CoopHostPasswordPanel extends ImageResizingPanel
{
	private static final long serialVersionUID = 1L;
	
	private JTextField hostPasswordTextBox;

	public CoopHostPasswordPanel()
	{
	    super(ContextSpecificImages.imagesMisc() + "Paper.jpg");
	}
	
	public void makePanels() 
	{
		try
		{
	        JPanel centerPanel = makePasswordEntryPanel();
	        this.add(centerPanel, BorderLayout.NORTH);
	        loadPanels();
		}
		catch (Throwable e)
		{
			Logger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}
	
    private void loadPanels() throws PWCGException
    {
        hostPasswordTextBox.setText(CoopUserManager.getIntance().getCoopHost().getPassword());
    }

    private JPanel makePasswordEntryPanel() throws PWCGException 
    {
	    Font font = MonitorSupport.getPrimaryFontLarge();

	    JPanel centerPanel = new JPanel();
	    centerPanel.setLayout(new BorderLayout());
	    centerPanel.setOpaque(false);
	    
		JPanel descPanel = new JPanel (new GridLayout(0,1));
		descPanel.setOpaque(false);
		
		JPanel textPanel = new JPanel (new GridLayout(0,1));
		textPanel.setOpaque(false);

	    JLabel passwordLabel = new JLabel("Enter Coop Host Password: ");
	    passwordLabel.setFont(font);
	    passwordLabel.setBackground(ColorMap.PAPER_BACKGROUND);
	    descPanel.add(passwordLabel);

        hostPasswordTextBox = new JTextField(50);
        hostPasswordTextBox.setFont(font);
        hostPasswordTextBox.setBackground(ColorMap.PAPER_BACKGROUND);
        textPanel.add(hostPasswordTextBox);

        centerPanel.add(descPanel, BorderLayout.WEST);
        centerPanel.add(textPanel, BorderLayout.CENTER);

		return centerPanel;

	}

	public void writeResults() throws PWCGException 
	{
		String password = hostPasswordTextBox.getText();
		CoopUserManager.getIntance().setHostPassword(password);
	}
}
