package pwcg.mission.flight.waypoint;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
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
        double angleToTarget = MathUtils.calcAngle(flight.getPosition(), flight.getTargetCoords());
        double distanceToTarget = MathUtils.calcDist(flight.getPosition(), flight.getTargetCoords());
        Coordinate ingressCoordinate = MathUtils.calcNextCoord(flight.getPosition(), angleToTarget, distanceToTarget / 2);
        
        double distance = MathUtils.calcDist(flight.getTargetCoords(), ingressCoordinate);
        if (isIngressTooCloseToTarget(distance))
        {
            ingressCoordinate = moveIngressZoneAwayFromTarget(ingressCoordinate, flight.getTargetCoords());
        }
        
        return ingressCoordinate;
    }
    
    private boolean isIngressTooCloseToTarget(double distance)
    {
        int ingressTooCloseToAttackDistance = getIngressTooCloseToTargetDistance();
        if (distance < ingressTooCloseToAttackDistance)
        {
            return true;
        }
        return false;
    }

    private Coordinate moveIngressZoneAwayFromTarget(Coordinate ingressCoordinate, Coordinate targetPosition) throws PWCGException
    {
        double angleAwayFromTarget = MathUtils.calcAngle(targetPosition, ingressCoordinate);
        Coordinate movedIngressCoordinate = MathUtils.calcNextCoord(ingressCoordinate, angleAwayFromTarget, getIngressTooCloseToTargetDistance());
        return movedIngressCoordinate;
    }

    private int getIngressTooCloseToTargetDistance()
    {
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int ingressTooCloseToAttackDistance = productSpecificConfiguration.getBombFinalApproachDistance() * 2;
        return ingressTooCloseToAttackDistance;
    }
}
