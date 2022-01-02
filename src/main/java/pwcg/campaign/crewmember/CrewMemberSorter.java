package pwcg.campaign.crewmember;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class CrewMemberSorter
{
    public static List<CrewMember> sortCrewMembers (Campaign campaign, Map <Integer, CrewMember> unsorted) throws PWCGException 
    {
        List<CrewMember> sorted = new ArrayList<>();
        Map<String, CrewMember> sortedTree = new TreeMap<>();
                
        for (CrewMember crewMember : unsorted.values())
        {
            String crewKey = crewMember.determineSortKey(campaign.getDate());
            sortedTree.put(crewKey, crewMember);
        }
        
        sorted.addAll(sortedTree.values());
        
        return sorted;
    }

}
