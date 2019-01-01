package pwcg.gui.campaign.coop;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import pwcg.campaign.io.json.CoopUserIOJson;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class CoopUserAccept extends PwcgGuiContext implements ActionListener
{
	private static final long serialVersionUID = 1L;
    private SelectorGUI selector;
	
    private Map<String, CoopUser> coopUserRecords = new TreeMap<>();

	public CoopUserAccept()
	{
	    super();
	}
	
	public void makePanels() 
	{
		try
		{
	        setCenterPanel(makeAcceptancePanel());
	        setLeftPanel(makeNavigatePanel());
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

    private void writeResults() throws PWCGException
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

    public JPanel makeNavigatePanel() throws PWCGException  
    {
        String imagePath = getSideImageMain("ConfigLeft.jpg");

        JPanel navPanel = new ImageResizingPanel(imagePath);
        navPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        JButton finishedButton = PWCGButtonFactory.makeMenuButton("Finished", "Finished", this);
        buttonPanel.add(finishedButton);

        navPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return navPanel;
    }

	public JPanel makeAcceptancePanel() throws PWCGException 
	{	
        String imagePath = ContextSpecificImages.imagesMisc() + "Paper.jpg";
        ImageResizingPanel centerPanel = new ImageResizingPanel(imagePath);
        centerPanel.setLayout(new BorderLayout());
        
	    selector = new SelectorGUI();
	    boolean allowReject = true;
        JPanel acceptPanel = selector.build(allowReject);
        centerPanel.add(acceptPanel, BorderLayout.CENTER);
		
		add(centerPanel, BorderLayout.CENTER);
		
		return centerPanel;
	}

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			String action = ae.getActionCommand();
			if (action.equalsIgnoreCase("Finished"))
			{
                writeResults();
		        CampaignGuiContextManager.getInstance().popFromContextStack();
				return;
			}
		}
		catch (Throwable e)
		{
			Logger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}
}
