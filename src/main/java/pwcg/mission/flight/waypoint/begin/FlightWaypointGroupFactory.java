package pwcg.mission.flight.waypoint.begin;

import pwcg.campaign.api.IAirfield;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory.AirStartPattern;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointFlightBeginAirStart;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointFlightBeginTakeoff;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointFlightBeginVirtual;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointFlightEnd;
import pwcg.mission.mcu.McuWaypoint;

public class FlightWaypointGroupFactory
{
    public static IMissionPointSet createFlightBegin(IFlight flight, AirStartPattern airStartNearAirfield, McuWaypoint ingressWaypoint) throws PWCGException, PWCGException 
    {
        IFlightInformation flightInformation = flight.getFlightData().getFlightInformation();

        if (!flightInformation.isAirStart())
        {
            MissionPointFlightBeginTakeoff flightBegin = new MissionPointFlightBeginTakeoff(flight);
            flightBegin.createFlightBegin();
            return flightBegin;
        }
        else if (flightInformation.isVirtual())
        {
            MissionPointFlightBeginVirtual flightBegin = new MissionPointFlightBeginVirtual(flight, airStartNearAirfield, ingressWaypoint);
            flightBegin.createFlightBegin();
            return flightBegin;
        }        
        else if (flightInformation.isAirStart())
        {
            MissionPointFlightBeginAirStart flightBegin = new MissionPointFlightBeginAirStart(flight, airStartNearAirfield, ingressWaypoint);
            flightBegin.createFlightBegin();
            return flightBegin;
        }
        else
        {
            throw new PWCGException("Flight does not match any expected criteria for start");
        }
    }
    
    public static IMissionPointSet createFlightEndAtHomeField(IFlight flight) throws PWCGException, PWCGException 
    {
        IAirfield landingAirfield = flight.getFlightData().getFlightInformation().getAirfield();
        return createFlightEnd(flight, landingAirfield);
    }
    
    public static IMissionPointSet createFlightEnd(IFlight flight, IAirfield landingAirfield) throws PWCGException, PWCGException 
    {
        MissionPointFlightEnd flightEnd = new MissionPointFlightEnd(flight, landingAirfield);
        flightEnd.createFlightEnd();
        flightEnd.createTargetAssociations();
        return flightEnd;
    }
}
