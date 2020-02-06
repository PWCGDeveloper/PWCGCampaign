package pwcg.mission.flight.waypoint.begin;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class AirStartWaypointFactory 
{
    public enum AirStartPattern
    {
        AIR_START_NEAR_AIRFIELD,
        AIR_START_NEAR_WAYPOINT;
    }

    public static McuWaypoint createAirStart(IFlight flight, AirStartPattern pattern, McuWaypoint referenceWaypointForAirStart) throws PWCGException
    {
        if (flight.isPlayerFlight())
        {
            return createAirStartNearWaypoint(flight, referenceWaypointForAirStart);
        }
        else if (pattern == AirStartPattern.AIR_START_NEAR_WAYPOINT)
        {
            return createAirStartNearWaypoint(flight, referenceWaypointForAirStart);
        }
        else
        {
            return createAirStartNearAirfield(flight);
        }
    }

    private static McuWaypoint createAirStartNearWaypoint(IFlight flight, McuWaypoint referenceWaypointForAirStart) throws PWCGException
    {
        Orientation waypolintOrientation = referenceWaypointForAirStart.getOrientation();
        
        double angleBack = MathUtils.adjustAngle(waypolintOrientation.getyOri(), 180);
        Coordinate airStartPosition = MathUtils.calcNextCoord(referenceWaypointForAirStart.getPosition(), angleBack, 3000.0);
        airStartPosition.setYPos(referenceWaypointForAirStart.getPosition().getYPos());
        
        McuWaypoint airStartWP = WaypointFactory.createAirStartWaypointType();
        airStartWP.setPosition(airStartPosition);
        airStartWP.setOrientation(waypolintOrientation.copy());
        return airStartWP;
    }  

    private static McuWaypoint createAirStartNearAirfield(IFlight flight) throws PWCGException
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
