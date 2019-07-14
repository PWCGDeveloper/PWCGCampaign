package pwcg.mission.flight;

import pwcg.mission.flight.escort.EscortForPlayerFlight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class EscortForPlayerValidator
{
    public static void validateEscortForPlayer(Flight flight)
    {
        EscortForPlayerFlight escortForPlayerFlight = flight.getEscortForPlayer();
        assert(escortForPlayerFlight != null);

        PlaneMCU leadEscortedPlane = flight.getPlanes().get(0);
        assert(escortForPlayerFlight.getCover().getTargets().get(0).equals(new Integer(leadEscortedPlane.getEntity().getIndex()).toString()));

        McuWaypoint ingressWP = WaypointGeneratorBase.findWaypointByType(flight.getAllWaypoints(), WaypointType.INGRESS_WAYPOINT.getName());
        PlaneMCU leadEscortPlane = escortForPlayerFlight.getPlanes().get(0);
        assert(leadEscortPlane.getPosition().getXPos() == ingressWP.getPosition().getXPos());
        assert(leadEscortPlane.getPosition().getZPos() == ingressWP.getPosition().getZPos());
        assert(leadEscortPlane.getPosition().getYPos() > ingressWP.getPosition().getYPos());
    }
}
