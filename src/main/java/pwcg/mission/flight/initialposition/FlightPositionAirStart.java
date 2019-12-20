package pwcg.mission.flight.initialposition;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointType;

public class FlightPositionAirStart
{
    public static void createPlanePositionAirStart(Flight flight) throws PWCGException
    {
        if (flight.getWaypointPackage().getWaypointsForLeadPlane().size() < 2)
        {
            throw new PWCGException("Flight generated with fewer than two waypoints : " + flight.getFlightType());
        }
        
        Coordinate firstWaypointPosition = flight.getWaypointPackage().getWaypointsForLeadPlane().get(0).getPosition();
        Coordinate secondWaypointPosition = flight.getWaypointPackage().getWaypointsForLeadPlane().get(1).getPosition();

        double angleBetweenFirstAndSecondWaypoint = MathUtils.calcAngle(firstWaypointPosition, secondWaypointPosition);
        double angleBetweenFirstAndSecondWaypointReversed = MathUtils.adjustAngle(angleBetweenFirstAndSecondWaypoint, 180);
        
        Coordinate startCoordinate = MathUtils.calcNextCoord(firstWaypointPosition, angleBetweenFirstAndSecondWaypointReversed, 1000);
        startCoordinate.setYPos(WaypointType.getAltitudeForWaypointType(WaypointType.AIR_START_WAYPOINT, flight.getFlightAltitude()));
        AirStartFormationSetter.resetAirStartFormation(flight, startCoordinate);
    }
 }
