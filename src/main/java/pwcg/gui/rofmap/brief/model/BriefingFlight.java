package pwcg.gui.rofmap.brief.model;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberSorter;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneSorter;
import pwcg.campaign.squadron.Company;
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
    private BriefingCrewMemberAssignmentData briefingAssignmentData;
    private double selectedFuel = 1.0;
    private Mission mission;

    public BriefingFlight(Mission mission, BriefingFlightParameters briefingFlightParameters, int squadronId)
    {
        this.mission = mission;
        this.squadronId = squadronId;
        this.briefingFlightParameters = briefingFlightParameters;
        briefingAssignmentData = new BriefingCrewMemberAssignmentData();
    }
    
    public void initializeFromMission(Company squadron) throws PWCGException
    {
        BriefingDataInitializer crewMemberHelper = new BriefingDataInitializer(mission);
        briefingAssignmentData = crewMemberHelper.initializeFromMission(squadron);

        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.initializePayloadsFromMission();
        
        initializeFuel();
    }

    public void changePlane(Integer crewMemberSerialNumber, Integer planeSerialNumber) throws PWCGException
    {
        briefingAssignmentData.changePlane(crewMemberSerialNumber, planeSerialNumber);

        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.setPayloadForChangedPlane(crewMemberSerialNumber);
    }


    public void assignCrewMemberFromBriefing(Integer crewMemberSerialNumber) throws PWCGException
    {
        EquippedPlane planeForCrewMember = this.getSortedUnassignedPlanes().get(0);
        assignCrewMemberAndPlaneFromBriefing(crewMemberSerialNumber, planeForCrewMember.getSerialNumber());
    }

    public void assignCrewMemberAndPlaneFromBriefing(Integer crewMemberSerialNumber, Integer planeSerialNumber) throws PWCGException
    {
        briefingAssignmentData.assignCrewMember(crewMemberSerialNumber, planeSerialNumber);
        
        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.setPayloadForAddedPlane(crewMemberSerialNumber);
    }    

    public void unassignCrewMemberFromBriefing(int crewMemberSerialNumber) throws PWCGException
    {
        briefingAssignmentData.unassignCrewMember(crewMemberSerialNumber);
    }

    public void modifyPayload(Integer crewMemberSerialNumber, int newPayload)
    {
        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.modifyPayload(crewMemberSerialNumber, newPayload);
    }

    public List<CrewMember> getSortedUnassignedCrewMembers() throws PWCGException 
    {       
        return CrewMemberSorter.sortCrewMembers(mission.getCampaign(), briefingAssignmentData.getUnassignedCrewMembers());
    }

    public List<EquippedPlane> getSortedUnassignedPlanes() throws PWCGException 
    {       
        return PlaneSorter.sortEquippedPlanesByGoodness(new ArrayList<EquippedPlane>(briefingAssignmentData.getUnassignedPlanes().values()));
    }

    public CrewPlanePayloadPairing getPairingByCrewMember(Integer crewMemberSerialNumber) throws PWCGException 
    {       
        return briefingAssignmentData.findAssignedCrewPairingByCrewMember(crewMemberSerialNumber);
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

    public BriefingCrewMemberAssignmentData getBriefingAssignmentData()
    {
        return briefingAssignmentData;
    }

    public List<CrewPlanePayloadPairing> getCrews()
    {
        return briefingAssignmentData.getCrews();
    }

    public void moveCrewMemberUp(int crewMemberSerialNumber)
    {
        briefingAssignmentData.moveCrewMemberUp(crewMemberSerialNumber);        
    }

    public void moveCrewMemberDown(int crewMemberSerialNumber)
    {
        briefingAssignmentData.moveCrewMemberDown(crewMemberSerialNumber);        
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
        IFlight flight = mission.getFlights().getPlayerFlightForSquadron(squadronId);
        this.selectedFuel = flight.getFlightPlanes().getFlightLeader().getFuel();
    }
}
