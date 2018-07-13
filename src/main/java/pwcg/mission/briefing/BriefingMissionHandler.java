package pwcg.mission.briefing;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneSorter;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberSorter;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.BriefParametersContextBuilder;
import pwcg.gui.rofmap.brief.BriefingCrewPlanePayloadSorter;
import pwcg.gui.rofmap.brief.BriefingFlightParameters;
import pwcg.mission.Mission;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;

public class BriefingMissionHandler
{
    private BriefingFlightParameters briefParametersContext = new BriefingFlightParameters();
    private BriefingAssignmentData briefingAssignmentData = new BriefingAssignmentData();
    private Mission mission = null;

    public BriefingMissionHandler(Mission mission)
    {
        this.mission = mission;
    }
    
    public void initializeFromMission() throws PWCGException
    {
        BriefingDataInitializer pilotHelper = new BriefingDataInitializer(mission, briefingAssignmentData);
        pilotHelper.initializeFromMission();

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

    public void loadMissionParams() throws PWCGException
    {     
    	BriefParametersContextBuilder briefParametersContextBuilder = new BriefParametersContextBuilder(mission);
        briefParametersContext = briefParametersContextBuilder.buildBriefParametersContext();
    }

    public void pushEditsToMission() throws PWCGException
    {
        PlayerFlightEditor planeGeneratorPlayer = new PlayerFlightEditor(mission.getCampaign(),mission.getMissionFlightBuilder().getPlayerFlight());
        planeGeneratorPlayer.updatePlayerPlanes(getCrewsSorted());
    }

    public void finalizeMission() throws PWCGException 
    {
        if (!mission.isFinalized())
        {
            updateMissionBriefingParameters();
            mission.finalizeMission();

            Campaign campaign  = PWCGContextManager.getInstance().getCampaign();
            campaign.setCurrentMission(mission);
        }
    }

    public void updateMissionBriefingParameters() 
    {
        if (!mission.isFinalized())
        {
            mission.getMissionFlightBuilder().getPlayerFlight().updateWaypoints(briefParametersContext.getWaypointsInBriefing());
            mission.getMissionFlightBuilder().getPlayerFlight().setFuel(briefParametersContext.getSelectedFuel());
            
            PWCGContextManager.getInstance().getCurrentMap().getMissionOptions().getMissionTime().setMissionTime(briefParametersContext.getSelectedTime());
        }
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

    public List<CrewPlanePayloadPairing> getCrewsSorted() throws PWCGException
    {
        BriefingCrewPlanePayloadSorter crewSorter = new BriefingCrewPlanePayloadSorter(mission, briefingAssignmentData.getAssignedCrewPlanes());
        return crewSorter.getAssignedCrewsSorted();
    }

    public CrewPlanePayloadPairing getPairingByPilot(Integer pilotSerialNumber) throws PWCGException 
    {       
        return briefingAssignmentData.getAssignedCrewPlanes().get(pilotSerialNumber);
    }

    public Mission getMission()
    {
        return mission;
    }

    public void setMission(Mission mission)
    {
        this.mission = mission;
    }

    public BriefingFlightParameters getBriefParametersContext()
    {
        return briefParametersContext;
    }

    public BriefingAssignmentData getBriefingAssignmentData()
    {
        return briefingAssignmentData;
    }    
}
