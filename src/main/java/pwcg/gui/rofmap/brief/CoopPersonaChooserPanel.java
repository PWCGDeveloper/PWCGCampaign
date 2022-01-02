package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
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
	
    private Map<String, CrewMember> playerCrewMembers = new TreeMap<>();
    private Map<String, Integer> campaignCoopPersonas = new TreeMap<>();
    
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
	        this.setImageFromName(imagePath);
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
        for (CrewMember player : campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList())
        {
            int coopPersona = findCoopPlayerForCrewMember(player);
            if (coopPersona > 0)
            {
                MultiSelectData selectData = buildSelectData(coopPersona);
                playerCrewMembers.put(player.getNameAndRank(), player);
                campaignCoopPersonas.put(player.getNameAndRank(), coopPersona);
                selector.addNotAccepted(selectData);
            }
        }
    }
    
    private int findCoopPlayerForCrewMember(CrewMember player) throws PWCGException
    {
        CoopUser coopUserForPlayer = CoopUserManager.getIntance().getCoopUserForCrewMember(campaign.getName(), player.getSerialNumber());
        if (coopUserForPlayer != null)
        {
            for (Integer coopPersona : coopUserForPlayer.getUserPersonas(campaign.getName()))
            {
                if (player.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_ACTIVE)
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
        CoopUser coopUser = CoopUserManager.getIntance().getCoopUserForCrewMember(campaign.getName(), coopPersona);
        MultiSelectData selectData = new MultiSelectData();
        
        CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(coopPersona);
        selectData.setName(crewMember.getNameAndRank());
        selectData.setText("User: " + coopUser.getUsername() +".  CrewMember Name: "  + crewMember.getNameAndRank());
        selectData.setInfo(
                "User: " + coopUser.getUsername() + 
                ".  Campaign: "  + campaign.getCampaignData().getName() + 
                ".  CrewMember Name: "  + crewMember.getNameAndRank() + 
                ".  Squadron: "  + crewMember.getCompanyId());
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
	
	public List<CrewMember> getAcceptedCrewMembers() throws PWCGException
	{
		List<CrewMember> selectedPlayers = new ArrayList<>();
		List<MultiSelectData> selectedRecords = selector.getAccepted();
		for (MultiSelectData selectedRecord : selectedRecords)
		{
		    CrewMember selectedPlayer = playerCrewMembers.get(selectedRecord.getName());
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
