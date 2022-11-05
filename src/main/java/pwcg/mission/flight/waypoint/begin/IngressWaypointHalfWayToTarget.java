package pwcg.mission.flight.waypoint.begin;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuWaypoint;

public class IngressWaypointHalfWayToTarget implements IIngressWaypoint
{
    private IFlight flight;

    public IngressWaypointHalfWayToTarget(IFlight flight) throws PWCGException 
    {
        this.flight = flight;
    }

    public McuWaypoint createIngressWaypoint() throws PWCGException  
    {
        Coordinate ingressCoords = getIngressWaypointNearTarget();
        ingressCoords.setYPos(flight.getFlightInformation().getAltitude());

        McuWaypoint ingressWP = WaypointFactory.createIngressWaypointType();
        ingressWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        ingressWP.setSpeed(flight.getFlightCruisingSpeed());
        ingressWP.setPosition(ingressCoords);   
        ingressWP.setTargetWaypoint(false);
        
        return ingressWP;
    }

    private Coordinate getIngressWaypointNearTarget() throws PWCGException 
    {
        double angleToTarget = MathUtils.calcAngle(flight.getFlightHomePosition(), flight.getTargetDefinition().getPosition());
        double distanceToTarget = MathUtils.calcDist(flight.getFlightHomePosition(), flight.getTargetDefinition().getPosition());
        Coordinate ingressCoordinate = MathUtils.calcNextCoord(flight.getCampaignMap(), flight.getFlightHomePosition(), angleToTarget, distanceToTarget / 2);
        
        double distance = MathUtils.calcDist(flight.getTargetDefinition().getPosition(), ingressCoordinate);
        if (isIngressTooCloseToTarget(distance))
        {
            ingressCoordinate = moveIngressZoneAwayFromTarget(ingressCoordinate, flight.getTargetDefinition().getPosition());
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
        Coordinate movedIngressCoordinate = MathUtils.calcNextCoord(flight.getCampaignMap(), ingressCoordinate, angleAwayFromTarget, getIngressTooCloseToTargetDistance());
        return movedIngressCoordinate;
    }

    private int getIngressTooCloseToTargetDistance()
    {
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int ingressTooCloseToAttackDistance = productSpecificConfiguration.getBombFinalApproachDistance() * 2;
        return ingressTooCloseToAttackDistance;
    }
}
