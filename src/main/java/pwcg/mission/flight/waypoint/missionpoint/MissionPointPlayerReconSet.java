package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.mission.flight.FlightPlanes;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.recon.PlayerReconWaypoint;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuEvent;
import pwcg.mission.mcu.McuMedia;
import pwcg.mission.mcu.McuWaypoint;

public class MissionPointPlayerReconSet implements IMissionPointSet{
	private McuWaypoint ingressWaypoint;
	private McuWaypoint egressWaypoint;
    private List<PlayerReconWaypoint> reconPlayerWaypoints = new ArrayList<>();
    private boolean linkToNextTarget = true;

    public MissionPointPlayerReconSet (McuWaypoint ingressWaypoint, McuWaypoint egressWaypoint)
    {
    	this.ingressWaypoint = ingressWaypoint;
    	this.egressWaypoint = egressWaypoint;
    }
    
    public void addReconPlayerWaypoint(PlayerReconWaypoint reconPlayerWaypoint)
    {
    	reconPlayerWaypoints.add(reconPlayerWaypoint);
    }
    
	@Override
	public List<McuWaypoint> getAllWaypoints() 
	{
		List<McuWaypoint> waypoints = new ArrayList<>();
		waypoints.add(ingressWaypoint);
		for (PlayerReconWaypoint reconPlayerWaypoint : reconPlayerWaypoints)
		{
			waypoints.add(reconPlayerWaypoint.getWaypoint());
		}
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
		if (ingressWaypoint.getWaypointID() == waypointId)
		{
			return ingressWaypoint;
		}
		
		for (PlayerReconWaypoint reconPlayerWaypoint : reconPlayerWaypoints)
		{
			if (reconPlayerWaypoint.getWaypoint().getWaypointID() == waypointId)
			{
				return reconPlayerWaypoint.getWaypoint();
			}
		}
		
		if (egressWaypoint.getWaypointID() == waypointId)
		{
			return egressWaypoint;
		}
		
		return null;
	}

	@Override
	public void write(BufferedWriter writer) throws PWCGException {
		ingressWaypoint.write(writer);
		for (PlayerReconWaypoint reconPlayerWaypoint : reconPlayerWaypoints)
		{
			reconPlayerWaypoint.write(writer);
		}
		egressWaypoint.write(writer);
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

		for (PlayerReconWaypoint reconPlayerWaypoint : reconPlayerWaypoints)
		{
			MissionPoint missionPoint = new MissionPoint(reconPlayerWaypoint.getPhotoMedia().getPosition(), WaypointAction.WP_ACTION_RECON);
			allMissionPoints.add(missionPoint);
		}

		MissionPoint egressMissionPoint = new MissionPoint(egressWaypoint.getPosition(), WaypointAction.WP_ACTION_EGRESS);
		allMissionPoints.add(egressMissionPoint);

		return allMissionPoints;
	}

	@Override
	public List<BaseFlightMcu> getAllFlightPoints() {
		List<BaseFlightMcu> allFlightPoints = new ArrayList<>();
		allFlightPoints.add(ingressWaypoint);
		for (PlayerReconWaypoint reconPlayerWaypoint : reconPlayerWaypoints)
		{
			McuMedia waypoint = reconPlayerWaypoint.getPhotoMedia();
			allFlightPoints.add(waypoint);		
		}

		allFlightPoints.add(egressWaypoint);

		return allFlightPoints;
	}

	@Override
	public void finalizeMissionPointSet(FlightPlanes flightPlanes) throws PWCGException {
		
		PlayerReconWaypoint firstReconPlayerWaypoint = reconPlayerWaypoints.get(0);
		ingressWaypoint.setTarget(firstReconPlayerWaypoint.getEntryTimer().getIndex());


		PlayerReconWaypoint previousReconPlayerWaypoint = null;
		for (PlayerReconWaypoint reconPlayerWaypoint : reconPlayerWaypoints)
		{
			reconPlayerWaypoint.getEntryTimer().setTimerTarget(reconPlayerWaypoint.getWaypoint().getIndex());
			
			reconPlayerWaypoint.getWaypoint().setTarget(reconPlayerWaypoint.getPhotoMedia().getIndex());
			
			McuEvent photoEVent = new McuEvent(McuEvent.ONPHOTO, reconPlayerWaypoint.getExitTimer().getIndex());
			reconPlayerWaypoint.getPhotoMedia().addEvent(photoEVent);
			
			if (previousReconPlayerWaypoint != null)
			{
				previousReconPlayerWaypoint.getExitTimer().setTimerTarget(reconPlayerWaypoint.getEntryTimer().getIndex());
			}
			
			previousReconPlayerWaypoint = reconPlayerWaypoint;
		}
		
		PlayerReconWaypoint lastReconPlayerWaypoint = reconPlayerWaypoints.get(reconPlayerWaypoints.size()-1);
		lastReconPlayerWaypoint.getExitTimer().setTimerTarget(egressWaypoint.getIndex());
		
        createObjectAssociations(flightPlanes.getFlightLeader());
	}

    private void createObjectAssociations(PlaneMcu plane)
    {
    	ingressWaypoint.setObject(plane.getLinkTrId());
		for (PlayerReconWaypoint reconPlayerWaypoint : reconPlayerWaypoints)
		{
			reconPlayerWaypoint.getWaypoint().setObject(plane.getLinkTrId());
		}
    	egressWaypoint.setObject(plane.getLinkTrId());
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

	public List<PlayerReconWaypoint> getReconPlayerWaypoints() {
		return reconPlayerWaypoints;
	}

	public void setReconPlayerWaypoints(List<PlayerReconWaypoint> reconPlayerWaypoints) {
		this.reconPlayerWaypoints = reconPlayerWaypoints;
	}

	public void setLinkToNextTarget(boolean linkToNextTarget) {
		this.linkToNextTarget = linkToNextTarget;
	}

}
