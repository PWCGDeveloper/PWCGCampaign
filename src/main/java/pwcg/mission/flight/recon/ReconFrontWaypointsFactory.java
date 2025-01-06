package pwcg.mission.flight.recon;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointPlayerReconSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.mcu.McuWaypoint;

public class ReconFrontWaypointsFactory
{
    private IFlight flight;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();

    public ReconFrontWaypointsFactory(IFlight flight) throws PWCGException
    {
        this.flight = flight;
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet.addWaypoint(ingressWaypoint);
        
        ReconFrontWaypointsBuilder reconFrontWaypointsBuilder = new ReconFrontWaypointsBuilder(flight);
        List<McuWaypoint> waypoints = reconFrontWaypointsBuilder.createTargetWaypoints(ingressWaypoint.getPosition());
        missionPointSet.addWaypoints(waypoints);

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        missionPointSet.addWaypoint(egressWaypoint);

        return missionPointSet;
    }


	public IMissionPointSet createPlayerWaypoints(McuWaypoint ingressWaypoint, McuWaypoint egressWaypoint) throws PWCGException {
		MissionPointPlayerReconSet missionPointSet = new MissionPointPlayerReconSet(ingressWaypoint, egressWaypoint);

		ReconFrontWaypointsBuilder waypointBuilder = new ReconFrontWaypointsBuilder(flight);
        List<McuWaypoint> waypoints = waypointBuilder.createTargetWaypoints(ingressWaypoint.getPosition());
        for (McuWaypoint waypoint : waypoints)
        {
        	PlayerReconWaypoint reconPlayerWaypoint = PlayerReconWaypointBuilder.buildReconPlayerWaypoint(flight.getCampaign(), waypoint);
        	missionPointSet.addReconPlayerWaypoint(reconPlayerWaypoint);
        }		
		
		return missionPointSet;
	}
}
