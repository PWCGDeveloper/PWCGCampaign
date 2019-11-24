package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.coop.CoopPersonaManager;
import pwcg.coop.model.CoopPersona;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ISelectorGUICallback;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.MultiSelectData;
import pwcg.gui.utils.SelectorGUI;

public class CoopPersonaChooserPanel extends ImageResizingPanel implements ISelectorGUICallback
{
	private static final long serialVersionUID = 1L;
    private SelectorGUI selector;
    private CoopPersonaChooser parent;
    private Campaign campaign;
	
    private Map<String, SquadronMember> playerSquadronMembers = new TreeMap<>();
    private Map<String, CoopPersona> campaignCoopPersonas = new TreeMap<>();
    
    public static final String NO_USER_FOR_PILOT = "No User For Pilot";

	public CoopPersonaChooserPanel(Campaign campaign, CoopPersonaChooser parent)
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
            CoopPersona coopPersona = findCoopPlayerForSquadronMember(player);
            MultiSelectData selectData = buildSelectData(coopPersona);
            playerSquadronMembers.put(player.getName(), player);
            campaignCoopPersonas.put(player.getName(), coopPersona);
            selector.addNotAccepted(selectData);
        }
    }
    
    private CoopPersona findCoopPlayerForSquadronMember(SquadronMember player) throws PWCGException
    {
        List<CoopPersona> coopPersonas = CoopPersonaManager.getIntance().getCoopPersonasForCampaign(campaign);
        for (CoopPersona coopPersona : coopPersonas)
        {
            if (player.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE)
            {
                if (player.getName().equals(coopPersona.getPilotName()))
                {
                    return coopPersona;
                }
            }
        }
        return makeNoCoopPersona(player);
    }

    private CoopPersona makeNoCoopPersona(SquadronMember player)
    {
        CoopPersona noCoopPersona = new CoopPersona();
        noCoopPersona.setCampaignName(campaign.getCampaignData().getName());
        noCoopPersona.setApproved(true);
        noCoopPersona.setPilotName(player.getName());
        noCoopPersona.setPilotRank(player.getRank());
        noCoopPersona.setSerialNumber(player.getSerialNumber());
        noCoopPersona.setSquadronId(player.getSquadronId());
        noCoopPersona.setUsername(NO_USER_FOR_PILOT);
        
        return noCoopPersona;
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
                ".  Squadron: "  + coopPersona.getSquadronId());
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
	
	public List<CoopPersona> getAcceptedCoopPersonas() throws PWCGException
	{
        List<CoopPersona> selectedCoopPersonas = new ArrayList<>();
        List<MultiSelectData> selectedRecords = selector.getAccepted();
		for (MultiSelectData selectedRecord : selectedRecords)
		{
			CoopPersona selectedCoopPersona = campaignCoopPersonas.get(selectedRecord.getName());
			selectedCoopPersonas.add(selectedCoopPersona);
		}
		return selectedCoopPersonas;
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
