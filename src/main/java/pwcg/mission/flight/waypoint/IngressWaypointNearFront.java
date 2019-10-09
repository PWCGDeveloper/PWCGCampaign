package pwcg.mission.flight.waypoint;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.mcu.McuWaypoint;

public class IngressWaypointNearFront implements IIngressWaypoint
{
    private Campaign campaign;
    private Flight flight;

    public IngressWaypointNearFront(Flight flight) throws PWCGException 
    {
        this.campaign = flight.getCampaign();
        this.flight = flight;
    }

    public McuWaypoint createIngressWaypoint() throws PWCGException  
    {
        Coordinate groundIngressCoords = getBestIngressPosition();
        
        Coordinate coord = new Coordinate();
        coord.setXPos(groundIngressCoords.getXPos());
        coord.setZPos(groundIngressCoords.getZPos());
        coord.setYPos(flight.getFlightAltitude());

        McuWaypoint ingressWP = WaypointFactory.createIngressWaypointType();
        ingressWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        ingressWP.setSpeed(flight.getFlightCruisingSpeed());
        ingressWP.setPosition(coord);   
        ingressWP.setTargetWaypoint(false);
        
        return ingressWP;
    }

    private Coordinate getBestIngressPosition() throws PWCGException 
    {
        if (flight.isVirtual())
        {
            return getBestIngressPositionForCommonAIFlights();
        }
        else
        {
            if (flight.getFlightType() == FlightTypes.ESCORT)
            {
                return getBestIngressPositionForEscortRendezvous();
            }
            else
            {
                return getBestIngressPositionBehindFriendlyLines();
            }
        }
    }

    private Coordinate getBestIngressPositionForCommonAIFlights() throws PWCGException 
    {
        FrontLinesForMap frontLinesForMap =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        Coordinate closestFrontLinesToTarget = frontLinesForMap.findClosestFrontCoordinateForSide(flight.getTargetCoords(), flight.getSquadron().determineSide());
        
        Coordinate flightHomeCoordinates = flight.getSquadron().determineCurrentPosition(campaign.getDate());
        double angleFromFrontToHome = MathUtils.calcAngle(closestFrontLinesToTarget, flightHomeCoordinates);
        
        Coordinate ingressCoordinate = MathUtils.calcNextCoord(closestFrontLinesToTarget, angleFromFrontToHome, WaypointGeneratorUtils.INGRESS_DISTANCE_FROM_FRONT);
        return ingressCoordinate;
    }

    private Coordinate getBestIngressPositionBehindFriendlyLines() throws PWCGException 
    {
        FrontLinesForMap frontLinesForMap =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        Coordinate closestEnemyFrontLinesToTarget = frontLinesForMap.findClosestFrontCoordinateForSide(flight.getTargetCoords(), flight.getSquadron().determineSide().getOppositeSide());
        Coordinate closestFriendlyFrontLinesToTarget = frontLinesForMap.findClosestFrontCoordinateForSide(flight.getTargetCoords(), flight.getSquadron().determineSide());
        int distanceBehindFrontForIngress = getDistanceFromFront();

        return BehindFriendlyLinesPositionCalculator.getPointBehindFriendlyLines(
                closestEnemyFrontLinesToTarget, closestFriendlyFrontLinesToTarget, distanceBehindFrontForIngress, campaign.getDate(), flight.getSquadron().determineSide());
    }

    private Coordinate getBestIngressPositionForEscortRendezvous() throws PWCGException 
    {
        FrontLinesForMap frontLinesForMap =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        Coordinate closestEnemyFrontLinesToTarget = frontLinesForMap.findClosestFrontCoordinateForSide(flight.getTargetCoords(), flight.getSquadron().determineSide().getOppositeSide());
        int distanceBehindFrontForIngress = getDistanceFromFront();

        return BehindFriendlyLinesPositionCalculator.getPointBehindFriendlyLines(
                closestEnemyFrontLinesToTarget, flight.getHomePosition(), distanceBehindFrontForIngress, campaign.getDate(), flight.getSquadron().determineSide());
    }

    private int getDistanceFromFront()
    {
        int distanceBehindFrontForIngress =  WaypointGeneratorUtils.INGRESS_DISTANCE_FROM_FRONT;
        if (flight.getFlightType() == FlightTypes.ESCORT)
        {
            IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
            int rendezvousDistanceFromFront = productSpecific.getRendezvousDistanceFromFront();
            distanceBehindFrontForIngress += rendezvousDistanceFromFront;
        }
        return distanceBehindFrontForIngress;
    }
}
