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
import pwcg.coop.model.CoopUser;
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
    private Map<String, Integer> campaignCoopPersonas = new TreeMap<>();
    
	public CoopPersonaChooserPanel(Campaign campaign, BriefingCoopPersonaChooser parent)
	{
        super();
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
	        this.setThemedImageFromName(campaign, imagePath);
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
            int coopPersona = findCoopPlayerForSquadronMember(player);
            if (coopPersona > 0)
            {
                MultiSelectData selectData = buildSelectData(coopPersona);
                playerSquadronMembers.put(player.getNameAndRank(), player);
                campaignCoopPersonas.put(player.getNameAndRank(), coopPersona);
                selector.addNotAccepted(selectData);
            }
        }
    }
    
    private int findCoopPlayerForSquadronMember(SquadronMember player) throws PWCGException
    {
        CoopUser coopUserForPlayer = CoopUserManager.getIntance().getCoopUserForSquadronMember(campaign.getName(), player.getSerialNumber());
        if (coopUserForPlayer != null)
        {
            for (Integer coopPersona : coopUserForPlayer.getUserPersonas(campaign.getName()))
            {
                if (player.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE)
                {
                    if (player.getSerialNumber() == coopPersona)
                    {
                        return coopPersona;
                    }
                }
            }
        }
        return -1;
    }

    private MultiSelectData buildSelectData(Integer coopPersona) throws PWCGException
    {
        CoopUser coopUser = CoopUserManager.getIntance().getCoopUserForSquadronMember(campaign.getName(), coopPersona);
        MultiSelectData selectData = new MultiSelectData();
        
        SquadronMember pilot = campaign.getPersonnelManager().getAnyCampaignMember(coopPersona);
        selectData.setName(pilot.getNameAndRank());
        selectData.setText("User: " + coopUser.getUsername() +".  Pilot Name: "  + pilot.getNameAndRank());
        selectData.setInfo(
                "User: " + coopUser.getUsername() + 
                ".  Campaign: "  + campaign.getCampaignData().getName() + 
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
	
	public List<Integer> getAcceptedCoopPersonas() throws PWCGException
	{
        List<Integer> selectedCoopPersonas = new ArrayList<>();
        List<MultiSelectData> selectedRecords = selector.getAccepted();
		for (MultiSelectData selectedRecord : selectedRecords)
		{
		    Integer selectedCoopPersona = campaignCoopPersonas.get(selectedRecord.getName());
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
