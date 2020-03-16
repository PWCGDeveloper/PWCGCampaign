package pwcg.gui.maingui.coop;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import pwcg.coop.CoopUserManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;

public class CoopCreateUserPanel extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
	private JTextField coopUserNameText;

	public CoopCreateUserPanel() 
	{
	    super(ContextSpecificImages.imagesMisc() + "Paper.jpg");
	}

    public void makePanels()
    {
        try
        {
	        JPanel centerPanel = makeCoopUserEntryPanel();
	        this.add(centerPanel, BorderLayout.NORTH);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private JPanel makeCoopUserEntryPanel() throws PWCGException 
    {
	    Font font = MonitorSupport.getPrimaryFontLarge();

	    JPanel centerPanel = new JPanel();
	    centerPanel.setLayout(new BorderLayout());
	    centerPanel.setOpaque(false);

	    JPanel dataEntryPanel = new JPanel();
	    dataEntryPanel.setLayout(new BorderLayout());
	    dataEntryPanel.setOpaque(false);
	    
		JPanel descPanel = buildCoopUserLabel(font);
		JPanel textPanel = buildCoopUserTextField(font);
		JPanel buttonPanel = buildCoopUserButtons(font);

        dataEntryPanel.add(descPanel, BorderLayout.WEST);
        dataEntryPanel.add(textPanel, BorderLayout.CENTER);
        dataEntryPanel.add(buttonPanel, BorderLayout.EAST);

        centerPanel.add(dataEntryPanel, BorderLayout.NORTH);
        
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
	    createUserButton.setBackground(ColorMap.PAPER_OFFSET);
	    
	    Border raisedBorder = BorderFactory.createRaisedBevelBorder();
	    createUserButton.setBorder(raisedBorder);

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
            	else if (CoopUserManager.getIntance().isDuplicateUser(username))
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
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void addCoopUser(String username) throws PWCGException
    {
        CoopUserManager.getIntance().buildCoopUser(username);
    }
}

