package pwcg.mission.flight.waypoint.initial;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class AirStartNearAirfieldBuilder
{
    public static McuWaypoint buildAirStartNearAirfield(Flight flight) throws PWCGException
    {
        Coordinate airfieldCoordinate = flight.getPosition();
        
        double angleFromAirfield = MathUtils.calcAngle(airfieldCoordinate, flight.getTargetPosition());
        Coordinate airStartPosition = MathUtils.calcNextCoord(airfieldCoordinate, angleFromAirfield, 3000.0);
        airStartPosition.setYPos(WaypointType.getAltitudeForWaypointType(WaypointType.AIR_START_WAYPOINT, flight.getFlightAltitude()));
        
        McuWaypoint airStartWP = WaypointFactory.createAirStartWaypointType();
        airStartWP.setPosition(airStartPosition);
        airStartWP.setOrientation(new Orientation(angleFromAirfield));
        return airStartWP;
    }
}
