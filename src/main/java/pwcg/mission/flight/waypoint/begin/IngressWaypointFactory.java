package pwcg.mission.flight.waypoint.begin;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.McuWaypoint;

public class IngressWaypointFactory
{
    public enum IngressWaypointPattern
    {
        INGRESS_NEAR_FRONT,
        INGRESS_NEAR_TARGET;
    }
    
    public static McuWaypoint createIngressWaypoint(IngressWaypointPattern pattern, IFlight flight) throws PWCGException
    {
        if (pattern == IngressWaypointPattern.INGRESS_NEAR_FRONT)
        {
            return createIngressWaypointNearFront(flight);
        }
        else
        {
            return createIngressWaypointNearTarget(flight);
        }
    }
    
    private static McuWaypoint createIngressWaypointNearFront(IFlight flight) throws PWCGException  
    {
        IIngressWaypoint ingressWaypointGenerator = new IngressWaypointNearFront(flight);
        McuWaypoint ingressWaypoint = ingressWaypointGenerator.createIngressWaypoint();
        return ingressWaypoint;
    }
    
    private static McuWaypoint createIngressWaypointNearTarget(IFlight flight) throws PWCGException  
    {
        IIngressWaypoint ingressWaypointGenerator = new IngressWaypointNearTarget(flight);
        McuWaypoint ingressWaypoint = ingressWaypointGenerator.createIngressWaypoint();
        return ingressWaypoint;
    }

}
