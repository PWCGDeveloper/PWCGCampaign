package pwcg.mission.flight.waypoint.begin;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuWaypoint;

public class IngressWaypointScrambleOpposition implements IIngressWaypoint
{
    private Campaign campaign;
    private IFlight flight;
        
    public IngressWaypointScrambleOpposition(IFlight flight) throws PWCGException 
    {
        this.campaign = flight.getCampaign();
        this.flight = flight;
    }

    public McuWaypoint createIngressWaypoint() throws PWCGException  
    {
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        FrontLinePoint nearbyFrontPoint = frontLinesForMap.findCloseFrontPositionForSide(flight.getFlightHomePosition(), 20000, flight.getFlightInformation().getSquadron().getCountry().getSide());
        Coordinate nearbyFrontPosition = nearbyFrontPoint.getPosition();
                
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int attackIngressDistance = productSpecificConfiguration.getGroundAttackIngressDistance();
        
        double ingressAngle = MathUtils.calcAngle(nearbyFrontPosition, flight.getFlightInformation().getTargetPosition());
        Coordinate ingressCoords = MathUtils.calcNextCoord(flight.getFlightInformation().getTargetPosition(), ingressAngle, attackIngressDistance);
        
        Coordinate coord = new Coordinate();
        coord.setXPos(ingressCoords.getXPos());
        coord.setZPos(ingressCoords.getZPos());
        coord.setYPos(flight.getFlightInformation().getAltitude());

        McuWaypoint ingressWP = WaypointFactory.createIngressWaypointType();
        ingressWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
        ingressWP.setSpeed(flight.getFlightPlanes().getFlightCruisingSpeed());
        ingressWP.setPosition(coord);   
        ingressWP.setTargetWaypoint(true);
        
        return ingressWP;
    }
}
