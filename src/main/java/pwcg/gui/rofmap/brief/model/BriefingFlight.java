package pwcg.gui.rofmap.brief.model;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneSorter;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberSorter;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.BriefingDataInitializer;
import pwcg.gui.rofmap.brief.BriefingPayloadHelper;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;

public class BriefingFlight
{
    private int squadronId;
    private BriefingFlightParameters briefingFlightParameters;
    private BriefingPilotAssignmentData briefingAssignmentData;
    private double selectedFuel = 1.0;
    private Mission mission;

    public BriefingFlight(Mission mission, BriefingFlightParameters briefingFlightParameters, int squadronId)
    {
        this.mission = mission;
        this.squadronId = squadronId;
        this.briefingFlightParameters = briefingFlightParameters;
        briefingAssignmentData = new BriefingPilotAssignmentData();
    }
    
    public void initializeFromMission(Squadron squadron) throws PWCGException
    {
        BriefingDataInitializer pilotHelper = new BriefingDataInitializer(mission);
        briefingAssignmentData = pilotHelper.initializeFromMission(squadron);

        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.initializePayloadsFromMission();
        
        initializeFuel();
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

    public List<SquadronMember> getSortedUnassignedPilots() throws PWCGException 
    {       
        return SquadronMemberSorter.sortSquadronMembers(mission.getCampaign(), briefingAssignmentData.getUnassignedPilots());
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

    public BriefingPilotAssignmentData getBriefingAssignmentData()
    {
        return briefingAssignmentData;
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

    public double getSelectedFuel()
    {
        return selectedFuel;
    }

    public void setSelectedFuel(double selectedFuel)
    {
        this.selectedFuel = selectedFuel;
    }

    public int getSquadronId()
    {
        return squadronId;
    }
    

    private void initializeFuel()
    {
        IFlight flight = mission.getMissionFlightBuilder().getPlayerFlightForSquadron(squadronId);
        this.selectedFuel = flight.getFlightPlanes().getFlightLeader().getFuel();
    }
}
