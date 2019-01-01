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

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignHumanPilotHandler;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.io.json.CoopPilotIOJson;
import pwcg.campaign.squadmember.AiPilotRemovalChooser;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.coop.model.CoopPilot;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class CoopPilotAccept extends PwcgGuiContext implements ActionListener
{
	private static final long serialVersionUID = 1L;
    private SelectorGUI selector;
	
    private Map<String, CoopPilot> coopPilotRecords = new TreeMap<>();

	public CoopPilotAccept()
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
        List<CoopPilot> coopPilots = CoopPilotIOJson.readCoopPilots();
        for (CoopPilot coopPilot : coopPilots)
        {
            coopPilotRecords.put(coopPilot.getPilotName(), coopPilot);
            MultiSelectData selectData = buildSelectData(coopPilot);
            if (coopPilot.isApproved())
            {
                selector.addAccepted(selectData);
            }
            else
            {
                selector.addNotAccepted(selectData);
            }
        }
    }

    private MultiSelectData buildSelectData(CoopPilot coopPilot)
    {
        MultiSelectData selectData = new MultiSelectData();
        selectData.setName(coopPilot.getPilotName());
        selectData.setText("User: " + coopPilot.getUsername() +".  Pilot Name: "  + coopPilot.getPilotName());
        selectData.setInfo(
                "User: " + coopPilot.getUsername() + 
                ".  Campaign: "  + coopPilot.getCampaignName() + 
                ".  Pilot Name: "  + coopPilot.getPilotName() + 
                ".  Squadron: "  + coopPilot.getSquadronId() + 
                ".  " + coopPilot.getNote());
        return selectData;
    }

    public JPanel makeNavigatePanel() throws PWCGException  
    {
        String imagePath = getSideImageMain("ConfigLeft.jpg");

        JPanel navPanel = new ImageResizingPanel(imagePath);
        navPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        JButton acceptButton = PWCGButtonFactory.makeMenuButton("Accept", "Accept", this);
        buttonPanel.add(acceptButton);

        JButton cancelButton = PWCGButtonFactory.makeMenuButton("Cancel", "Cancel", this);
        buttonPanel.add(cancelButton);

        navPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return navPanel;
    }

	public JPanel makeAcceptancePanel() throws PWCGException 
	{	
        String imagePath = ContextSpecificImages.imagesMisc() + "Paper.jpg";
        ImageResizingPanel centerPanel = new ImageResizingPanel(imagePath);
        centerPanel.setLayout(new BorderLayout());
        
	    selector = new SelectorGUI();
	    boolean allowReject = false;
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
			if (action.equalsIgnoreCase("Accept"))
			{

		        
			    
                // create human pilot
			    // Add serial number to pilot record
                // Add human pilot
			    // remove ai pilot
			    writeResults();
			}
			else if (action.equalsIgnoreCase("Cancel"))
			{
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
	

    private void writeResults() throws PWCGException
    {
        for (MultiSelectData selectData: selector.getAccepted())
        {
            CoopPilot acceptedPilot = coopPilotRecords.get(selectData.getName());
            if (acceptedPilot != null)
            {
                if (acceptedPilot.getSerialNumber() == 0)
                {
                    int newPilotSerialNumber = addHumanPilot(acceptedPilot);
                    acceptedPilot.setSerialNumber(newPilotSerialNumber);
                    
                    acceptedPilot.setApproved(true);
                    CoopPilotIOJson.writeJson(acceptedPilot);
                }
            }
        }        
    }
    
    private int addHumanPilot(CoopPilot acceptedPilot) throws PWCGException 
    {
        Campaign campaign = new Campaign();
        campaign.open(acceptedPilot.getCampaignName());    
        PWCGContextManager.getInstance().setCampaign(campaign);

        AiPilotRemovalChooser pilotRemovalChooser = new AiPilotRemovalChooser(campaign);
        SquadronMember squadronMemberToReplace = pilotRemovalChooser.findAiPilotToRemove(acceptedPilot.getPilotRank(), acceptedPilot.getSquadronId());
        
        CampaignHumanPilotHandler humanPilotHandler = new CampaignHumanPilotHandler(campaign);
        int newPilotSerialNumber = humanPilotHandler.addNewPilot(
                acceptedPilot.getPilotName(), 
                acceptedPilot.getPilotRank(), 
                squadronMemberToReplace.getSerialNumber(), 
                acceptedPilot.getSquadronId());
        
        return newPilotSerialNumber;
    }
}
