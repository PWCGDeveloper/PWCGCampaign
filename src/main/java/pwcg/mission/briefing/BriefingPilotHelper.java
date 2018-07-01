package pwcg.mission.briefing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;
import pwcg.mission.flight.plane.PlaneMCU;

public class BriefingPilotHelper
{
    private Mission mission;
    private Map <Integer, CrewPlanePayloadPairing> assignedCrewMap = new HashMap <>();
    private Map <Integer, CrewPlanePayloadPairing> unAssignedCrewMap = new HashMap <>();
    
	public BriefingPilotHelper(Mission mission, Map <Integer, CrewPlanePayloadPairing> assignedCrewMap, Map <Integer, CrewPlanePayloadPairing> unAssignedCrewMap)
	{
        this.mission = mission;
        this.assignedCrewMap = assignedCrewMap;
        this.unAssignedCrewMap = unAssignedCrewMap;
	}
	
	public void initializeFromMission() throws PWCGException
	{	    
        assignedCrewMap.clear();
        unAssignedCrewMap.clear();
        
	    SquadronMembers inSquadron = mission.getCampaign().getPersonnelManager().getSquadronPersonnel(mission.getCampaign().getSquadronId()).getActiveSquadronMembersWithAces();
	    for (SquadronMember playerSquadronMember : inSquadron.getSquadronMembers().values())
	    {
            CrewPlanePayloadPairing crewPlanePayloadPairing = new CrewPlanePayloadPairing(playerSquadronMember);
            unAssignedCrewMap.put(playerSquadronMember.getSerialNumber(), crewPlanePayloadPairing);
	    }
	    
        Flight playerFlight = mission.getMissionFlightBuilder().getPlayerFlight();
	    for (PlaneMCU plane : playerFlight.getPlanes())
	    {
	        assignPilotFromMission(plane);
	    }
	}

    public void modifyPlaneType(Integer pilotSerialNumber, String planeName) 
    {
        try
        {
            CrewPlanePayloadPairing crewPlane = assignedCrewMap.get(pilotSerialNumber);
            if (crewPlane != null)
            {
                PlaneType plane = PWCGContextManager.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(planeName);
                updateForPlaneType(crewPlane, plane);
            }
        }
        catch (Exception e)
        {
            Logger.logException(e);
        }
    }

    public void assignPilotFromBriefing(Integer pilotSerialNumber) throws PWCGException 
    {
        CrewPlanePayloadPairing crewPlane = unAssignedCrewMap.get(pilotSerialNumber);
        Squadron squadron =  mission.getCampaign().determineSquadron();
        List<PlaneType> aircraftTypes = squadron.determineCurrentAircraftList(mission.getCampaign().getDate());
        
        int aircraftIndex = RandomNumberGenerator.getRandom(aircraftTypes.size());
        PlaneType plane = aircraftTypes.get(aircraftIndex);
        updateForPlaneType(crewPlane, plane);

        unAssignedCrewMap.remove(pilotSerialNumber);
        assignedCrewMap.put(pilotSerialNumber, crewPlane);
    }

    public void unassignPilotFromBriefing(Integer pilotSerialNumber) throws PWCGException 
    {
        CrewPlanePayloadPairing crewPlane = assignedCrewMap.get(pilotSerialNumber);
        crewPlane.setPlaneType(CrewPlanePayloadPairing.NO_PLANE_ASSIGNED);
        crewPlane.setPayloadId(CrewPlanePayloadPairing.NO_PAYLOAD_ASSIGNED);
        crewPlane.clearModification();
        
        assignedCrewMap.remove(pilotSerialNumber);
        unAssignedCrewMap.put(pilotSerialNumber, crewPlane);
    }

    private void assignPilotFromMission(PlaneMCU plane) throws PWCGException 
    {
        CrewPlanePayloadPairing crewPlane = unAssignedCrewMap.get(plane.getPilot().getSerialNumber());
        updateForPlaneType(crewPlane, plane);
        
        unAssignedCrewMap.remove(plane.getPilot().getSerialNumber());
        assignedCrewMap.put(plane.getPilot().getSerialNumber(), crewPlane);
    }

    private void updateForPlaneType(CrewPlanePayloadPairing crewPlane, PlaneType planeType) throws PWCGException
    {
        crewPlane.setPlaneType(planeType.getType());
    }
}
