package pwcg.mission.flight.waypoint.begin;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuWaypoint;

public class IngressWaypointNearField implements IIngressWaypoint
{
    private IFlight flight;

    public IngressWaypointNearField(IFlight flight) throws PWCGException 
    {
        this.flight = flight;
    }

    public McuWaypoint createIngressWaypoint() throws PWCGException  
    {
        Coordinate ingressCoords = getIngressWaypointNearField();
        ingressCoords.setYPos(flight.getFlightInformation().getAltitude());

        McuWaypoint ingressWP = WaypointFactory.createIngressWaypointType();
        ingressWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        ingressWP.setSpeed(flight.getFlightPlanes().getFlightCruisingSpeed());
        ingressWP.setPosition(ingressCoords);   
        ingressWP.setTargetWaypoint(false);
        
        return ingressWP;
    }

    private Coordinate getIngressWaypointNearField() throws PWCGException 
    {
        double angleToTarget = MathUtils.calcAngle(flight.getFlightHomePosition(), flight.getTargetDefinition().getTargetPosition());
        double distanceToTarget = MathUtils.calcDist(flight.getFlightHomePosition(), flight.getTargetDefinition().getTargetPosition());
        double distanceToIngress = distanceToTarget;
        if (distanceToTarget > 12000)
        {
            distanceToIngress = 8000.0;
        }
        else
        {
            distanceToIngress = distanceToTarget / 2;
        }
        
        Coordinate ingressCoordinate = MathUtils.calcNextCoord(flight.getFlightHomePosition(), angleToTarget, distanceToIngress);
        return ingressCoordinate;
    }
}
