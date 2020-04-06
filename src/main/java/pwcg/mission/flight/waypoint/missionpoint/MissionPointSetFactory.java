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

    public static IMissionPointSet createFlightBegin(IFlight flight, IMissionPointSet flightActivate, AirStartPattern suggestedAirStartPattern, McuWaypoint waypointToLinkAirSTart) throws PWCGException, PWCGException 
    {
        IFlightInformation flightInformation = flight.getFlightInformation();
        if (!flightInformation.isAirStart())
        {
            MissionPointFlightBeginTakeoff flightBegin = new MissionPointFlightBeginTakeoff(flight, flightActivate);
            flightBegin.createFlightBegin();
            return flightBegin;
        }
        else if (flightInformation.isVirtual())
        {
            AirStartPattern airStartPattern = determineAirStartPattern(flightInformation, suggestedAirStartPattern);
            MissionPointFlightBeginVirtual flightBegin = new MissionPointFlightBeginVirtual(flight, airStartPattern, waypointToLinkAirSTart);
            flightBegin.createFlightBegin();
            return flightBegin;
        }        
        else if (flightInformation.isAirStart())
        {
            AirStartPattern airStartPattern = determineAirStartPattern(flightInformation, suggestedAirStartPattern);
            MissionPointFlightBeginAirStart flightBegin = new MissionPointFlightBeginAirStart(flight, airStartPattern, waypointToLinkAirSTart);
            flightBegin.createFlightBegin();
            return flightBegin;
        }
        else
        {
            throw new PWCGException("Flight does not match any expected criteria for start");
        }
    }
    
    private static AirStartPattern determineAirStartPattern(IFlightInformation flightInformation, AirStartPattern suggestedAirStartPattern)
    {
        if (flightInformation.isPlayerFlight())
        {
            return AirStartPattern.AIR_START_NEAR_WAYPOINT;
        }

        return suggestedAirStartPattern;
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

    public static IMissionPointSet duplicateWaypointsWithOffset(IFlight escortedFlight, int altitudeOffset, int horizontalOffset) throws PWCGException
    {
        DuplicatedMissionPointSetBuilder duplicatedMissionPointSetBuilder = new DuplicatedMissionPointSetBuilder(escortedFlight, altitudeOffset, horizontalOffset); 
        IMissionPointSet duplicatedMissionPointSet = duplicatedMissionPointSetBuilder.createDuplicateWaypointsWithOffset();
        return duplicatedMissionPointSet;
    }
}
