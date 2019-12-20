package pwcg.mission.flight.waypoint.initial;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class AirStartNearIngressWaypointBuilder
{
    public static McuWaypoint buildAirStartNearIngress(Flight flight) throws PWCGException
    {
        McuWaypoint ingressWaypoint = flight.getWaypointPackage().getWaypointByType(WaypointType.INGRESS_WAYPOINT);
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
