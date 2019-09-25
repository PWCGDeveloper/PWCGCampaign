package pwcg.mission.flight.waypoint;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.AttackMcuSequence;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.McuWaypoint;

public class IngressWaypointNearTarget implements IIngressWaypoint
{
    private Flight flight;

    public IngressWaypointNearTarget(Flight flight) throws PWCGException 
    {
        this.flight = flight;
    }

    public McuWaypoint createIngressWaypoint() throws PWCGException  
    {
        Coordinate ingressCoords = getIngressWaypointNearTarget();
        ingressCoords.setYPos(flight.getFlightAltitude());

        McuWaypoint ingressWP = WaypointFactory.createIngressWaypointType();
        ingressWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        ingressWP.setSpeed(flight.getFlightCruisingSpeed());
        ingressWP.setPosition(ingressCoords);   
        ingressWP.setTargetWaypoint(false);
        
        return ingressWP;
    }

    private Coordinate getIngressWaypointNearTarget() throws PWCGException 
    {
        double angleToIntercept = MathUtils.calcAngle(flight.getHomePosition(), flight.getTargetCoords());
        double distanceToIntercept = MathUtils.calcDist(flight.getHomePosition(), flight.getTargetCoords());
        Coordinate ingressCoordinate = MathUtils.calcNextCoord(flight.getHomePosition(), angleToIntercept, distanceToIntercept / 2);
        
        double distance = MathUtils.calcDist(flight.getTargetCoords(), ingressCoordinate);
        if (distance < (AttackMcuSequence.CHECK_ZONE_DEFAULT_DISTANCE + 10000))
        {
            ingressCoordinate = moveIngressZoneAwayFromTarget(ingressCoordinate, flight.getTargetCoords());
        }
        
        return ingressCoordinate;
    }

    private Coordinate moveIngressZoneAwayFromTarget(Coordinate ingressCoordinate, Coordinate targetPosition) throws PWCGException
    {
        double angleAwayFromTarget = MathUtils.calcAngle(targetPosition, ingressCoordinate);
        Coordinate movedIngressCoordinate = MathUtils.calcNextCoord(ingressCoordinate, angleAwayFromTarget, AttackMcuSequence.CHECK_ZONE_DEFAULT_DISTANCE + 10000);
        return movedIngressCoordinate;
    }

}
