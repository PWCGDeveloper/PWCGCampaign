package pwcg.mission.flight.waypoint;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.McuWaypoint;

public class IngressWaypointNearTarget extends IngressWaypointBase
{
    public IngressWaypointNearTarget(Flight flight, Coordinate lastPosition, Coordinate targetPosition, int waypointSpeed, int waypointAltitude) throws PWCGException 
    {
        super(flight, lastPosition, targetPosition, waypointSpeed, waypointAltitude);
    }

    public McuWaypoint createIngressWaypoint() throws PWCGException  
    {
        Coordinate groundIngressCoords = getBestBomberHomeDefenseCoordinate();
        
        Coordinate coord = new Coordinate();
        coord.setXPos(groundIngressCoords.getXPos());
        coord.setZPos(groundIngressCoords.getZPos());
        coord.setYPos(waypointAltitude);

        McuWaypoint ingressWP = WaypointFactory.createIngressWaypointType();
        ingressWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        ingressWP.setSpeed(waypointSpeed);
        ingressWP.setPosition(coord);   
        ingressWP.setTargetWaypoint(false);
        
        return ingressWP;
    }

    private Coordinate getBestBomberHomeDefenseCoordinate() throws PWCGException 
    {
        double angleToIntercept = MathUtils.calcAngle(lastPosition, targetPosition);
        double distanceToIntercept = MathUtils.calcDist(lastPosition, targetPosition);
        Coordinate ingressCoordinate = MathUtils.calcNextCoord(lastPosition, angleToIntercept, distanceToIntercept / 2);
        return ingressCoordinate;
    }

}
