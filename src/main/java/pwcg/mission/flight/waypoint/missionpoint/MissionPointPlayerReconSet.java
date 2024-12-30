package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.mission.flight.FlightPlanes;
import pwcg.mission.flight.recon.ReconPlayerWaypoint;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuEvent;
import pwcg.mission.mcu.McuWaypoint;

public class MissionPointPlayerReconSet implements IMissionPointSet{
	private McuWaypoint ingressWaypoint;
	private McuWaypoint egressWaypoint;
    private List<ReconPlayerWaypoint> reconPlayerWaypoints = new ArrayList<>();
    private boolean linkToNextTarget = true;

    public MissionPointPlayerReconSet (McuWaypoint ingressWaypoint, McuWaypoint egressWaypoint)
    {
    	this.ingressWaypoint = ingressWaypoint;
    	this.egressWaypoint = egressWaypoint;
    }
    
    public void addReconPlayerWaypoint(ReconPlayerWaypoint reconPlayerWaypoint)
    {
    	reconPlayerWaypoints.add(reconPlayerWaypoint);
    }
    
	@Override
	public List<McuWaypoint> getAllWaypoints() 
	{
		List<McuWaypoint> waypoints = new ArrayList<>();
		waypoints.add(ingressWaypoint);		
		for (ReconPlayerWaypoint reconPlayerWaypoint : reconPlayerWaypoints)
		{
			McuWaypoint waypoint = reconPlayerWaypoint.getWaypoint();
			waypoints.add(waypoint);		
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
		if (ingressWaypoint.getIndex() == waypointId)
		{
			return ingressWaypoint;
		}
		
		if (egressWaypoint.getIndex() == waypointId)
		{
			return egressWaypoint;
		}
		
		for (ReconPlayerWaypoint reconPlayerWaypoint : reconPlayerWaypoints)
		{
			McuWaypoint waypoint = reconPlayerWaypoint.getWaypoint();
			if (waypoint.getIndex() == waypointId)
			{
				return waypoint;
			}
		}
		return null;
	}

	@Override
	public void write(BufferedWriter writer) throws PWCGException {
		ingressWaypoint.write(writer);
		for (ReconPlayerWaypoint reconPlayerWaypoint : reconPlayerWaypoints)
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
		return reconPlayerWaypoints.getFirst().getEntryTimer().getIndex();
	}

	@Override
	public List<MissionPoint> getFlightMissionPoints() throws PWCGException {
		List<MissionPoint> allMissionPoints = new ArrayList<>();

		MissionPoint ingressMissionPoint = new MissionPoint(ingressWaypoint.getPosition(), WaypointAction.WP_ACTION_INGRESS);
		allMissionPoints.add(ingressMissionPoint);

		for (ReconPlayerWaypoint reconPlayerWaypoint : reconPlayerWaypoints)
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
		for (ReconPlayerWaypoint reconPlayerWaypoint : reconPlayerWaypoints)
		{
			allFlightPoints.add(reconPlayerWaypoint.getWaypoint());
		}
		allFlightPoints.add(egressWaypoint);

		return allFlightPoints;
	}

	@Override
	public void finalizeMissionPointSet(FlightPlanes flightPlanes) throws PWCGException {
		
		ReconPlayerWaypoint firstReconPlayerWaypoint = reconPlayerWaypoints.getFirst();
		ingressWaypoint.setTarget(firstReconPlayerWaypoint.getEntryTimer().getIndex());


		ReconPlayerWaypoint previousReconPlayerWaypoint = null;
		for (ReconPlayerWaypoint reconPlayerWaypoint : reconPlayerWaypoints)
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
		
		ReconPlayerWaypoint lastReconPlayerWaypoint = reconPlayerWaypoints.getLast();
		lastReconPlayerWaypoint.getExitTimer().setTimerTarget(egressWaypoint.getIndex());
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
		throw new PWCGException("No ability to update a recon waypoint");
	}

	@Override
	public void removeUnwantedWaypoints(List<BriefingMapPoint> waypointsInBriefing) throws PWCGException {
		throw new PWCGException("No ability to remove a recon waypoint");
	}

	@Override
	public long addWaypointFromBriefing(BriefingMapPoint newWaypoint, long waypointIdAfter) throws PWCGException {
		throw new PWCGException("No ability to add a recon waypoint");
	}

	@Override
	public void addWaypointAfterWaypoint(McuWaypoint newWaypoint, long waypointIdAfter) throws PWCGException {
		throw new PWCGException("No ability to add a recon waypoint");
	}

	@Override
	public void addWaypointBeforeWaypoint(McuWaypoint newWaypoint, long waypointIdBefore) throws PWCGException {
		throw new PWCGException("No ability to add a recon waypoint");
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

	public List<ReconPlayerWaypoint> getReconPlayerWaypoints() {
		return reconPlayerWaypoints;
	}

	public void setReconPlayerWaypoints(List<ReconPlayerWaypoint> reconPlayerWaypoints) {
		this.reconPlayerWaypoints = reconPlayerWaypoints;
	}

	public void setLinkToNextTarget(boolean linkToNextTarget) {
		this.linkToNextTarget = linkToNextTarget;
	}

}
