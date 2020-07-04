package pwcg.gui.helper;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneSorter;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberSorter;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.BriefParametersContextBuilder;
import pwcg.gui.rofmap.brief.BriefingFlightParameters;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;

public class BriefingMissionFlight
{
    private BriefingFlightParameters briefingFlightParameters;
    private BriefingAssignmentData briefingAssignmentData;
    private Mission mission;
    private IFlight flight;

    public BriefingMissionFlight(Mission mission, IFlight flight)
    {
        this.mission = mission;
        this.flight = flight;
        briefingFlightParameters = new BriefingFlightParameters(flight);
        briefingAssignmentData = new BriefingAssignmentData();
    }
    
    public void initializeFromMission(Squadron squadron) throws PWCGException
    {
        BriefingDataInitializer pilotHelper = new BriefingDataInitializer(mission);
        briefingAssignmentData = pilotHelper.initializeFromMission(squadron);

        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.initializePayloadsFromMission();
    }

    public void changePlane(Integer pilotSerialNumber, Integer planeSerialNumber) throws PWCGException
    {
        briefingAssignmentData.changePlane(pilotSerialNumber, planeSerialNumber);

        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.setPayloadForChangedPlane(pilotSerialNumber);
    }


    public void assignPilotFromBriefing(Integer pilotSerialNumber) throws PWCGException
    {
        EquippedPlane planeForPilot = this.getSortedUnassignedPlanes().get(0);
        assignPilotAndPlaneFromBriefing(pilotSerialNumber, planeForPilot.getSerialNumber());
    }

    public void assignPilotAndPlaneFromBriefing(Integer pilotSerialNumber, Integer planeSerialNumber) throws PWCGException
    {
        briefingAssignmentData.assignPilot(pilotSerialNumber, planeSerialNumber);
        
        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.setPayloadForAddedPlane(pilotSerialNumber);
    }    

    public void unassignPilotFromBriefing(int pilotSerialNumber) throws PWCGException
    {
        briefingAssignmentData.unassignPilot(pilotSerialNumber);
    }

    public void modifyPayload(Integer pilotSerialNumber, int newPayload)
    {
        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.modifyPayload(pilotSerialNumber, newPayload);
    }

    public void loadMissionParams(IFlight playerFlight) throws PWCGException
    {     
    	BriefParametersContextBuilder briefParametersContextBuilder = new BriefParametersContextBuilder(playerFlight);
        briefingFlightParameters = briefParametersContextBuilder.buildBriefParametersContext();
    }

    public List<SquadronMember> getSortedAssigned() throws PWCGException 
    {       
        return SquadronMemberSorter.sortSquadronMembers(mission.getCampaign(), briefingAssignmentData.getAssignedPilots());
    }

    public List<SquadronMember> getSortedUnassignedPilots() throws PWCGException 
    {       
        return SquadronMemberSorter.sortSquadronMembers(mission.getCampaign(), briefingAssignmentData.getUnassignedPilots());
    }

    public List<EquippedPlane> getSortedAssignedPlanes() throws PWCGException 
    {       
        return PlaneSorter.sortEquippedPlanesByGoodness(new ArrayList<EquippedPlane>(briefingAssignmentData.getAssignedPlanes().values()));
    }

    public List<EquippedPlane> getSortedUnassignedPlanes() throws PWCGException 
    {       
        return PlaneSorter.sortEquippedPlanesByGoodness(new ArrayList<EquippedPlane>(briefingAssignmentData.getUnassignedPlanes().values()));
    }

    public CrewPlanePayloadPairing getPairingByPilot(Integer pilotSerialNumber) throws PWCGException 
    {       
        return briefingAssignmentData.findAssignedCrewPairingByPilot(pilotSerialNumber);
    }

    public Mission getMission()
    {
        return mission;
    }

    public void setMission(Mission mission)
    {
        this.mission = mission;
    }

    public BriefingFlightParameters getBriefingFlightParameters()
    {
        return briefingFlightParameters;
    }

    public BriefingAssignmentData getBriefingAssignmentData()
    {
        return briefingAssignmentData;
    }

    public IFlight getFlight()
    {
        return flight;
    }

    public List<CrewPlanePayloadPairing> getCrews()
    {
        return briefingAssignmentData.getCrews();
    }

    public void movePilotUp(int pilotSerialNumber)
    {
        briefingAssignmentData.movePilotUp(pilotSerialNumber);        
    }

    public void movePilotDown(int pilotSerialNumber)
    {
        briefingAssignmentData.movePilotDown(pilotSerialNumber);        
    }
}
