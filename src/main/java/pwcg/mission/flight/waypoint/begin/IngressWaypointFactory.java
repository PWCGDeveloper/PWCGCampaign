package pwcg.mission.flight.waypoint.begin;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.McuWaypoint;

public class IngressWaypointFactory
{
    public enum IngressWaypointPattern
    {
        INGRESS_NEAR_FRONT,
        INGRESS_TOWARDS_TARGET,
        INGRESS_AT_TARGET,
        INGRESS_NEAR_FIELD;
    }
    
    public static McuWaypoint createIngressWaypoint(IngressWaypointPattern pattern, IFlight flight) throws PWCGException
    {
        if (flight.getMission().isAAATruckMission())
        {
            pattern = IngressWaypointPattern.INGRESS_AT_TARGET;
        }
        
        if (pattern == IngressWaypointPattern.INGRESS_NEAR_FRONT)
        {
            return createIngressWaypointNearFront(flight);
        }
        else if (pattern == IngressWaypointPattern.INGRESS_TOWARDS_TARGET)
        {
            return createIngressWaypointNearTarget(flight);
        }
        else if (pattern == IngressWaypointPattern.INGRESS_AT_TARGET)
        {
            return createIngressWaypointAtTarget(flight, 0);
        }
        else
        {
            return createIngressWaypointNearField(flight);
        }
    }
    
    public static McuWaypoint createIngressWaypointAtTarget(IFlight flight, double distanceToIngress) throws PWCGException
    {
        if (distanceToIngress < IngressBackoffUsingConfigsCalculator.MINIMUM_BACKOFF)
        {
            distanceToIngress = IngressBackoffUsingConfigsCalculator.calculateDistanbceToTargetFromConfigs();
        }
        IngressWaypointAtTarget ingressWaypointGenerator = new IngressWaypointAtTarget(flight);
        McuWaypoint ingressWaypoint = ingressWaypointGenerator.createIngressWaypoint(distanceToIngress);
        return ingressWaypoint;
    }

    private static McuWaypoint createIngressWaypointNearFront(IFlight flight) throws PWCGException  
    {
        IIngressWaypoint ingressWaypointGenerator = new IngressWaypointNearFront(flight);
        McuWaypoint ingressWaypoint = ingressWaypointGenerator.createIngressWaypoint();
        return ingressWaypoint;
    }
    
    private static McuWaypoint createIngressWaypointNearTarget(IFlight flight) throws PWCGException  
    {
        IIngressWaypoint ingressWaypointGenerator = new IngressWaypointHalfWayToTarget(flight);
        McuWaypoint ingressWaypoint = ingressWaypointGenerator.createIngressWaypoint();
        return ingressWaypoint;
    }
    
    private static McuWaypoint createIngressWaypointNearField(IFlight flight) throws PWCGException  
    {
        IIngressWaypoint ingressWaypointGenerator = new IngressWaypointNearField(flight);
        McuWaypoint ingressWaypoint = ingressWaypointGenerator.createIngressWaypoint();
        return ingressWaypoint;
    }
}
