package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.mission.flight.FlightPlanes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.artySpot.ArtillerySpotArtilleryGroup;
import pwcg.mission.flight.artySpot.grid.ArtillerySpotGrid;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuCheckZone;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class MissionPointPlayerArtillerySpotSet implements IMissionPointSet{
	private IFlight artillerySpotFlight;
	private ArtillerySpotGrid artillerySpotGrid;
	private McuWaypoint ingressWaypoint;
	private McuWaypoint egressWaypoint;
	private McuWaypoint artillerySpotWaypoint;
	private McuTimer startSpotTimer = new McuTimer();
	private McuTimer artillerySpawnTimer = new McuTimer();
	private McuTimer mediaTimer = new McuTimer();
	private McuTimer exitCheckZoneTimer = new McuTimer();
	private McuTimer loiterCompleteTimer = new McuTimer();
	private McuTimer egressWPTimer = new McuTimer();
	private McuCheckZone artillerySpotGridCheckZone = new McuCheckZone("Arty Spot Entry CZ");
	private McuCheckZone artillerySpotExitCheckZone = new McuCheckZone("Arty Spot Exit CZ");
    private boolean linkToNextTarget = true;
	private ArtillerySpotArtilleryGroup friendlyArtillery;

	
    public MissionPointPlayerArtillerySpotSet (IFlight flight, McuWaypoint ingressWaypoint, McuWaypoint artillerySpotWaypoint)
    {
		this.artillerySpotFlight = flight;
     	this.ingressWaypoint = ingressWaypoint;
    	this.artillerySpotWaypoint = artillerySpotWaypoint;
     }
    
    public void create() throws PWCGException
    {
        egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(artillerySpotFlight, ingressWaypoint.getPosition());

      	friendlyArtillery = new ArtillerySpotArtilleryGroup(artillerySpotFlight);
    	friendlyArtillery.build();
    	
		artillerySpotGrid = new ArtillerySpotGrid(artillerySpotFlight.getFlightInformation());
		artillerySpotGrid.create(friendlyArtillery, artillerySpotFlight.getTargetDefinition().getPosition().copy());

      	artillerySpotWaypoint.setName("Artillery Spot Waypoint");
    	artillerySpotWaypoint.setDesc("Artillery Spot Waypoint");
    	artillerySpotWaypoint.setPosition(artillerySpotWaypoint.getPosition());

    	artillerySpotGridCheckZone.setName("Artillery Spot Check Zone");
    	artillerySpotGridCheckZone.setDesc("Artillery Spot Check Zon");
    	artillerySpotGridCheckZone.setPosition(artillerySpotWaypoint.getPosition());
    	artillerySpotGridCheckZone.setCloser(McuCheckZone.CLOSER);
    	artillerySpotGridCheckZone.setZone(2000);
    	artillerySpotGridCheckZone.triggerCheckZoneByFlight(artillerySpotFlight);
    	
    	artillerySpotExitCheckZone.setName("Artillery Spot Exit Check Zone");
    	artillerySpotExitCheckZone.setDesc("Artillery Spot Exit Check Zone");
    	artillerySpotExitCheckZone.setPosition(artillerySpotWaypoint.getPosition());
    	artillerySpotExitCheckZone.setCloser(McuCheckZone.FURTHER);
    	artillerySpotExitCheckZone.setZone(15000);
    	artillerySpotExitCheckZone.triggerCheckZoneByFlight(artillerySpotFlight);

    	startSpotTimer.setTime(1);
    	startSpotTimer.setName("Start Spot Timer");
    	startSpotTimer.setDesc("Start Spot Timer");
    	startSpotTimer.setPosition(artillerySpotWaypoint.getPosition());

    	mediaTimer.setTime(1);
    	mediaTimer.setName("Artillery Spot Media Timer");
    	mediaTimer.setDesc("Artillery Spot Media Timer");
    	mediaTimer.setPosition(artillerySpotWaypoint.getPosition());
       	
    	artillerySpawnTimer.setTime(1);
    	artillerySpawnTimer.setName("Artillery Spawn Timer");
    	artillerySpawnTimer.setDesc("Artillery Spawn Timer");
    	artillerySpawnTimer.setPosition(artillerySpotWaypoint.getPosition());
       	
    	exitCheckZoneTimer.setTime(1);
    	exitCheckZoneTimer.setName("Exit Check Zone Timer");
    	exitCheckZoneTimer.setDesc("Exit Check Zone Timer");
    	exitCheckZoneTimer.setPosition(artillerySpotWaypoint.getPosition());
    	
    	egressWPTimer.setTime(1);
    	egressWPTimer.setName("Egress Timer");
    	egressWPTimer.setDesc("Egress Timer");
    	egressWPTimer.setPosition(artillerySpotWaypoint.getPosition());

    	loiterCompleteTimer.setTime(450);
    	loiterCompleteTimer.setName("Loiter Timer");
    	loiterCompleteTimer.setDesc("Loiter Timer");
    	loiterCompleteTimer.setPosition(artillerySpotWaypoint.getPosition());
    }
         
	@Override
	public List<McuWaypoint> getAllWaypoints() 
	{
		List<McuWaypoint> waypoints = new ArrayList<>();
		waypoints.add(ingressWaypoint);
		waypoints.add(artillerySpotWaypoint);
		waypoints.add(egressWaypoint);		
		return waypoints;
	}

	@Override
	public boolean containsWaypoint(long waypointIdToFind) 
	{
		try
		{
			McuWaypoint waypoint = getWaypointById(waypointIdToFind);
			if (waypoint != null)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (PWCGException e)
		{
			return false;			
		}
	}

	@Override
	public McuWaypoint getWaypointById(long waypointId) throws PWCGException 
	{		
		for (McuWaypoint waypoint : getAllWaypoints())
		{
			if (waypoint.getWaypointID() == waypointId)
			{
				return waypoint;
			}
		}
		
		return null;
	}
	
	@Override
	public void write(BufferedWriter writer) throws PWCGException {
	   	friendlyArtillery.write(writer);
	   	
		ingressWaypoint.write(writer);
		artillerySpotWaypoint.write(writer);
		startSpotTimer.write(writer);
		artillerySpawnTimer.write(writer);
		mediaTimer.write(writer);
		exitCheckZoneTimer.write(writer);
		artillerySpotGridCheckZone.write(writer);
    	artillerySpotGrid.write(writer);

		loiterCompleteTimer.write(writer);
		egressWPTimer.write(writer);
		egressWaypoint.write(writer);
		artillerySpotExitCheckZone.write(writer);
	}

	@Override
	public void setLinkToNextTarget(int nextTargetIndex) throws PWCGException {
		egressWaypoint.setTarget(nextTargetIndex);		
	}

	@Override
	public int getEntryPoint() throws PWCGException {
		return ingressWaypoint.getIndex();
	}

	@Override
	public List<MissionPoint> getFlightMissionPoints() throws PWCGException {
		List<MissionPoint> allMissionPoints = new ArrayList<>();

		MissionPoint ingressMissionPoint = new MissionPoint(ingressWaypoint.getPosition(), WaypointAction.WP_ACTION_INGRESS);
		allMissionPoints.add(ingressMissionPoint);

		MissionPoint artillerySpotMissionPoint = new MissionPoint(artillerySpotWaypoint.getPosition(), WaypointAction.WP_ACTION_SPOT);
		allMissionPoints.add(artillerySpotMissionPoint);

		MissionPoint egressMissionPoint = new MissionPoint(egressWaypoint.getPosition(), WaypointAction.WP_ACTION_EGRESS);
		allMissionPoints.add(egressMissionPoint);

		return allMissionPoints;
	}

	@Override
	public List<BaseFlightMcu> getAllFlightPoints() {
		List<BaseFlightMcu> allFlightPoints = new ArrayList<>();
		allFlightPoints.add(ingressWaypoint);
		allFlightPoints.add(artillerySpotWaypoint);
		allFlightPoints.add(egressWaypoint);

		return allFlightPoints;
	}

	@Override
	public void finalizeMissionPointSet(FlightPlanes flightPlanes) throws PWCGException 
	{		
		ingressWaypoint.setTarget(artillerySpotWaypoint.getIndex());
		artillerySpotWaypoint.setTarget(artillerySpotGridCheckZone.getIndex());
		artillerySpotGridCheckZone.setCheckZoneTarget(startSpotTimer.getIndex());
		
        startSpotTimer.setTimerTarget(mediaTimer.getIndex());
        
        mediaTimer.setTimerTarget(artillerySpotGrid.getArtillerySpotMedia().getStartMediaTimer().getIndex());
        mediaTimer.setTimerTarget(exitCheckZoneTimer.getIndex());
        
        exitCheckZoneTimer.setTimerTarget(artillerySpotExitCheckZone.getIndex());
        exitCheckZoneTimer.setTimerTarget(loiterCompleteTimer.getIndex());

		artillerySpotExitCheckZone.setCheckZoneTarget(egressWPTimer.getIndex());
		loiterCompleteTimer.setTimerTarget(egressWPTimer.getIndex());

		egressWPTimer.setTimerTarget(egressWaypoint.getIndex());		

        createObjectAssociations(flightPlanes.getFlightLeader());
	}

    private void createObjectAssociations(PlaneMcu plane)
    {
    	ingressWaypoint.setObject(plane.getLinkTrId());
    	artillerySpotWaypoint.setObject(plane.getLinkTrId());
    	egressWaypoint.setObject(plane.getLinkTrId());
    	artillerySpotGridCheckZone.setObject(plane.getLinkTrId());
    	artillerySpotExitCheckZone.setObject(plane.getLinkTrId());
    }

    @Override
    public void disableLinkToNextTarget()
    {
        linkToNextTarget = false;        
    }

    @Override
    public boolean isLinkToNextTarget()
    {
        return linkToNextTarget;
    }

	@Override
	public MissionPointSetType getMissionPointSetType() {
		return MissionPointSetType.MISSION_POINT_SET_PLAYER_RECON;
	}

	@Override
	public void updateWaypointFromBriefing(BriefingMapPoint waypoint) throws PWCGException {
		if (ingressWaypoint.getWaypointID() == waypoint.getWaypointID())
		{
			ingressWaypoint.updateFromBriefing(waypoint);
		}
		else if (egressWaypoint.getWaypointID() == waypoint.getWaypointID())
		{
			egressWaypoint.updateFromBriefing(waypoint);
		}
	}

	@Override
	public void removeUnwantedWaypoints(List<BriefingMapPoint> waypointsInBriefing) throws PWCGException {
	}

	@Override
	public long addWaypointFromBriefing(BriefingMapPoint newWaypoint, long waypointIdAfter) throws PWCGException {
		return 0;
	}

	@Override
	public void addWaypointAfterWaypoint(McuWaypoint newWaypoint, long waypointIdAfter) throws PWCGException {
	}

	@Override
	public void addWaypointBeforeWaypoint(McuWaypoint newWaypoint, long waypointIdBefore) throws PWCGException {
	}

	public McuWaypoint getIngressWaypoint() {
		return ingressWaypoint;
	}

	public void setIngressWaypoint(McuWaypoint ingressWaypoint) {
		this.ingressWaypoint = ingressWaypoint;
	}

	public McuWaypoint getEgressWaypoint() {
		return egressWaypoint;
	}

	public void setEgressWaypoint(McuWaypoint egressWaypoint) {
		this.egressWaypoint = egressWaypoint;
	}

	public void setLinkToNextTarget(boolean linkToNextTarget) {
		this.linkToNextTarget = linkToNextTarget;
	}

}
