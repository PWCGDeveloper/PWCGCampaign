package pwcg.mission.flight.waypoint.begin;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.McuWaypoint;

public class AirStartWaypointFactory 
{
    public enum AirStartPattern
    {
        AIR_START_NEAR_AIRFIELD,
        AIR_START_NEAR_INGRESS;
    }

    public static McuWaypoint createAirStart(IFlight flight, AirStartPattern pattern, McuWaypoint ingressWaypoint) throws PWCGException
    {
        if (flight.getFlightInformation().isPlayerFlight())
        {
            return createAirStartNearIngress(flight, ingressWaypoint);
        }
        else if (pattern == AirStartPattern.AIR_START_NEAR_INGRESS)
        {
            return createAirStartNearAirfield(flight);
        }
        else
        {
            return createAirStartNearIngress(flight, ingressWaypoint);
        }
    }

    private static McuWaypoint createAirStartNearIngress(IFlight flight, McuWaypoint ingressWaypoint) throws PWCGException
    {
        return AirStartNearIngressWaypointBuilder.buildAirStartNearIngress(flight, ingressWaypoint);
    }  

    private static McuWaypoint createAirStartNearAirfield(IFlight flight) throws PWCGException
    {
        return AirStartNearAirfieldBuilder.buildAirStartNearAirfield(flight);
    }  
}
