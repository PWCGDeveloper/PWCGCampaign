package pwcg.gui.maingui.coop;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;

import pwcg.campaign.io.json.CoopUserIOJson;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.MultiSelectData;
import pwcg.gui.utils.SelectorGUI;

public class CoopUserAccept extends ImageResizingPanel
{
	private static final long serialVersionUID = 1L;
    private SelectorGUI selector;
	
    private Map<String, CoopUser> coopUserRecords = new TreeMap<>();

	public CoopUserAccept()
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
			Logger.logException(e);
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
        List<CoopUser> coopUsers = CoopUserIOJson.readCoopUsers();
        for (CoopUser coopUser : coopUsers)
        {
            coopUserRecords.put(coopUser.getUsername(), coopUser);
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
        writeAcceptedUsers();
        writeRejectedUsers();
    }

    private void writeAcceptedUsers() throws PWCGException
    {
        for (MultiSelectData selectData: selector.getAccepted())
        {
            CoopUser acceptedUser = coopUserRecords.get(selectData.getName());
            if (acceptedUser != null)
            {
                acceptedUser.setApproved(true);
                CoopUserIOJson.writeJson(acceptedUser);
            }
        }        
    }

    private void writeRejectedUsers() throws PWCGException
    {
        for (MultiSelectData selectData: selector.getNotAccepted())
        {
            CoopUser rejectedUser = coopUserRecords.get(selectData.getName());
            if (rejectedUser != null)
            {
                rejectedUser.setApproved(false);
                CoopUserIOJson.writeJson(rejectedUser);
            }
        }        
    }
}
