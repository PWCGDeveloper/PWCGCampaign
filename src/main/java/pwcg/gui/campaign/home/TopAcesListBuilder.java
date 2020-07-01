package pwcg.gui.campaign.home;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
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

    public List<SquadronMember> getTopTenAces(TopAcesListType topAcesListType)  
    {
        try
        {
            List<SquadronMember> allAces = getAcesSortedByVictories();
            List<SquadronMember> reducedAces = reduceAces(allAces, topAcesListType);
            return reducedAces;
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
        
        return new ArrayList<>();
    }

    private List<SquadronMember> reduceAces(List<SquadronMember> allAces, TopAcesListType topAcesListType) throws PWCGException
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

    private List<SquadronMember> reduceAcesPlayerServiceOnly(List<SquadronMember> allAces) throws PWCGException
    {
        SquadronMember referencePlayer = campaign.findReferencePlayer();
        
        List<SquadronMember> reducedAces = new ArrayList<>();
        for (SquadronMember ace : allAces)
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

    private List<SquadronMember> reduceAcesNoHistorical(List<SquadronMember> allAces)
    {
        List<SquadronMember> reducedAces = new ArrayList<>();
        for (SquadronMember ace : allAces)
        {
            if (!(ace instanceof Ace))
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

    private List<SquadronMember> reduceAcesToTen(List<SquadronMember> allAces)
    {
        List<SquadronMember> reducedAces = new ArrayList<>();
        for (SquadronMember ace : allAces)
        {
            reducedAces.add(ace);
            if (reducedAces.size() == 10)
            {
                return reducedAces;
            }
        }
        return reducedAces;
    }

    private List<SquadronMember> getAcesSortedByVictories() throws PWCGException
    {
        Map<Integer, SquadronMember> allPilotsInCampaign = campaign.getPersonnelManager().getAllCampaignMembers();
        
        Map<String, SquadronMember> sortedAcesByVictories = new TreeMap <>();
        int topAceCount = 1;
	    for (SquadronMember ace : allPilotsInCampaign.values()) 
	    {  
	        if (ace.getVictories().size() >= 5)
	        {
    			String newKey = new String ("" + (ACE_VICTORY_SORT_CONSTANT - ((100 * ace.getSquadronMemberVictories().getAirToAirVictories()) + topAceCount)));
    			sortedAcesByVictories.put(newKey, ace);    			
    			++topAceCount;
	        }
	    }
	    
	    return new ArrayList<SquadronMember>(sortedAcesByVictories.values());
    }
}
