package pwcg.gui.campaign;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeMap;

import javax.swing.JPanel;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.gui.campaign.home.CampaignHomeGUI;
import pwcg.gui.campaign.home.CampaignPilotChalkBoard;
import pwcg.gui.campaign.pilot.CampaignPilotListPanel;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;

/**
 * Generates chalk board and pilot set for top aces
 * 
 * @author Patrick Wilson
 *
 */
public class CampaignRosterTopAcesPanelFactory extends CampaignRosterBasePanelFactory
{
    public static final Integer ACE_VICTORY_SORT_CONSTANT = 100000;
    
	public CampaignRosterTopAcesPanelFactory(CampaignHomeGUI parent) throws PWCGException  
	{
		super(parent);
	}

    public void makeCampaignHomePanels() throws PWCGException  
    {
        CampaignPilotChalkBoard acesPanel = new CampaignPilotChalkBoard();
        acesPanel.makeSquadronPanel(sortedPilots);
        chalkboardPanel = acesPanel;
        
        CampaignPilotListPanel pilotList = new CampaignPilotListPanel(parent);
        pilotListPanel = pilotList.makeSquadronRightPanel(sortedPilots, "  Top Aces", "CampFlowPilot:");
        
        JPanel descPanel = makeDescPanel();
        pilotListPanel.add(descPanel, BorderLayout.CENTER);
   }

    public void makePilotList() throws PWCGException 
	{
		TreeMap<String, SquadronMember> allAcesInCampaign = new TreeMap<String, SquadronMember>();
		int aceCounter = 0;
		aceCounter = getAces(allAcesInCampaign, aceCounter);
		getAcesSortedByVictories(allAcesInCampaign);  
	}

    private void getAcesSortedByVictories(TreeMap<String, SquadronMember> allAcesInCampaign)
    {
        Map<String, SquadronMember> sortedPilotsByVictories = new TreeMap <>();
		int topAceCount = 0;
		NavigableSet<String> sortedAcesDescending = allAcesInCampaign.descendingKeySet();  	      
	    for (String key : sortedAcesDescending) 
	    {  
	    	// This little stunt sets the sort order
	    	SquadronMember ace = allAcesInCampaign.get(key);
			String newKey = new String ("" + (ACE_VICTORY_SORT_CONSTANT - ((100 * ace.getSquadronMemberVictories().getAirToAirVictories()) + topAceCount)));
			sortedPilotsByVictories.put(newKey, ace);
	        
	        ++topAceCount;

			PWCGLogger.log(LogLevel.DEBUG, "Selected Ace key: " + newKey + "     " + ace.getName() + "     " + ace.getSquadronMemberVictories().getAirToAirVictories());
	        
	        if (topAceCount == 10)
	        {
	        	break;
	        }
	    }
	    
	    sortedPilots = new ArrayList<SquadronMember>(sortedPilotsByVictories.values());
    }

    private int getAces(TreeMap<String, SquadronMember> allAcesInCampaign, int aceCounter) throws PWCGException
    {
        for (SquadronMember pilot : campaign.getPersonnelManager().getAllCampaignMembers().values())
		{
			if (pilot.getSquadronMemberVictories().getAirToAirVictories() > 5)
			{
				String key = new String ("" + (ACE_VICTORY_SORT_CONSTANT + (100 * pilot.getSquadronMemberVictories().getAirToAirVictories()) + aceCounter));
				++aceCounter;
				allAcesInCampaign.put(key, pilot);

				PWCGLogger.log(LogLevel.DEBUG, "Add Ace key: " + key + "     " + pilot.getName() + "     " + pilot.getSquadronMemberVictories().getAirToAirVictories());
			}
		}
        return aceCounter;
    }

    private JPanel makeDescPanel() throws PWCGException 
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "Plaque.jpg";
        ImageResizingPanel descPanel = new ImageResizingPanel(imagePath);
        descPanel.setLayout(new BorderLayout());
        
        return descPanel;
    }

}
