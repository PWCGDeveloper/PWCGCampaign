package pwcg.mission.flight.waypoint.end;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class EgressWaypointGenerator
{

    public static McuWaypoint createEgressWaypoint(IFlight flight, Coordinate ingressWaypointCoord) throws PWCGException  
    {
        Coordinate egressCoord = createEgressCoordinates(flight, ingressWaypointCoord);

        McuWaypoint egressWP = WaypointFactory.createEgressWaypointType();
        egressWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        egressWP.setDesc(flight.getFlightInformation().getFlightName(), WaypointType.EGRESS_WAYPOINT.getName());
        egressWP.setSpeed(flight.getFlightCruisingSpeed());
        egressWP.setPosition(egressCoord);
        
       return egressWP;
    }

    public static Coordinate createEgressCoordinates(IFlight flight, Coordinate ingressWaypointCoord) throws PWCGException
    {
        double angleFromFieldToFront = MathUtils.calcAngle(ingressWaypointCoord, flight.getFlightInformation().getHomePosition());
        int headingOffsetFromIngress = 90 - RandomNumberGenerator.getRandom(180);
        double angleOffsetFromIngress = MathUtils.adjustAngle(angleFromFieldToFront, headingOffsetFromIngress);
        Coordinate egressCoord = MathUtils.calcNextCoord(ingressWaypointCoord, angleOffsetFromIngress, 5000);
        egressCoord.setYPos(flight.getFlightInformation().getAltitude());
        if (egressCoord.getYPos() < 500)
        {
            egressCoord.setYPos(500);
        }

        return egressCoord;
    }

}
