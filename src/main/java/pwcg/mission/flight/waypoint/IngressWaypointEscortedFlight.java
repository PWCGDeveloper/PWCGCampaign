package pwcg.mission.flight.waypoint;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.mcu.McuWaypoint;

public class IngressWaypointEscortedFlight extends IngressWaypointBase
{
    public IngressWaypointEscortedFlight(Flight flight, Coordinate lastPosition, Coordinate targetPosition, int waypointSpeed, int waypointAltitude) throws PWCGException 
    {
        super(flight, lastPosition, targetPosition, waypointSpeed, waypointAltitude);
    }

    @Override
    public McuWaypoint createIngressWaypoint() throws PWCGException  
    {        
        Coordinate selectedPosition = ingressCoordinatesForEscortedFlights();
        selectedPosition = ingressNotTooFarFromField(flight, selectedPosition);
        selectedPosition.setYPos(waypointAltitude);

        McuWaypoint ingressWP = WaypointFactory.createIngressWaypointType();
        ingressWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        ingressWP.setSpeed(waypointSpeed);
        ingressWP.setPosition(selectedPosition);
                
        return ingressWP;
    }

    private Coordinate ingressCoordinatesForEscortedFlights() throws PWCGException
    {
        IAirfield playerAirfield = flight.getAirfield(); 
        Coordinate playerFieldPosition = playerAirfield.getPosition().copy();
        FrontLinesForMap frontLines =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        Side escortFlightSide = flight.getSquadron().determineSide();

        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int initialRadius = productSpecific.getInitialTargetRadiusFromGeneralTargetLocation(FlightTypes.BOMB);
        int minDistanceBehindLines = 5000;
        int maxDistanceBehindLines = 15000;
        Coordinate positionBehindFriendlyLines = frontLines.findPositionBehindLinesForSide(playerFieldPosition, initialRadius, minDistanceBehindLines, maxDistanceBehindLines, escortFlightSide);
        return positionBehindFriendlyLines;
    }

    private Coordinate ingressNotTooFarFromField(Flight flight, Coordinate ingressWPCoord) throws PWCGException
    {
        IAirfield playerAirfield = flight.getAirfield(); 
        Coordinate playerFieldPosition = playerAirfield.getPosition().copy();
        double distanceFromField = MathUtils.calcDist(ingressWPCoord, playerFieldPosition);
        if (distanceFromField > 20000)
        {
            double angle = MathUtils.calcAngle(playerFieldPosition, ingressWPCoord);
            ingressWPCoord = MathUtils.calcNextCoord(playerFieldPosition, angle, 20000);
        }
        
        return ingressWPCoord;
    }

}
