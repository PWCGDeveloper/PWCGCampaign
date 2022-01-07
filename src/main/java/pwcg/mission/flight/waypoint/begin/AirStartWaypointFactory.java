package pwcg.mission.flight.waypoint.begin;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuWaypoint;

public class AirStartWaypointFactory 
{
    public static McuWaypoint createAirStart(IFlight flight, McuWaypoint referenceWaypointForAirStart) throws PWCGException
    {
        return createAirStartNearWaypoint(flight, referenceWaypointForAirStart);
    }

    private static McuWaypoint createAirStartNearWaypoint(IFlight flight, McuWaypoint referenceWaypointForAirStart) throws PWCGException
    {
        double angleOfEntryToWP = referenceWaypointForAirStart.getOrientation().getyOri();
        double angleBack = MathUtils.adjustAngle(angleOfEntryToWP, 180);

        Coordinate airStartPosition = MathUtils.calcNextCoord(referenceWaypointForAirStart.getPosition(), angleBack, 3000.0);
        airStartPosition.setYPos(referenceWaypointForAirStart.getPosition().getYPos());
        
        McuWaypoint airStartWP = WaypointFactory.createAirStartWaypointType();
        airStartWP.setPosition(airStartPosition);
        airStartWP.setOrientation(new Orientation(angleOfEntryToWP));
        airStartWP.setSpeed(flight.getFlightCruisingSpeed());
        return airStartWP;
    }  
}
