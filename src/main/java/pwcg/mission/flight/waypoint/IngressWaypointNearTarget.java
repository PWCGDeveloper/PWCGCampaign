package pwcg.mission.flight.waypoint;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.AttackMcuSequence;
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
        Coordinate groundIngressCoords = getBestGroundAttackCoordinate();
        groundIngressCoords.setYPos(waypointAltitude);

        McuWaypoint ingressWP = WaypointFactory.createIngressWaypointType();
        ingressWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        ingressWP.setSpeed(waypointSpeed);
        ingressWP.setPosition(groundIngressCoords);   
        ingressWP.setTargetWaypoint(false);
        
        return ingressWP;
    }

    private Coordinate getBestGroundAttackCoordinate() throws PWCGException 
    {
        double angleToIntercept = MathUtils.calcAngle(lastPosition, targetPosition);
        double distanceToIntercept = MathUtils.calcDist(lastPosition, targetPosition);
        Coordinate ingressCoordinate = MathUtils.calcNextCoord(lastPosition, angleToIntercept, distanceToIntercept / 2);
        
        double distance = MathUtils.calcDist(targetPosition, ingressCoordinate);
        if (distance < (AttackMcuSequence.CHECK_ZONE_INSTANCE + 10000))
        {
            ingressCoordinate = moveIngressZoneAwayFromTarget(ingressCoordinate, targetPosition);
        }
        
        return ingressCoordinate;
    }

    private Coordinate moveIngressZoneAwayFromTarget(Coordinate ingressCoordinate, Coordinate targetPosition) throws PWCGException
    {
        double angleAwayFromTarget = MathUtils.calcAngle(targetPosition, ingressCoordinate);
        Coordinate movedIngressCoordinate = MathUtils.calcNextCoord(ingressCoordinate, angleAwayFromTarget, AttackMcuSequence.CHECK_ZONE_INSTANCE + 10000);
        return movedIngressCoordinate;
    }

}
