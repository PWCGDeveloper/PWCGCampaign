package pwcg.mission.flight.waypoint.patterns;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.strategicintercept.StrategicInterceptWaypointFactory;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory.AirStartPattern;
import pwcg.mission.flight.waypoint.begin.IngressWaypointFactory;
import pwcg.mission.flight.waypoint.begin.IngressWaypointFactory.IngressWaypointPattern;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetFactory;
import pwcg.mission.mcu.McuWaypoint;

public class InterceptAtTargetPattern
{
    public static List<IMissionPointSet> generateInterceptSegments(IFlight flight) throws PWCGException
    {
        if (flight.getTargetDefinition().getOpposingFlight() == null)
        {
            throw new PWCGException("No opposing flight for intercept pattern");
        }
        
        List<IMissionPointSet> interceptPatternWaypoints = new ArrayList<>();
        McuWaypoint ingressWaypoint = IngressWaypointFactory.createIngressWaypoint(IngressWaypointPattern.INGRESS_TOWARDS_TARGET, flight);

        IMissionPointSet flightActivate = MissionPointSetFactory.createFlightActivate(flight);
        interceptPatternWaypoints.add(flightActivate);

        IMissionPointSet flightBegin = MissionPointSetFactory.createFlightBegin(flight, flightActivate, AirStartPattern.AIR_START_NEAR_WAYPOINT, ingressWaypoint);
        interceptPatternWaypoints.add(flightBegin);

        StrategicInterceptWaypointFactory missionWaypointFactory = new StrategicInterceptWaypointFactory(flight, flight.getTargetDefinition().getOpposingFlight());
        IMissionPointSet missionWaypoints = missionWaypointFactory.createWaypoints(ingressWaypoint);
        interceptPatternWaypoints.add(missionWaypoints);
        
        IMissionPointSet flightEnd = MissionPointSetFactory.createFlightEndAtHomeField(flight);
        interceptPatternWaypoints.add(flightEnd);

        return interceptPatternWaypoints;        
    }

}
