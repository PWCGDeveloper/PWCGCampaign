package pwcg.mission.flight.recon;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointPlayerReconSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.mcu.McuWaypoint;

public class ReconAirfieldWaypointsFactory
{
    private IFlight flight;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();

    public ReconAirfieldWaypointsFactory(IFlight flight) throws PWCGException
    {
        this.flight = flight;
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet.addWaypoint(ingressWaypoint);

        ReconAirfieldWaypointsBuilder reconAirfieldWaypointsBuilder = new ReconAirfieldWaypointsBuilder(flight);
        List<McuWaypoint> waypoints = reconAirfieldWaypointsBuilder.createTargetWaypoints(ingressWaypoint.getPosition());
        missionPointSet.addWaypoints(waypoints);

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        missionPointSet.addWaypoint(egressWaypoint);

        return missionPointSet;
    }

	public IMissionPointSet createPlayerWaypoints(McuWaypoint ingressWaypoint, McuWaypoint egressWaypoint) throws PWCGException {
		MissionPointPlayerReconSet missionPointSet = new MissionPointPlayerReconSet(egressWaypoint, egressWaypoint);

		ReconAirfieldWaypointsBuilder waypointBuilder = new ReconAirfieldWaypointsBuilder(flight);
        List<McuWaypoint> waypoints = waypointBuilder.createTargetWaypoints(ingressWaypoint.getPosition());
        for (McuWaypoint waypoint : waypoints)
        {
        	PlayerReconWaypoint reconPlayerWaypoint = PlayerReconWaypointBuilder.buildReconPlayerWaypoint(flight.getCampaign(), waypoint);
        	missionPointSet.addReconPlayerWaypoint(reconPlayerWaypoint);
        }		
		
		return missionPointSet;
	}

}
