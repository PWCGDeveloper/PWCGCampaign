package pwcg.mission.briefing;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;

public class BriefingAssignmentData
{
    private Map<Integer, CrewPlanePayloadPairing> assignedCrewPlanes = new HashMap<>();
    private Map<Integer, SquadronMember> assignedPilots = new HashMap<>();
    private Map<Integer, SquadronMember> unAssignedPilots = new HashMap<>();
    private Map<Integer, EquippedPlane> assignedPlanes = new HashMap<>();
    private Map<Integer, EquippedPlane> unAssignedPlanes = new HashMap<>();

    public void reset()
    {
        assignedCrewPlanes.clear();
        assignedPilots.clear();
        unAssignedPilots.clear();
        assignedPlanes.clear();
        unAssignedPlanes.clear();
    }

    public void addPilot(SquadronMember squadronMember)
    {
        unAssignedPilots.put(squadronMember.getSerialNumber(), squadronMember);
    }

    public void addPlane(EquippedPlane equippedPlane)
    {
        unAssignedPlanes.put(equippedPlane.getSerialNumber(), equippedPlane);
    }

    public void assignPilot(int pilotSerialNumber, int planeSerialNumber)
    {
        SquadronMember assignedPilot = unAssignedPilots.remove(pilotSerialNumber);
        assignedPilots.put(assignedPilot.getSerialNumber(), assignedPilot);
        
        EquippedPlane equippedPlane = unAssignedPlanes.remove(planeSerialNumber);
        assignedPlanes.put(equippedPlane.getSerialNumber(), equippedPlane);
        
        CrewPlanePayloadPairing crewPlanePayloadPairing = new CrewPlanePayloadPairing(assignedPilot, equippedPlane);
        crewPlanePayloadPairing.setPayloadId(CrewPlanePayloadPairing.NO_PAYLOAD_ASSIGNED);
        crewPlanePayloadPairing.clearModification();

        assignedCrewPlanes.put(assignedPilot.getSerialNumber(), crewPlanePayloadPairing);
    }

    public void unassignPilot(int pilotSerialNumber)
    {
        CrewPlanePayloadPairing crewPlanePayloadPairing = assignedCrewPlanes.remove(pilotSerialNumber);

        assignedPilots.remove(crewPlanePayloadPairing.getPilot().getSerialNumber());
        unAssignedPilots.put(crewPlanePayloadPairing.getPilot().getSerialNumber(), crewPlanePayloadPairing.getPilot());

        assignedPlanes.remove(crewPlanePayloadPairing.getPlane().getSerialNumber());
        unAssignedPlanes.put(crewPlanePayloadPairing.getPlane().getSerialNumber(), crewPlanePayloadPairing.getPlane());
    }
    
    public void changePlane(int pilotSerialNumber, Integer planeSerialNumber)
    {
        CrewPlanePayloadPairing crewPlanePayloadPairing = assignedCrewPlanes.get(pilotSerialNumber);

        assignedPlanes.remove(crewPlanePayloadPairing.getPlane().getSerialNumber());
        unAssignedPlanes.put(crewPlanePayloadPairing.getPlane().getSerialNumber(), crewPlanePayloadPairing.getPlane());
        
        EquippedPlane equippedPlane = unAssignedPlanes.remove(planeSerialNumber);
        assignedPlanes.put(equippedPlane.getSerialNumber(), equippedPlane);
 
        crewPlanePayloadPairing.setPlane(equippedPlane);
        crewPlanePayloadPairing.setPayloadId(CrewPlanePayloadPairing.NO_PAYLOAD_ASSIGNED);
        crewPlanePayloadPairing.clearModification();
    }

    public void modifyPayload(Integer pilotSerialNumber, int payloadId) 
    {
        CrewPlanePayloadPairing crewPlane = assignedCrewPlanes.get(pilotSerialNumber);
        crewPlane.setPayloadId(payloadId);
    }

    public Map<Integer, CrewPlanePayloadPairing> getAssignedCrewPlanes()
    {
        return assignedCrewPlanes;
    }

    public Map<Integer, SquadronMember> getAssignedPilots()
    {
        return assignedPilots;
    }

    public Map<Integer, SquadronMember> getUnassignedPilots()
    {
        return unAssignedPilots;
    }

    public Map<Integer, EquippedPlane> getAssignedPlanes()
    {
        return assignedPlanes;
    }

    public Map<Integer, EquippedPlane> getUnassignedPlanes()
    {
        return unAssignedPlanes;
    }
}
