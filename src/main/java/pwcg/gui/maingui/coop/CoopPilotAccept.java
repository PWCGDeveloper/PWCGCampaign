package pwcg.gui.maingui.coop;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.MultiSelectData;
import pwcg.gui.utils.SelectorGUI;

public class CoopPilotAccept extends ImageResizingPanel
{
	private static final long serialVersionUID = 1L;
    private SelectorGUI selector;
	
    private Map<String, CoopPilot> coopPilotRecords = new TreeMap<>();

	public CoopPilotAccept()
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

	public JPanel makeAcceptancePanel() throws PWCGException 
	{	
	    selector = new SelectorGUI();
	    boolean allowReject = false;
        JPanel acceptPanel = selector.build(allowReject);
		return acceptPanel;
	}

    public void writeResults() throws PWCGException
    {
        for (MultiSelectData selectData: selector.getAccepted())
        {
            CoopPilot acceptedPilot = coopPilotRecords.get(selectData.getName());
            if (acceptedPilot != null)
            {
                if (acceptedPilot.getSerialNumber() == 0)
                {
                    int newPilotSerialNumber = addHumanPilot(acceptedPilot);
                    updateHumanPilotRecord(acceptedPilot, newPilotSerialNumber);
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
        
        campaign.write();
        
        return newPilotSerialNumber;
    }
    
    private void updateHumanPilotRecord(CoopPilot acceptedPilot, int newPilotSerialNumber) throws PWCGException
    {
        acceptedPilot.setSerialNumber(newPilotSerialNumber);
        
        acceptedPilot.setApproved(true);
        CoopPilotIOJson.writeJson(acceptedPilot);
    }

}
