package pwcg.gui.rofmap.brief;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.playerunit.crew.CrewTankPayloadPairing;

public class BriefingCrewPlanePayloadSorter
{
    private Mission mission;
    private Map <Integer, CrewTankPayloadPairing> assignedCrewMap = new HashMap <>();
    
    public BriefingCrewPlanePayloadSorter(Mission mission, Map <Integer, CrewTankPayloadPairing> assignedCrewMap)
    {
        this.mission = mission;
        this.assignedCrewMap = assignedCrewMap;
    }

    public List<CrewTankPayloadPairing> getAssignedCrewsSorted() throws PWCGException 
    {       
        List<CrewTankPayloadPairing> assignedCrews = new ArrayList<CrewTankPayloadPairing>(assignedCrewMap.values());
        List<CrewTankPayloadPairing> sortedAssignedCrews = sortCrews(assignedCrews);
        
        return sortedAssignedCrews;
    }

    private List<CrewTankPayloadPairing> sortCrews (Collection<CrewTankPayloadPairing> unsorted) throws PWCGException 
    {
        List<CrewTankPayloadPairing> sorted = new ArrayList<CrewTankPayloadPairing>();
        Map<String, CrewTankPayloadPairing> sortedTree = new TreeMap<String, CrewTankPayloadPairing>();
                
        for (CrewTankPayloadPairing crewPlane : unsorted)
        {
            CrewMember crewMemberSquadMember = crewPlane.getCrewMember();
            String crewKey = crewMemberSquadMember.determineSortKey(mission.getCampaign().getDate());
            sortedTree.put(crewKey, crewPlane);
        }
        
        sorted.addAll(sortedTree.values());
        
        return sorted;
    }

}
