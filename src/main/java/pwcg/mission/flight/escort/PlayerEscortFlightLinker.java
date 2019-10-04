package pwcg.mission.flight.escort;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerEscortFlightLinker
{
    private PlayerEscortFlight escortFlight = null;
    private Flight escortedFlight = null;

    public PlayerEscortFlightLinker(PlayerEscortFlight escortFlight, Flight escortedFlight)
    {
        this.escortFlight = escortFlight;;
        this.escortedFlight = escortedFlight;
    }

    public void linkEscortedFlight() throws PWCGException
    {
        linkRendezvousWPToCover();
        linkIngressToRendezvous();
        createSeparationTargetAssociations();
        linkCover();
    }

    private void linkRendezvousWPToCover() throws PWCGException
    {
        McuWaypoint rendezvousWP = escortFlight.getWaypointPackage().getWaypointByActionForLeadPlaneWithFailure(WaypointAction.WP_ACTION_RENDEZVOUS);
        McuWaypoint escortedAirStartWP = escortedFlight.getAllFlightWaypoints().get(0);
        rendezvousWP.setTarget(escortFlight.getCoverTimer().getIndex());
        escortFlight.getCoverTimer().setTarget(escortFlight.getEscortedFlightWaypointTimer().getIndex());
        escortFlight.getEscortedFlightWaypointTimer().setTarget(escortedAirStartWP.getIndex());
    }

    private void linkIngressToRendezvous()
    {
        McuWaypoint prevWP = null;
        for (McuWaypoint nextWP : escortFlight.getWaypointPackage().getWaypointsForLeadPlane())
        {
            if (prevWP != null)
            {
                prevWP.setTarget(nextWP.getIndex());
                if (nextWP.getName().equals(WaypointType.RENDEZVOUS_WAYPOINT.getName()))
                {
                    break;
                }
            }

            prevWP = nextWP;
        }
    }

    private void createSeparationTargetAssociations()
    {
        boolean separationStarted = false;
        McuWaypoint prevWP = null;
        for (McuWaypoint nextWP : escortFlight.getWaypointPackage().getWaypointsForLeadPlane())
        {
            if (nextWP.getName().equals(WaypointType.EGRESS_WAYPOINT.getName()))
            {
                linkSeparationToEgressWP(nextWP);
                separationStarted = true;
            }
            else if (separationStarted)
            {
                prevWP.setTarget(nextWP.getIndex());
            }

            prevWP = nextWP;
        }
    }

    private void linkSeparationToEgressWP(McuWaypoint nextWP)
    {
        List<McuWaypoint> escortedWaypoints = escortedFlight.getAllFlightWaypoints();
        McuWaypoint escortedEgress = WaypointGeneratorUtils.findWaypointByType(escortedWaypoints, WaypointType.EGRESS_WAYPOINT.getName());
        escortedEgress.setTarget(escortFlight.getForceCompleteTimer().getIndex());
        escortFlight.getForceCompleteTimer().setTarget(escortFlight.getEgressTimer().getIndex());
        escortFlight.getEgressTimer().setTarget(nextWP.getIndex());
    }

    private void linkCover() throws PWCGException
    {
        escortFlight.getCover().setTarget(escortedFlight.getPlanes().get(0).getEntity().getIndex());
    }
}
