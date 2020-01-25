package pwcg.mission.flight.waypoint.begin;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class AirStartNearAirfieldBuilder
{
    public static McuWaypoint buildAirStartNearAirfield(IFlight flight) throws PWCGException
    {
        Coordinate airfieldCoordinate = flight.getFlightHomePosition();
        
        double angleFromAirfield = MathUtils.calcAngle(airfieldCoordinate, flight.getFlightInformation().getTargetPosition());
        Coordinate airStartPosition = MathUtils.calcNextCoord(airfieldCoordinate, angleFromAirfield, 3000.0);
        airStartPosition.setYPos(WaypointType.getAltitudeForWaypointType(WaypointType.AIR_START_WAYPOINT, flight.getFlightInformation().getAltitude()));
        
        McuWaypoint airStartWP = WaypointFactory.createAirStartWaypointType();
        airStartWP.setPosition(airStartPosition);
        airStartWP.setOrientation(new Orientation(angleFromAirfield));
        return airStartWP;
    }
}
