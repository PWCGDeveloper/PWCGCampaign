package pwcg.gui.maingui.coop;

import java.awt.BorderLayout;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.JTextField;
import javax.swing.border.Border;

import pwcg.coop.CoopUserManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;

public class CoopCreateUserPanel extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
	private JTextField coopUserNameText;

	public CoopCreateUserPanel() 
	{
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
	}

    public void makePanels()
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
            this.setImageFromName(imagePath);
            this.setBorder(BorderFactory.createEmptyBorder(150,40,40,150));

	        Pane centerPanel = makeCoopUserEntryPanel();
	        this.add(centerPanel, BorderLayout.NORTH);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private Pane makeCoopUserEntryPanel() throws PWCGException 
    {
	    Font font = PWCGMonitorFonts.getPrimaryFontLarge();

	    Pane centerPanel = new Pane();
	    centerPanel.setLayout(new BorderLayout());
	    centerPanel.setOpaque(false);

	    Pane dataEntryPanel = new Pane();
	    dataEntryPanel.setLayout(new BorderLayout());
	    dataEntryPanel.setOpaque(false);
	    
		Pane descPanel = buildCoopUserLabel(font);
		Pane textPanel = buildCoopUserTextField(font);
		Pane buttonPanel = buildCoopUserButtons(font);

        dataEntryPanel.add(descPanel, BorderLayout.WEST);
        dataEntryPanel.add(textPanel, BorderLayout.CENTER);
        dataEntryPanel.add(buttonPanel, BorderLayout.EAST);

        centerPanel.add(dataEntryPanel, BorderLayout.NORTH);
        
		return centerPanel;

	}

	private Pane buildCoopUserTextField(Font font) 
	{
		Pane textPanel = new Pane (new GridLayout(0,1));
		textPanel.setOpaque(false);
		
        coopUserNameText = new JTextField(25);
        coopUserNameText.setFont(font);
        coopUserNameText.setBackground(ColorMap.PAPER_BACKGROUND);
        textPanel.add(coopUserNameText);
		return textPanel;
	}

	private Pane buildCoopUserLabel(Font font) 
	{
		Pane descPanel = new Pane (new GridLayout(0,1));
		descPanel.setOpaque(false);
		
	    Label passwordLabel = new Label("Enter Coop Username: ");
	    passwordLabel.setFont(font);
	    passwordLabel.setBackground(ColorMap.NEWSPAPER_BACKGROUND);
	    descPanel.add(passwordLabel);
		return descPanel;
	}
	
	private Pane buildCoopUserButtons(Font font) 
	{
		Pane controlPanel = new Pane (new GridLayout(0,1));
		controlPanel.setOpaque(false);

	    Button createUserButton = new Button("Create Coop User");
	    createUserButton.setActionCommand("Create Coop User");
	    createUserButton.addActionListener(this);
	    createUserButton.setFont(font);
	    createUserButton.setBackground(ColorMap.NEWSPAPER_BACKGROUND);
	    
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

