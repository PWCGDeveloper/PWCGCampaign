package pwcg.mission.flight.waypoint.missionpoint;

import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory.AirStartPattern;
import pwcg.mission.mcu.McuWaypoint;

public class MissionPointSetFactory
{
    public static IMissionPointSet createFlightActivate(IFlight flight) throws PWCGException, PWCGException 
    {
        if (flight.getFlightInformation().isVirtual())
        {
            MissionPointFlightActivateVirtual flightActivate = new MissionPointFlightActivateVirtual(flight);
            flightActivate.createFlightActivate();
            return flightActivate;
        }
        else if (flight.getFlightInformation().isAiTriggeredTakeoff())
        {
            MissionPointFlightActivateTriggered flightActivate = new MissionPointFlightActivateTriggered(flight);
            flightActivate.createFlightActivate();
            return flightActivate;
        }
        else
        {
            MissionPointFlightActivateReal flightActivate = new MissionPointFlightActivateReal(flight);
            flightActivate.createFlightActivate();
            return flightActivate;
        }
    }

    public static IMissionPointSet createScrambleFlightActivateWithDelay(IFlight flight, int delaySeconds) throws PWCGException, PWCGException 
    {
        MissionPointFlightActivateTriggered flightActivate = new MissionPointFlightActivateTriggered(flight,delaySeconds);
        flightActivate.createFlightActivate();
        return flightActivate;
    }

    public static IMissionPointSet createFlightBegin(IFlight flight, IMissionPointSet flightActivate, AirStartPattern airStartPattern, McuWaypoint waypointToLinkAirSTart) throws PWCGException, PWCGException 
    {
        MissionPointFlightBeginVirtual flightBegin = new MissionPointFlightBeginVirtual(flight, airStartPattern, waypointToLinkAirSTart);
        flightBegin.createFlightBegin();
        return flightBegin;
    }

    public static IMissionPointSet createFlightEnd(IFlight flight) throws PWCGException, PWCGException 
    {
        MissionPointFlightEnd flightEnd = new MissionPointFlightEnd(flight);
        flightEnd.createFlightEnd();
        return flightEnd;
    }
    
    public static IMissionPointSet createFlightRendezvous (IFlight flight, McuWaypoint ingressWaypoint) throws PWCGException
    {
        RendezvousMissionPointSetBuilder rendezvousMissionPointSetBuilder = new RendezvousMissionPointSetBuilder(flight); 
        IMissionPointSet rendezvousMissionPointSet = rendezvousMissionPointSetBuilder.createFlightRendezvousToPickUpEscort(ingressWaypoint);
        return rendezvousMissionPointSet;
    }

    public static IMissionPointSet duplicateWaypointsWithOffset(IFlight escortedFlight, int altitudeOffset, int horizontalOffset) throws PWCGException
    {
        DuplicatedMissionPointSetBuilder duplicatedMissionPointSetBuilder = new DuplicatedMissionPointSetBuilder(escortedFlight, altitudeOffset, horizontalOffset); 
        IMissionPointSet duplicatedMissionPointSet = duplicatedMissionPointSetBuilder.createDuplicateWaypointsWithOffset();
        return duplicatedMissionPointSet;
    }
}
