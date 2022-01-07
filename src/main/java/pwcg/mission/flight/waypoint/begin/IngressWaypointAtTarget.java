package pwcg.mission.flight.waypoint.begin;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuWaypoint;

public class IngressWaypointAtTarget implements IIngressWaypoint
{
    private IFlight flight;

    public IngressWaypointAtTarget(IFlight flight) throws PWCGException 
    {
        this.flight = flight;
    }

    public McuWaypoint createIngressWaypoint() throws PWCGException  
    {
        Coordinate ingressCoords = getIngressWaypointAtTarget();
        ingressCoords.setYPos(flight.getFlightInformation().getAltitude());

        McuWaypoint ingressWP = WaypointFactory.createIngressWaypointType();
        ingressWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        ingressWP.setSpeed(flight.getFlightCruisingSpeed());
        ingressWP.setPosition(ingressCoords);   
        ingressWP.setTargetWaypoint(false);
        
        return ingressWP;
    }

    private Coordinate getIngressWaypointAtTarget() throws PWCGException 
    {        
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int minDistanceToTarget = productSpecific.getIngressAtTargetMinDIstance();
        int maxDistanceToTarget = productSpecific.getIngressAtTargetMaxDIstance();
        int randomDistanceToTarget = RandomNumberGenerator.getRandom(maxDistanceToTarget - minDistanceToTarget);
        int distanceToTarget = minDistanceToTarget + randomDistanceToTarget;

        double angleFromTarget = MathUtils.calcAngle(flight.getTargetDefinition().getPosition(), flight.getFlightInformation().getHomePosition());
        Coordinate ingressCoordinate = MathUtils.calcNextCoord(flight.getTargetDefinition().getPosition(), angleFromTarget, distanceToTarget);
        return ingressCoordinate;
    }
}
