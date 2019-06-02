package pwcg.gui.maingui.coop;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pwcg.campaign.CoopHostUserBuilder;
import pwcg.campaign.io.json.CoopUserIOJson;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;

public class CoopHostPassword extends ImageResizingPanel
{
	private static final long serialVersionUID = 1L;
	
    private CoopUser coopHostRecord = null;
	private JTextField hostPasswordTextBox;

	public CoopHostPassword()
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
        List<CoopUser> coopUsers = CoopUserIOJson.readCoopUsers();
        for (CoopUser coopUser : coopUsers)
        {
            if (coopUser.getUsername().equals("Host"))
            {
            	coopHostRecord = coopUser;
            }
        }
        
        if (coopHostRecord == null)
        {
        	CoopHostUserBuilder hostBuilder = new CoopHostUserBuilder();
        	coopHostRecord = hostBuilder.getHostUser();
        }

        hostPasswordTextBox.setText(coopHostRecord.getPassword());
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
		if ((password != null) && password.length() > 0)
		{
			coopHostRecord.setPassword(password);
			CoopUserIOJson.writeJson(coopHostRecord);
		}
	}
}
