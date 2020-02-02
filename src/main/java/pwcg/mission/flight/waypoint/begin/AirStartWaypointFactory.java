package pwcg.mission.flight.waypoint.begin;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.McuWaypoint;

public class AirStartWaypointFactory 
{
    public enum AirStartPattern
    {
        AIR_START_NEAR_AIRFIELD,
        AIR_START_NEAR_WAYPOINT;
    }

    public static McuWaypoint createAirStart(IFlight flight, AirStartPattern pattern, McuWaypoint waypoint) throws PWCGException
    {
        if (flight.isPlayerFlight())
        {
            return createAirStartNearWaypoint(flight, waypoint);
        }
        else if (pattern == AirStartPattern.AIR_START_NEAR_WAYPOINT)
        {
            return createAirStartNearWaypoint(flight, waypoint);
        }
        else
        {
            return createAirStartNearAirfield(flight);
        }
    }

    private static McuWaypoint createAirStartNearWaypoint(IFlight flight, McuWaypoint waypoint) throws PWCGException
    {
        return AirStartNearWaypointBuilder.buildAirStartNearWaypoint(flight, waypoint);
    }  

    private static McuWaypoint createAirStartNearAirfield(IFlight flight) throws PWCGException
    {
        return AirStartNearAirfieldBuilder.buildAirStartNearAirfield(flight);
    }  
}
