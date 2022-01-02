package pwcg.gui.campaign.home;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.TankAce;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.dialogs.ErrorDialog;

public class TopAcesListBuilder
{
    public enum TopAcesListType
    {
        TOP_ACES_ALL,
        TOP_ACES_SERVICE,
        TOP_ACES_NO_HISTORICAL;
    }
    
    public static final Integer ACE_VICTORY_SORT_CONSTANT = 100000;
    
    private Campaign campaign;

	public TopAcesListBuilder(Campaign campaign) throws PWCGException  
	{
        this.campaign = campaign;
	}

    public List<CrewMember> getTopTenAces(TopAcesListType topAcesListType)  
    {
        try
        {
            List<CrewMember> allAces = getAcesSortedByVictories();
            List<CrewMember> reducedAces = reduceAces(allAces, topAcesListType);
            return reducedAces;
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
        
        return new ArrayList<>();
    }

    private List<CrewMember> reduceAces(List<CrewMember> allAces, TopAcesListType topAcesListType) throws PWCGException
    {
        if (topAcesListType == TopAcesListType.TOP_ACES_SERVICE)
        {
            return reduceAcesPlayerServiceOnly(allAces);
        }
        else if (topAcesListType == TopAcesListType.TOP_ACES_NO_HISTORICAL)
        {
            return reduceAcesNoHistorical(allAces);
        }
        else
        {
            return reduceAcesToTen(allAces);
        }
    }

    private List<CrewMember> reduceAcesPlayerServiceOnly(List<CrewMember> allAces) throws PWCGException
    {
        CrewMember referencePlayer = campaign.findReferencePlayer();
        
        List<CrewMember> reducedAces = new ArrayList<>();
        for (CrewMember ace : allAces)
        {
            if (ace.determineService(campaign.getDate()) == referencePlayer.determineService(campaign.getDate()))
            {
                reducedAces.add(ace);
            }
            
            if (reducedAces.size() == 10)
            {
                return reducedAces;
            }
        }
        return reducedAces;
    }

    private List<CrewMember> reduceAcesNoHistorical(List<CrewMember> allAces)
    {
        List<CrewMember> reducedAces = new ArrayList<>();
        for (CrewMember ace : allAces)
        {
            if (!(ace instanceof TankAce))
            {
                reducedAces.add(ace);
            }
            
            if (reducedAces.size() == 10)
            {
                return reducedAces;
            }
        }
        return reducedAces;
    }

    private List<CrewMember> reduceAcesToTen(List<CrewMember> allAces)
    {
        List<CrewMember> reducedAces = new ArrayList<>();
        for (CrewMember ace : allAces)
        {
            reducedAces.add(ace);
            if (reducedAces.size() == 10)
            {
                return reducedAces;
            }
        }
        return reducedAces;
    }

    private List<CrewMember> getAcesSortedByVictories() throws PWCGException
    {
        Map<Integer, CrewMember> allCrewMembersInCampaign = campaign.getPersonnelManager().getAllCampaignMembers();
        
        Map<String, CrewMember> sortedAcesByVictories = new TreeMap <>();
        int topAceCount = 1;
	    for (CrewMember ace : allCrewMembersInCampaign.values()) 
	    {  
	        if (ace.getCrewMemberVictories().getAirToAirVictoryCount() >= 5)
	        {
    			String newKey = new String ("" + (ACE_VICTORY_SORT_CONSTANT - ((100 * ace.getCrewMemberVictories().getAirToAirVictoryCount()) + topAceCount)));
    			sortedAcesByVictories.put(newKey, ace);    			
    			++topAceCount;
	        }
	    }
	    
	    return new ArrayList<CrewMember>(sortedAcesByVictories.values());
    }
}
