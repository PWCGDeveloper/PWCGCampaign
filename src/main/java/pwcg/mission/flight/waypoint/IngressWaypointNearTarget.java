package pwcg.mission.flight.waypoint;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.AttackMcuSequence;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.McuWaypoint;

public class IngressWaypointNearTarget implements IIngressWaypoint
{
    public static final int INGRESS_TOO_CLOSE_TO_ATTACK = AttackMcuSequence.ATTACK_MCU_TRIGGER_DISTANCE + 12000;

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
        double angleToTarget = MathUtils.calcAngle(flight.getPosition(), flight.getTargetCoords());
        double distanceToTarget = MathUtils.calcDist(flight.getPosition(), flight.getTargetCoords());
        Coordinate ingressCoordinate = MathUtils.calcNextCoord(flight.getPosition(), angleToTarget, distanceToTarget / 2);
        
        double distance = MathUtils.calcDist(flight.getTargetCoords(), ingressCoordinate);
        if (distance < INGRESS_TOO_CLOSE_TO_ATTACK)
        {
            ingressCoordinate = moveIngressZoneAwayFromTarget(ingressCoordinate, flight.getTargetCoords());
        }
        
        return ingressCoordinate;
    }

    private Coordinate moveIngressZoneAwayFromTarget(Coordinate ingressCoordinate, Coordinate targetPosition) throws PWCGException
    {
        double angleAwayFromTarget = MathUtils.calcAngle(targetPosition, ingressCoordinate);
        Coordinate movedIngressCoordinate = MathUtils.calcNextCoord(ingressCoordinate, angleAwayFromTarget, INGRESS_TOO_CLOSE_TO_ATTACK);
        return movedIngressCoordinate;
    }

}
