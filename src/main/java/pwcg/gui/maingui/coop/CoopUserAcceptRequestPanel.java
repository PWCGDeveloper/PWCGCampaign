package pwcg.gui.maingui.coop;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import pwcg.coop.CoopUserManager;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.MultiSelectData;
import pwcg.gui.utils.SelectorGUI;

public class CoopUserAcceptRequestPanel extends ImageResizingPanel
{
	private static final long serialVersionUID = 1L;
    private SelectorGUI selector;
	
	public CoopUserAcceptRequestPanel()
	{
	    super(ContextSpecificImages.imagesMisc() + "Paper.jpg");
	}
	
	public void makePanels() 
	{
		try
		{
	        JPanel centerPanel = makeAcceptancePanel();
	        this.add(centerPanel, BorderLayout.CENTER);
	        loadPanels();
		}
		catch (Throwable e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

	private JPanel makeAcceptancePanel() throws PWCGException 
	{	
	    selector = new SelectorGUI();
	    boolean allowReject = true;
        JPanel acceptPanel = selector.build(allowReject);				
		return acceptPanel;
	}

    private void loadPanels() throws PWCGException
    {
        List<CoopUser> coopUsers = CoopUserManager.getIntance().getAllCoopUsers();
        for (CoopUser coopUser : coopUsers)
        {
            if (coopUser.isApproved())
            {
                MultiSelectData selectData = buildSelectData(coopUser);
                selector.addAccepted(selectData);
            }
            else
            {
                MultiSelectData selectData = buildSelectData(coopUser);
                selector.addNotAccepted(selectData);
            }
        }
    }

    private MultiSelectData buildSelectData(CoopUser coopUser)
    {
        MultiSelectData selectData = new MultiSelectData();
        selectData.setName(coopUser.getUsername());
        selectData.setText("User: " + coopUser.getUsername() +".");
        selectData.setInfo(
                "User: " + coopUser.getUsername() + 
                ".  " + coopUser.getNote());
        return selectData;
    }

    public void writeResults() throws PWCGException
    {
        List<String> acceptedUsers = getAcceptedUsers();
        List<String> rejectedUsers = getRejectedUsers();
        CoopUserManager.getIntance().setUserAcceptedStatus(acceptedUsers, rejectedUsers);
    }

    private List<String> getAcceptedUsers() throws PWCGException
    {
        List<String> acceptedUsers = new ArrayList<>();
        for (MultiSelectData selectData: selector.getAccepted())
        {
            acceptedUsers.add(selectData.getName());
        }
        return acceptedUsers;        
    }

    private List<String> getRejectedUsers() throws PWCGException
    {
        List<String> rejectedUsers = new ArrayList<>();
        for (MultiSelectData selectData: selector.getNotAccepted())
        {
            rejectedUsers.add(selectData.getName());
        }
        return rejectedUsers;        
    }
}
