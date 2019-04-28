package pwcg.mission.flight.waypoint;

import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.McuWaypoint;

public class IngressWaypointNearFront extends IngressWaypointBase
{
    public IngressWaypointNearFront(Flight flight, Coordinate lastPosition, Coordinate targetPosition, int waypointSpeed, int waypointAltitude) throws PWCGException 
    {
        super(flight, lastPosition, targetPosition, waypointSpeed, waypointAltitude);
    }

    public McuWaypoint createIngressWaypoint() throws PWCGException  
    {
        Coordinate groundIngressCoords = getBestIngressPositionToFront();
        
        Coordinate coord = new Coordinate();
        coord.setXPos(groundIngressCoords.getXPos());
        coord.setZPos(groundIngressCoords.getZPos());
        coord.setYPos(waypointAltitude);

        McuWaypoint ingressWP = WaypointFactory.createIngressWaypointType();
        ingressWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        ingressWP.setSpeed(waypointSpeed);
        ingressWP.setPosition(coord);   
        ingressWP.setTargetWaypoint(false);
        
        return ingressWP;
    }

    private Coordinate getBestIngressPositionToFront() throws PWCGException 
    {
        FrontLinesForMap frontLinesForMap =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        Coordinate closestFrontLinesToTarget = frontLinesForMap.findClosestFrontCoordinateForSide(targetPosition, campaign.determineCountry().getSide());
        
        Coordinate flightHomeCoordinates = flight.getSquadron().determineCurrentPosition(campaign.getDate());
        double angleFromFrontToHome = MathUtils.calcAngle(closestFrontLinesToTarget, flightHomeCoordinates);
        
        Coordinate ingressCoordinate = MathUtils.calcNextCoord(closestFrontLinesToTarget, angleFromFrontToHome, WaypointGeneratorBase.INGRESS_DISTANCE_FROM_FRONT);
        return ingressCoordinate;
    }
}
