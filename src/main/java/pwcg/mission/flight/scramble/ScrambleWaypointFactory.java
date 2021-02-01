package pwcg.mission.flight.scramble;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.flight.waypoint.patterns.CircleWaypointPattern;
import pwcg.mission.mcu.McuWaypoint;

public class ScrambleWaypointFactory
{
    private IFlight flight;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();
    
    public ScrambleWaypointFactory(IFlight flight)
    {
        this.flight = flight;
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet.addWaypoint(ingressWaypoint);
        
        List<McuWaypoint> waypoints = createTargetWaypoints(ingressWaypoint);
        missionPointSet.addWaypoints(waypoints);

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        missionPointSet.addWaypoint(egressWaypoint);

        return missionPointSet;
    }

    private List<McuWaypoint> createTargetWaypoints(McuWaypoint ingressWaypoint) throws PWCGException  
	{
        List<McuWaypoint> waypoints = new ArrayList<>();

		List<McuWaypoint> scrambleTargetWPs = createTargetScrambleWaypoint(ingressWaypoint);
		waypoints.addAll(scrambleTargetWPs);

        return waypoints;
	}

    private List<McuWaypoint> createTargetScrambleWaypoint(McuWaypoint ingressWaypoint) throws PWCGException
    {
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int loopLegDistance = productSpecificConfiguration.getInterceptInnerLoopDistance();
        
        CircleWaypointPattern circleWaypointPattern = new CircleWaypointPattern(
                flight.getCampaign(), 
                flight, 
                WaypointType.PATROL_WAYPOINT, 
                WaypointAction.WP_ACTION_PATROL,
                3000,
                6);
        
        List<McuWaypoint> waypoints = circleWaypointPattern.generateCircleWPs(
                flight.getFlightHomePosition(), 
                ingressWaypoint.getOrientation().getyOri(), 
                ingressWaypoint.getPosition().getYPos(),
                flight.getFlightInformation().getAltitude(), 
                loopLegDistance);
        
        return waypoints;
    }
}
