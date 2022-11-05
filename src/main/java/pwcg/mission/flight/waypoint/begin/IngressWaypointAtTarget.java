package pwcg.mission.flight.waypoint.begin;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuWaypoint;

public class IngressWaypointAtTarget
{
    private IFlight flight;

    public IngressWaypointAtTarget(IFlight flight) throws PWCGException 
    {
        this.flight = flight;
    }

    public McuWaypoint createIngressWaypoint(double distanceFromTarget) throws PWCGException  
    {
        Coordinate ingressCoords = getIngressWaypointAtTarget(distanceFromTarget);
        ingressCoords.setYPos(flight.getFlightInformation().getAltitude());

        McuWaypoint ingressWP = WaypointFactory.createIngressWaypointType();
        ingressWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        ingressWP.setSpeed(flight.getFlightCruisingSpeed());
        ingressWP.setPosition(ingressCoords);   
        ingressWP.setTargetWaypoint(false);
        
        return ingressWP;
    }

    private Coordinate getIngressWaypointAtTarget(double distanceFromTarget) throws PWCGException 
    {                
        double angleFromTarget = MathUtils.calcAngle(flight.getTargetDefinition().getPosition(), flight.getFlightHomePosition());
        Coordinate ingressCoordinate = MathUtils.calcNextCoord(flight.getCampaignMap(), flight.getTargetDefinition().getPosition(), angleFromTarget, distanceFromTarget);
        return ingressCoordinate;
    }
}
