package pwcg.mission.flight.waypoint;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.McuWaypoint;

public class IngressWaypointScrambleOpposition implements IIngressWaypoint
{
    private Campaign campaign;
    private Flight flight;
        
    public IngressWaypointScrambleOpposition(Flight flight) throws PWCGException 
    {
        this.campaign = flight.getCampaign();
        this.flight = flight;
    }

    public McuWaypoint createIngressWaypoint() throws PWCGException  
    {
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        FrontLinePoint nearbyFrontPoint = frontLinesForMap.findCloseFrontPositionForSide(flight.getHomePosition(), 20000, flight.getSquadron().getCountry().getSide());
        Coordinate nearbyFrontPosition = nearbyFrontPoint.getPosition();
                
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int attackIngressDistance = productSpecificConfiguration.getGroundAttackIngressDistance();
        
        double ingressAngle = MathUtils.calcAngle(nearbyFrontPosition, flight.getTargetCoords());
        Coordinate ingressCoords = MathUtils.calcNextCoord(flight.getTargetCoords(), ingressAngle, attackIngressDistance);
        
        Coordinate coord = new Coordinate();
        coord.setXPos(ingressCoords.getXPos());
        coord.setZPos(ingressCoords.getZPos());
        coord.setYPos(flight.getFlightAltitude());

        McuWaypoint ingressWP = WaypointFactory.createIngressWaypointType();
        ingressWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
        ingressWP.setSpeed(flight.getFlightCruisingSpeed());
        ingressWP.setPosition(coord);   
        ingressWP.setTargetWaypoint(true);
        
        return ingressWP;
    }
}
