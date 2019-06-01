package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.io.json.CoopPilotIOJson;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.coop.model.CoopPilot;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.MultiSelectData;
import pwcg.gui.utils.SelectorGUI;

public class CoopPilotChooserPanel extends ImageResizingPanel
{
	private static final long serialVersionUID = 1L;
    private SelectorGUI selector;
    private Campaign campaign;
	
    private Map<String, CoopPilot> coopPilotRecords = new TreeMap<>();

	public CoopPilotChooserPanel(Campaign campaign)
	{
	    super(ContextSpecificImages.imagesMisc() + "Paper.jpg");
	    this.campaign = campaign;
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
        	if (campaign.getCampaignData().getName().equals(coopPilot.getCampaignName()))
        	{
        		SquadronMember coopSquadronMember = campaign.getPersonnelManager().getAnyCampaignMember(coopPilot.getSerialNumber());
        		if (coopSquadronMember.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE)
        		{
		            coopPilotRecords.put(coopPilot.getPilotName(), coopPilot);
		            MultiSelectData selectData = buildSelectData(coopPilot);
	                selector.addNotAccepted(selectData);
        		}
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
	    boolean allowReject = true;
        JPanel acceptPanel = selector.build(allowReject);
		return acceptPanel;
	}
	
	public List<SquadronMember> getAcceptedPilots() throws PWCGException
	{
		List<SquadronMember> selectedCoopPilots = new ArrayList<>();
		List<MultiSelectData> selectedRecords = selector.getAccepted();
		for (MultiSelectData selectedRecord : selectedRecords)
		{
			CoopPilot selectedCoopPilot = coopPilotRecords.get(selectedRecord.getName());
			SquadronMember selectedPlayer = campaign.getPersonnelManager().getAnyCampaignMember(selectedCoopPilot.getSerialNumber());
			selectedCoopPilots.add(selectedPlayer);
		}
		return selectedCoopPilots;
	}
}
