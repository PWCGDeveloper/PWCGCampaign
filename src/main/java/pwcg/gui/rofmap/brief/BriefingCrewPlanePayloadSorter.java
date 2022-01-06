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
import pwcg.mission.playerunit.crew.CrewVehiclePayloadPairing;

public class BriefingCrewPlanePayloadSorter
{
    private Mission mission;
    private Map <Integer, CrewVehiclePayloadPairing> assignedCrewMap = new HashMap <>();
    
    public BriefingCrewPlanePayloadSorter(Mission mission, Map <Integer, CrewVehiclePayloadPairing> assignedCrewMap)
    {
        this.mission = mission;
        this.assignedCrewMap = assignedCrewMap;
    }

    public List<CrewVehiclePayloadPairing> getAssignedCrewsSorted() throws PWCGException 
    {       
        List<CrewVehiclePayloadPairing> assignedCrews = new ArrayList<CrewVehiclePayloadPairing>(assignedCrewMap.values());
        List<CrewVehiclePayloadPairing> sortedAssignedCrews = sortCrews(assignedCrews);
        
        return sortedAssignedCrews;
    }

    private List<CrewVehiclePayloadPairing> sortCrews (Collection<CrewVehiclePayloadPairing> unsorted) throws PWCGException 
    {
        List<CrewVehiclePayloadPairing> sorted = new ArrayList<CrewVehiclePayloadPairing>();
        Map<String, CrewVehiclePayloadPairing> sortedTree = new TreeMap<String, CrewVehiclePayloadPairing>();
                
        for (CrewVehiclePayloadPairing crewPlane : unsorted)
        {
            CrewMember crewMemberSquadMember = crewPlane.getCrewMember();
            String crewKey = crewMemberSquadMember.determineSortKey(mission.getCampaign().getDate());
            sortedTree.put(crewKey, crewPlane);
        }
        
        sorted.addAll(sortedTree.values());
        
        return sorted;
    }

}
