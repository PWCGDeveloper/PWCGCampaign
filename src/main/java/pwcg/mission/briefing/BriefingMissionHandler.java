package pwcg.mission.briefing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.BriefParametersContextBuilder;
import pwcg.gui.rofmap.brief.BriefingCrewPlanePayloadSorter;
import pwcg.gui.rofmap.brief.BriefingFlightParameters;
import pwcg.gui.rofmap.brief.PlayerFlightEditor;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;
import pwcg.mission.flight.plane.Plane;

public class BriefingMissionHandler
{
    private BriefingFlightParameters briefParametersContext = new BriefingFlightParameters();
    private Map <Integer, CrewPlanePayloadPairing> assignedCrewMap = new HashMap <>();
    private Map <Integer, CrewPlanePayloadPairing> unAssignedCrewMap = new HashMap <>();
    private Mission mission = null;

    public BriefingMissionHandler(Mission mission)
    {
        this.mission = mission;
    }
    
    public void initializeFromMission() throws PWCGException
    {
        BriefingPilotHelper pilotHelper = new BriefingPilotHelper(mission, assignedCrewMap, unAssignedCrewMap);
        pilotHelper.initializeFromMission();

        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, assignedCrewMap);
        payloadHelper.initializePayloadsFromMission();
    }

    public void modifyPlaneType(Integer pilotSerialNumber, String newPlane) throws PWCGException
    {
        BriefingPilotHelper pilotHelper = new BriefingPilotHelper(mission, assignedCrewMap, unAssignedCrewMap);
        pilotHelper.modifyPlaneType(pilotSerialNumber, newPlane);
        
        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, assignedCrewMap);
        payloadHelper.setPayloadForChangedPlane(pilotSerialNumber);
    }

    public void assignPilotFromBriefing(Integer pilotSerialNumber) throws PWCGException
    {
        BriefingPilotHelper pilotHelper = new BriefingPilotHelper(mission, assignedCrewMap, unAssignedCrewMap);
        pilotHelper.assignPilotFromBriefing(pilotSerialNumber);
        
        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, assignedCrewMap);
        payloadHelper.setPayloadForAddedPlane(pilotSerialNumber);
    }    

    public void unassignPilotFromBriefing(int pilotSerialNumber) throws PWCGException
    {
        BriefingPilotHelper pilotHelper = new BriefingPilotHelper(mission, assignedCrewMap, unAssignedCrewMap);
        pilotHelper.unassignPilotFromBriefing(pilotSerialNumber);
    }

    public void modifyPayload(Integer pilotSerialNumber, int newPayload)
    {
        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, assignedCrewMap);
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
        List<Plane> updatedPlaneSet =  planeGeneratorPlayer.updatePlayerPlanes(getCrewsSorted(assignedCrewMap));
        
        Flight playerFlight = mission.getMissionFlightBuilder().getPlayerFlight();
        playerFlight.setPlanes(updatedPlaneSet);
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

    public List<CrewPlanePayloadPairing> getSortedAssigned() throws PWCGException 
    {       
        return getCrewsSorted(assignedCrewMap);
    }

    public List<CrewPlanePayloadPairing> getSortedUnassigned() throws PWCGException 
    {       
        return getCrewsSorted(unAssignedCrewMap);
    }

    public List<CrewPlanePayloadPairing> getCrewsSorted(Map <Integer, CrewPlanePayloadPairing> crewMap) throws PWCGException
    {
        BriefingCrewPlanePayloadSorter crewSorter = new BriefingCrewPlanePayloadSorter(mission, crewMap);
        return crewSorter.getAssignedCrewsSorted();
    }

    public CrewPlanePayloadPairing getPairingByPilot(Integer pilotSerialNumber) throws PWCGException 
    {       
        return assignedCrewMap.get(pilotSerialNumber);
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
}
