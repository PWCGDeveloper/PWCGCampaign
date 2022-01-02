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
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;

public class BriefingCrewPlanePayloadSorter
{
    private Mission mission;
    private Map <Integer, CrewPlanePayloadPairing> assignedCrewMap = new HashMap <>();
    
    public BriefingCrewPlanePayloadSorter(Mission mission, Map <Integer, CrewPlanePayloadPairing> assignedCrewMap)
    {
        this.mission = mission;
        this.assignedCrewMap = assignedCrewMap;
    }

    public List<CrewPlanePayloadPairing> getAssignedCrewsSorted() throws PWCGException 
    {       
        List<CrewPlanePayloadPairing> assignedCrews = new ArrayList<CrewPlanePayloadPairing>(assignedCrewMap.values());
        List<CrewPlanePayloadPairing> sortedAssignedCrews = sortCrews(assignedCrews);
        
        return sortedAssignedCrews;
    }

    private List<CrewPlanePayloadPairing> sortCrews (Collection<CrewPlanePayloadPairing> unsorted) throws PWCGException 
    {
        List<CrewPlanePayloadPairing> sorted = new ArrayList<CrewPlanePayloadPairing>();
        Map<String, CrewPlanePayloadPairing> sortedTree = new TreeMap<String, CrewPlanePayloadPairing>();
                
        for (CrewPlanePayloadPairing crewPlane : unsorted)
        {
            CrewMember crewMemberSquadMember = crewPlane.getCrewMember();
            String crewKey = crewMemberSquadMember.determineSortKey(mission.getCampaign().getDate());
            sortedTree.put(crewKey, crewPlane);
        }
        
        sorted.addAll(sortedTree.values());
        
        return sorted;
    }

}
