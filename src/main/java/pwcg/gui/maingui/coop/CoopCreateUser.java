package pwcg.gui.maingui.coop;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pwcg.campaign.io.json.CoopUserIOJson;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;

public class CoopCreateUser extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

	private Map<String, CoopUser> existingCoopUsers = new HashMap<>();
	private Map<String, CoopUser> addedCoopUsers = new HashMap<>();
	private JTextField coopUserNameText;

	public CoopCreateUser() 
	{
	    super(ContextSpecificImages.imagesMisc() + "Paper.jpg");
	}

    public void makePanels()
    {
        try
        {
            loadExistingCoopUsers();
	        JPanel centerPanel = makeCoopUserEntryPanel();
	        this.add(centerPanel, BorderLayout.NORTH);
        }
        catch (Exception e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void loadExistingCoopUsers() throws PWCGException {
    	List<CoopUser> existingCoopUserList = CoopUserIOJson.readCoopUsers();
    	for (CoopUser existingCoopUser : existingCoopUserList)
    	{
    		existingCoopUsers.put(existingCoopUser.getUsername(), existingCoopUser);
    	}
	}

    private JPanel makeCoopUserEntryPanel() throws PWCGException 
    {
	    Font font = MonitorSupport.getPrimaryFontLarge();

	    JPanel centerPanel = new JPanel();
	    centerPanel.setLayout(new BorderLayout());
	    centerPanel.setOpaque(false);
	    
		JPanel descPanel = buildCoopUserLabel(font);
		JPanel textPanel = buildCoopUserTextField(font);
		JPanel buttonPanel = buildCoopUserButtons(font);

        centerPanel.add(descPanel, BorderLayout.WEST);
        centerPanel.add(textPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.EAST);

		return centerPanel;

	}

	private JPanel buildCoopUserTextField(Font font) 
	{
		JPanel textPanel = new JPanel (new GridLayout(0,1));
		textPanel.setOpaque(false);

        coopUserNameText = new JTextField(50);
        coopUserNameText.setFont(font);
        coopUserNameText.setBackground(ColorMap.PAPER_BACKGROUND);
        textPanel.add(coopUserNameText);
		return textPanel;
	}

	private JPanel buildCoopUserLabel(Font font) 
	{
		JPanel descPanel = new JPanel (new GridLayout(0,1));
		descPanel.setOpaque(false);

	    JLabel passwordLabel = new JLabel("Enter Coop Username: ");
	    passwordLabel.setFont(font);
	    passwordLabel.setBackground(ColorMap.PAPER_BACKGROUND);
	    descPanel.add(passwordLabel);
		return descPanel;
	}
	

	private JPanel buildCoopUserButtons(Font font) 
	{
		JPanel controlPanel = new JPanel (new GridLayout(0,1));
		controlPanel.setOpaque(false);

	    JButton createUserButton = new JButton("Create Coop User");
	    createUserButton.setActionCommand("Create Coop User");
	    createUserButton.addActionListener(this);
	    createUserButton.setFont(font);
	    createUserButton.setBackground(ColorMap.PAPER_BACKGROUND);
	    controlPanel.add(createUserButton);
		return controlPanel;
	}

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
        	String username = coopUserNameText.getText();
            if (action.equalsIgnoreCase("Create Coop User"))
            {
            	if (username == null || username.length() == 0)
            	{
            		ErrorDialog.userError("Username must be entered");
            	}
            	else if (isDuplicateUser(username))
            	{
            		ErrorDialog.userError("Username already in use");
            	}
            	else 
            	{
	            	addCoopUser(username);
            	}
                return;
            }
        }
        catch (Throwable e)
        {
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

	private boolean isDuplicateUser(String username) 
	{
		if (addedCoopUsers.containsKey(username) || existingCoopUsers.containsKey(username))
		{
			return true;
		}
		return false;
	}

	private void addCoopUser(String username) {
		CoopUser coopUser = new CoopUser();
		coopUser.setUsername(username);
		coopUser.setPassword("PWCG");
		coopUser.setNote("Created by host");
		coopUser.setApproved(true);
		
		addedCoopUsers.put(coopUser.getUsername(), coopUser);
	}

	public void writeResults() throws PWCGException 
	{
		for (CoopUser addedCoopUser : addedCoopUsers.values())
		{
			CoopUserIOJson.writeJson(addedCoopUser);
		}
		
		addedCoopUsers.clear();
	}
}

