package pwcg.mission.flight.waypoint.begin;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.McuWaypoint;

public class IngressWaypointFactory
{
    public static McuWaypoint createIngressWaypoint(IFlight flight) throws PWCGException
    {
        IIngressWaypoint ingressWaypointGenerator = new IngressWaypointAtTarget(flight);
        McuWaypoint ingressWaypoint = ingressWaypointGenerator.createIngressWaypoint();
        return ingressWaypoint;
    }
}
