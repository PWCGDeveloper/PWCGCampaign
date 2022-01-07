package pwcg.mission.flight.waypoint.missionpoint;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.McuWaypoint;

public class MissionPointSetFactory
{
    public static IMissionPointSet createFlightActivate(IFlight flight, McuWaypoint ingressWaypoint) throws PWCGException, PWCGException 
    {
        MissionPointFlightActivateVirtual flightActivate = new MissionPointFlightActivateVirtual();
        flightActivate.createFlightActivate(ingressWaypoint);
        return flightActivate;
    }

    public static IMissionPointSet createFlightBegin(IFlight flight, IMissionPointSet flightActivate, McuWaypoint waypointToLinkAirSTart) throws PWCGException, PWCGException 
    {
        MissionPointFlightBeginVirtual flightBegin = new MissionPointFlightBeginVirtual(flight, waypointToLinkAirSTart);
        flightBegin.createFlightBegin();
        return flightBegin;
    }

    public static IMissionPointSet createFlightEnd(IFlight flight) throws PWCGException, PWCGException 
    {
        MissionPointFlightEnd flightEnd = new MissionPointFlightEnd(flight);
        flightEnd.createFlightEnd();
        return flightEnd;
    }

    public static IMissionPointSet duplicateWaypointsWithOffset(IFlight escortedFlight, int altitudeOffset, int horizontalOffset) throws PWCGException
    {
        DuplicatedMissionPointSetBuilder duplicatedMissionPointSetBuilder = new DuplicatedMissionPointSetBuilder(escortedFlight, altitudeOffset, horizontalOffset); 
        IMissionPointSet duplicatedMissionPointSet = duplicatedMissionPointSetBuilder.createDuplicateWaypointsWithOffset();
        return duplicatedMissionPointSet;
    }
}
