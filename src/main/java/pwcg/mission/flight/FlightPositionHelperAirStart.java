package pwcg.mission.flight;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;

public class FlightPositionHelperAirStart
{
    public static void createPlanePositionCloseToFirstWP(Flight flight) throws PWCGException
    {
        Coordinate firstWaypointCoordinate = flight.findFirstWaypointPosition();

        double angleBetweenTargetAndInitialWaypoint = MathUtils.calcAngle(flight.getTargetCoords(), firstWaypointCoordinate);
        
        Coordinate startCoordinate = MathUtils.calcNextCoord(flight.findFirstWaypointPosition(), angleBetweenTargetAndInitialWaypoint, 3000);
        startCoordinate.setYPos(firstWaypointCoordinate.getYPos());

        Orientation startOrientation = new Orientation(MathUtils.adjustAngle(angleBetweenTargetAndInitialWaypoint, 180));

        AirStartFormationSetter flightPositionHelperAirStart = new AirStartFormationSetter(flight.getCampaign(), flight);
        flightPositionHelperAirStart.resetAirStartFormation(startCoordinate, startOrientation);
    }
}
