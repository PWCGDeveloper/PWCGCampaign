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
import pwcg.gui.utils.ISelectorGUICallback;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.MultiSelectData;
import pwcg.gui.utils.SelectorGUI;

public class CoopPilotChooserPanel extends ImageResizingPanel implements ISelectorGUICallback
{
	private static final long serialVersionUID = 1L;
    private SelectorGUI selector;
    private CoopPilotChooser parent;
    private Campaign campaign;
	
    private Map<String, SquadronMember> playerSquadronMembers = new TreeMap<>();
    private Map<String, CoopPilot> campaignCoopPilots = new TreeMap<>();
    
    public static final String NO_USER_FOR_PILOT = "No User For Pilot";

	public CoopPilotChooserPanel(Campaign campaign, CoopPilotChooser parent)
	{
	    super(ContextSpecificImages.imagesMisc() + "Paper.jpg");
	    this.campaign = campaign;
	    this.parent = parent;
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
        for (SquadronMember player : campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList())
        {
            CoopPilot coopPilot = findCoopPlayerForSquadronMember(player);
            MultiSelectData selectData = buildSelectData(coopPilot);
            playerSquadronMembers.put(player.getName(), player);
            campaignCoopPilots.put(player.getName(), coopPilot);
            selector.addNotAccepted(selectData);
        }
    }
    
    private CoopPilot findCoopPlayerForSquadronMember(SquadronMember player) throws PWCGException
    {
        List<CoopPilot> coopPilots = CoopPilotIOJson.readCoopPilots();
        for (CoopPilot coopPilot : coopPilots)
        {
            if (campaign.getCampaignData().getName().equals(coopPilot.getCampaignName()))
            {
                if (player.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE)
                {
                    if (player.getName().equals(coopPilot.getPilotName()))
                    {
                        return coopPilot;
                    }
                }
            }
        }
        return makeNoCoopPilot(player);
    }

    private CoopPilot makeNoCoopPilot(SquadronMember player)
    {
        CoopPilot noCoopPilot = new CoopPilot();
        noCoopPilot.setCampaignName(campaign.getCampaignData().getName());
        noCoopPilot.setApproved(true);
        noCoopPilot.setPilotName(player.getName());
        noCoopPilot.setPilotRank(player.getRank());
        noCoopPilot.setSerialNumber(player.getSerialNumber());
        noCoopPilot.setSquadronId(player.getSquadronId());
        noCoopPilot.setUsername(NO_USER_FOR_PILOT);
        
        return noCoopPilot;
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
                ".  Squadron: "  + coopPilot.getSquadronId());
        return selectData;
    }

	public JPanel makeAcceptancePanel() throws PWCGException 
	{	
	    selector = new SelectorGUI();
	    selector.registerCallback(this);
	    boolean allowReject = true;
        JPanel acceptPanel = selector.build(allowReject);
		return acceptPanel;
	}
	
	public List<SquadronMember> getAcceptedSquadronMembers() throws PWCGException
	{
		List<SquadronMember> selectedPlayers = new ArrayList<>();
		List<MultiSelectData> selectedRecords = selector.getAccepted();
		for (MultiSelectData selectedRecord : selectedRecords)
		{
		    SquadronMember selectedPlayer = playerSquadronMembers.get(selectedRecord.getName());
			selectedPlayers.add(selectedPlayer);
		}
		return selectedPlayers;
	}
	
	public List<CoopPilot> getAcceptedCoopPilots() throws PWCGException
	{
        List<CoopPilot> selectedCoopPilots = new ArrayList<>();
        List<MultiSelectData> selectedRecords = selector.getAccepted();
		for (MultiSelectData selectedRecord : selectedRecords)
		{
			CoopPilot selectedCoopPilot = campaignCoopPilots.get(selectedRecord.getName());
			selectedCoopPilots.add(selectedCoopPilot);
		}
		return selectedCoopPilots;
	}

	@Override
	public void onSelectCallback() 
	{
		try
		{
			parent.evaluateErrors();
		}
		catch (Exception e)
		{
			Logger.logException(e);
		}
	}
}
