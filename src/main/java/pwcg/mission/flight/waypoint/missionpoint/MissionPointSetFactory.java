package pwcg.mission.flight.waypoint.missionpoint;

import pwcg.campaign.api.IAirfield;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory.AirStartPattern;
import pwcg.mission.mcu.McuWaypoint;

public class MissionPointSetFactory
{
    public static IMissionPointSet createFlightActivate(IFlight flight) throws PWCGException, PWCGException 
    {
        MissionPointFlightActivate flightActivate = new MissionPointFlightActivate(flight);
        flightActivate.createFlightActivate();
        return flightActivate;
    }

    public static IMissionPointSet createFlightBegin(IFlight flight, AirStartPattern airStartNearAirfield, McuWaypoint waypointToLinkAirSTart) throws PWCGException, PWCGException 
    {
        IFlightInformation flightInformation = flight.getFlightInformation();

        if (!flightInformation.isAirStart())
        {
            MissionPointFlightBeginTakeoff flightBegin = new MissionPointFlightBeginTakeoff(flight);
            flightBegin.createFlightBegin();
            return flightBegin;
        }
        else if (flightInformation.isVirtual())
        {
            MissionPointFlightBeginVirtual flightBegin = new MissionPointFlightBeginVirtual(flight, airStartNearAirfield, waypointToLinkAirSTart);
            flightBegin.createFlightBegin();
            return flightBegin;
        }        
        else if (flightInformation.isAirStart())
        {
            MissionPointFlightBeginAirStart flightBegin = new MissionPointFlightBeginAirStart(flight, airStartNearAirfield, waypointToLinkAirSTart);
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
        IAirfield landingAirfield = flight.getFlightInformation().getAirfield();
        return createFlightEnd(flight, landingAirfield);
    }
    
    public static IMissionPointSet createFlightEnd(IFlight flight, IAirfield landingAirfield) throws PWCGException, PWCGException 
    {
        MissionPointFlightEnd flightEnd = new MissionPointFlightEnd(flight, landingAirfield);
        flightEnd.createFlightEnd();
        return flightEnd;
    }
    
    public static IMissionPointSet createFlightRendezvous (IFlight flight, McuWaypoint ingressWaypoint) throws PWCGException
    {
        RendezvousMissionPointSetBuilder rendezvousMissionPointSetBuilder = new RendezvousMissionPointSetBuilder(flight); 
        IMissionPointSet rendezvousMissionPointSet = rendezvousMissionPointSetBuilder.createFlightRendezvous(ingressWaypoint);
        return rendezvousMissionPointSet;
    }
}
