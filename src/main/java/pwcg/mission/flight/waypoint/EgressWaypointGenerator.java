package pwcg.mission.flight.waypoint;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.McuWaypoint;

public class EgressWaypointGenerator
{

    public static McuWaypoint createEgressWaypoint(Flight flight, Coordinate ingressWaypointCoord) throws PWCGException  
    {
        Coordinate egressCoord = createEgressCoordinates(flight, ingressWaypointCoord);

        McuWaypoint egressWP = WaypointFactory.createEgressWaypointType();
        egressWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        egressWP.setDesc(flight.getSquadron().determineDisplayName(flight.getCampaign().getDate()), WaypointType.EGRESS_WAYPOINT.getName());
        egressWP.setSpeed(flight.getFlightCruisingSpeed());
        egressWP.setPosition(egressCoord);
        
       return egressWP;
    }

    public static Coordinate createEgressCoordinates(Flight flight, Coordinate ingressWaypointCoord) throws PWCGException
    {
        double angleFromFieldToFront = MathUtils.calcAngle(ingressWaypointCoord, flight.getPosition());
        int headingOffsetFromIngress = 90 - RandomNumberGenerator.getRandom(180);
        double angleOffsetFromIngress = MathUtils.adjustAngle(angleFromFieldToFront, headingOffsetFromIngress);
        Coordinate egressCoord = MathUtils.calcNextCoord(ingressWaypointCoord, angleOffsetFromIngress, 5000);
        egressCoord.setYPos(flight.getFlightInformation().getAltitude());

        return egressCoord;
    }

}
