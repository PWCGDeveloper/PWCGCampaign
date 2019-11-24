package pwcg.gui.maingui.coop;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignHumanPilotHandler;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.AiPilotRemovalChooser;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.coop.CoopPersonaManager;
import pwcg.coop.model.CoopPersona;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.MultiSelectData;
import pwcg.gui.utils.SelectorGUI;

public class CoopPersonaAcceptPanel extends ImageResizingPanel
{
	private static final long serialVersionUID = 1L;
    private SelectorGUI selector;
	
	public CoopPersonaAcceptPanel()
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
        List<CoopPersona> coopPersonas = CoopPersonaManager.getIntance().getAllCoopPersonas();
        for (CoopPersona coopPersona : coopPersonas)
        {
            MultiSelectData selectData = buildSelectData(coopPersona);
            if (coopPersona.isApproved())
            {
                selector.addAccepted(selectData);
            }
            else
            {
                selector.addNotAccepted(selectData);
            }
        }
    }

    private MultiSelectData buildSelectData(CoopPersona coopPersona)
    {
        MultiSelectData selectData = new MultiSelectData();
        selectData.setName(coopPersona.getPilotName());
        selectData.setText("User: " + coopPersona.getUsername() +".  Pilot Name: "  + coopPersona.getPilotName());
        selectData.setInfo(
                "User: " + coopPersona.getUsername() + 
                ".  Campaign: "  + coopPersona.getCampaignName() + 
                ".  Pilot Name: "  + coopPersona.getPilotName() + 
                ".  Squadron: "  + coopPersona.getSquadronId() + 
                ".  " + coopPersona.getNote());
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
            CoopPersona acceptedPersona = CoopPersonaManager.getIntance().getCoopPersona(selectData.getName());
            if (acceptedPersona != null)
            {
                int newPilotSerialNumber = addHumanPilot(acceptedPersona);
                CoopPersonaManager.getIntance().acceptPersonaIntoCampaign(selectData.getName(), newPilotSerialNumber);
            }
        }        
    }
    
    private int addHumanPilot(CoopPersona acceptedPilot) throws PWCGException 
    {
        Campaign campaign = new Campaign();
        campaign.open(acceptedPilot.getCampaignName());    
        PWCGContext.getInstance().setCampaign(campaign);

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
}
