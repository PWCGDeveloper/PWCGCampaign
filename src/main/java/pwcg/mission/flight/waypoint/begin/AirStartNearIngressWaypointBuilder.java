package pwcg.mission.flight.waypoint.begin;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuWaypoint;

public class AirStartNearIngressWaypointBuilder
{
    public static McuWaypoint buildAirStartNearIngress(IFlight flight, McuWaypoint ingressWaypoint) throws PWCGException
    {
        Orientation ingressOrientation = ingressWaypoint.getOrientation();
        
        double angleBack = MathUtils.adjustAngle(ingressOrientation.getyOri(), 180);
        Coordinate airStartPosition = MathUtils.calcNextCoord(ingressWaypoint.getPosition(), angleBack, 3000.0);
        airStartPosition.setYPos(ingressWaypoint.getPosition().getYPos());
        
        McuWaypoint airStartWP = WaypointFactory.createAirStartWaypointType();
        airStartWP.setPosition(airStartPosition);
        airStartWP.setOrientation(ingressOrientation.copy());
        return airStartWP;
    }
}
