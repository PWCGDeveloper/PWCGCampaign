package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.coop.CoopUserManager;
import pwcg.coop.model.CoopPersona;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.ISelectorGUICallback;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.MultiSelectData;
import pwcg.gui.utils.SelectorGUI;

public class CoopPersonaChooserPanel extends ImageResizingPanel implements ISelectorGUICallback
{
	private static final long serialVersionUID = 1L;
    private SelectorGUI selector;
    private BriefingCoopPersonaChooser parent;
    private Campaign campaign;
	
    private Map<String, SquadronMember> playerSquadronMembers = new TreeMap<>();
    private Map<String, CoopPersona> campaignCoopPersonas = new TreeMap<>();
    
    public static final String NO_USER_FOR_PILOT = "No User For Pilot";

	public CoopPersonaChooserPanel(Campaign campaign, BriefingCoopPersonaChooser parent)
	{
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
	    
	    this.campaign = campaign;
	    this.parent = parent;
	}
	
	public void makePanels() 
	{
		try
		{
	        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
	        this.setImage(imagePath);
	        this.setBorder(BorderFactory.createEmptyBorder(50,50,50,100));

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

    private void loadPanels() throws PWCGException
    {
        for (SquadronMember player : campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList())
        {
            CoopPersona coopPersona = findCoopPlayerForSquadronMember(player);
            MultiSelectData selectData = buildSelectData(coopPersona);
            playerSquadronMembers.put(player.getNameAndRank(), player);
            campaignCoopPersonas.put(player.getNameAndRank(), coopPersona);
            selector.addNotAccepted(selectData);
        }
    }
    
    private CoopPersona findCoopPlayerForSquadronMember(SquadronMember player) throws PWCGException
    {
        List<CoopPersona> coopPersonas = CoopUserManager.getIntance().getPersonasForCampaign(campaign);
        for (CoopPersona coopPersona : coopPersonas)
        {
            if (player.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE)
            {
                if (player.getSerialNumber() == coopPersona.getSerialNumber())
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
        noCoopPersona.setSerialNumber(player.getSerialNumber());
        
        return noCoopPersona;
    }

    private MultiSelectData buildSelectData(CoopPersona coopPersona) throws PWCGException
    {
        MultiSelectData selectData = new MultiSelectData();
        
        SquadronMember pilot = campaign.getPersonnelManager().getAnyCampaignMember(coopPersona.getSerialNumber());
        selectData.setName(pilot.getNameAndRank());
        selectData.setText("User: " + coopPersona.getCoopUsername() +".  Pilot Name: "  + pilot.getNameAndRank());
        selectData.setInfo(
                "User: " + coopPersona.getCoopUsername() + 
                ".  Campaign: "  + coopPersona.getCampaignName() + 
                ".  Pilot Name: "  + pilot.getNameAndRank() + 
                ".  Squadron: "  + pilot.getSquadronId());
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
			PWCGLogger.logException(e);
		}
	}
}
