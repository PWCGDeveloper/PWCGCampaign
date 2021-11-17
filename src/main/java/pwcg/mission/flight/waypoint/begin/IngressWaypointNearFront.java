package pwcg.mission.flight.waypoint.begin;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.utils.BehindFriendlyLinesPositionCalculator;

public class IngressWaypointNearFront implements IIngressWaypoint
{
    private Campaign campaign;
    private IFlight flight;

    public IngressWaypointNearFront(IFlight flight) throws PWCGException 
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
        coord.setYPos(flight.getFlightInformation().getAltitude());

        McuWaypoint ingressWP = WaypointFactory.createIngressWaypointType();
        ingressWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        ingressWP.setSpeed(flight.getFlightCruisingSpeed());
        ingressWP.setPosition(coord);   
        ingressWP.setTargetWaypoint(false);
        
        return ingressWP;
    }

    private Coordinate getBestIngressPosition() throws PWCGException 
    {
        if (flight.getFlightInformation().isVirtual())
        {
            return getBestIngressPositionForCommonAIFlights();
        }
        else
        {
            if (flight.getFlightType() == FlightTypes.ESCORT)
            {
                return getBestIngressPositionForEscortRendezvous();
            }
            else if (flight.getFlightType() == FlightTypes.PATROL)
            {
                return getBestIngressPositionForPatrol();
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
        Coordinate closestFrontLinesToTarget = frontLinesForMap.findClosestFrontCoordinateForSide(
                flight.getTargetDefinition().getPosition(), 
                flight.getSquadron().determineSide());
        
        Coordinate flightHomeCoordinates = flight.getSquadron().determineCurrentPosition(campaign.getDate());
        double angleFromFrontToHome = MathUtils.calcAngle(closestFrontLinesToTarget, flightHomeCoordinates);

        int distanceBehindFrontForIngress = getDistanceFromFront();
        Coordinate ingressCoordinate = MathUtils.calcNextCoord(closestFrontLinesToTarget, angleFromFrontToHome, distanceBehindFrontForIngress);
        return ingressCoordinate;
    }

    private Coordinate getBestIngressPositionForPatrol() throws PWCGException 
    {
        FrontLinesForMap frontLinesForMap =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());

        Coordinate closestFrontLinesToBase = frontLinesForMap.findClosestFrontCoordinateForSide(
                flight.getSquadron().determineCurrentPosition(campaign.getDate()), 
                flight.getSquadron().determineSide());
        
        Coordinate closestFriendlyFrontLinesToTarget = frontLinesForMap.findClosestFrontCoordinateForSide(
                flight.getTargetDefinition().getPosition(), 
                flight.getSquadron().determineSide());

        double distanceBetweenBaseAndTarget = MathUtils.calcDist(closestFrontLinesToBase, closestFriendlyFrontLinesToTarget);
        double angleBetweenbaseAndTarget = MathUtils.calcAngle(closestFrontLinesToBase, closestFriendlyFrontLinesToTarget);
        Coordinate positionBetweenbaseAndTarget = MathUtils.calcNextCoord(closestFrontLinesToBase, angleBetweenbaseAndTarget, (distanceBetweenBaseAndTarget / 2));
        
        Coordinate flightHomeCoordinates = flight.getSquadron().determineCurrentPosition(campaign.getDate());
        double angleFromFrontToHome = MathUtils.calcAngle(positionBetweenbaseAndTarget, flightHomeCoordinates);

        int distanceBehindFrontForIngress = getDistanceFromFront();
        Coordinate ingressCoordinate = MathUtils.calcNextCoord(positionBetweenbaseAndTarget, angleFromFrontToHome, distanceBehindFrontForIngress);
        return ingressCoordinate;
    }

    private Coordinate getBestIngressPositionBehindFriendlyLines() throws PWCGException 
    {
        FrontLinesForMap frontLinesForMap =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        Coordinate closestEnemyFrontLinesToTarget = frontLinesForMap.findClosestFrontCoordinateForSide(
                flight.getTargetDefinition().getPosition(), 
                flight.getSquadron().determineSide().getOppositeSide());
        Coordinate closestFriendlyFrontLinesToTarget = frontLinesForMap.findClosestFrontCoordinateForSide(
                flight.getTargetDefinition().getPosition(), 
                flight.getSquadron().determineSide());
        int distanceBehindFrontForIngress = getDistanceFromFront();

        return BehindFriendlyLinesPositionCalculator.getPointBehindFriendlyLines(
                closestEnemyFrontLinesToTarget, 
                closestFriendlyFrontLinesToTarget, 
                distanceBehindFrontForIngress, 
                campaign.getDate(), 
                flight.getSquadron().determineSide());
    }

    private Coordinate getBestIngressPositionForEscortRendezvous() throws PWCGException 
    {
        FrontLinesForMap frontLinesForMap =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        Coordinate closestEnemyFrontLinesToTarget = frontLinesForMap.findClosestFrontCoordinateForSide(
                flight.getTargetDefinition().getPosition(), 
                flight.getSquadron().determineSide().getOppositeSide());
        int distanceBehindFrontForIngress = getDistanceFromFront();

        return BehindFriendlyLinesPositionCalculator.getPointBehindFriendlyLines(
                closestEnemyFrontLinesToTarget, 
                flight.getFlightHomePosition(), 
                distanceBehindFrontForIngress, 
                campaign.getDate(), 
                flight.getSquadron().determineSide());
    }

    private int getDistanceFromFront()
    {
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int ingressDistanceFromFront = productSpecificConfiguration.getDefaultIngressDistanceFromFront();
        return ingressDistanceFromFront;
    }
}
