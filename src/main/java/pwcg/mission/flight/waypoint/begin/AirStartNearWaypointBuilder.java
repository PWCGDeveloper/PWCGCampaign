package pwcg.mission.flight.waypoint.begin;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuWaypoint;

public class AirStartNearWaypointBuilder
{
    public static McuWaypoint buildAirStartNearWaypoint(IFlight flight, McuWaypoint waypoint) throws PWCGException
    {
        Orientation waypolintOrientation = waypoint.getOrientation();
        
        double angleBack = MathUtils.adjustAngle(waypolintOrientation.getyOri(), 180);
        Coordinate airStartPosition = MathUtils.calcNextCoord(waypoint.getPosition(), angleBack, 3000.0);
        airStartPosition.setYPos(waypoint.getPosition().getYPos());
        
        McuWaypoint airStartWP = WaypointFactory.createAirStartWaypointType();
        airStartWP.setPosition(airStartPosition);
        airStartWP.setOrientation(waypolintOrientation.copy());
        return airStartWP;
    }
}
