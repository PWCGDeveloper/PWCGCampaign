package pwcg.campaign.squadmember;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class SquadronMemberSorter
{
    public static List<SquadronMember> sortSquadronMembers (Campaign campaign, Map <Integer, SquadronMember> unsorted) throws PWCGException 
    {
        List<SquadronMember> sorted = new ArrayList<>();
        Map<String, SquadronMember> sortedTree = new TreeMap<>();
                
        for (SquadronMember squadronMember : unsorted.values())
        {
            String crewKey = squadronMember.determineSortKey(campaign);
            sortedTree.put(crewKey, squadronMember);
        }
        
        sorted.addAll(sortedTree.values());
        
        return sorted;
    }

}
