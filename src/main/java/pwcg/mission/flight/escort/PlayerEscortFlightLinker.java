package pwcg.mission.flight.escort;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerEscortFlightLinker
{
    protected PlayerEscortFlight escortFlight = null;
    protected Flight escortedFlight = null;

    public PlayerEscortFlightLinker(PlayerEscortFlight escortFlight, Flight escortedFlight)
    {
        this.escortFlight = escortFlight;;
        this.escortedFlight = escortedFlight;;
    }

    public void linkEscortedFlight() throws PWCGException
    {
        createRendezvousTargetAssociations();
        createSeparationTargetAssociations();
        linkCover();
    }

    private void createRendezvousTargetAssociations()
    {
        McuWaypoint prevWP = null;
        for (McuWaypoint nextWP : escortFlight.getWaypointPackage().getWaypointsForLeadPlane())
        {
            if (nextWP.getName().equals(WaypointType.RENDEZVOUS_WAYPOINT.getName()))
            {
                linkRendezvousWPToCover(nextWP);
                prevWP.setTarget(nextWP.getIndex());
                break;
            }

            if (prevWP != null)
            {
                prevWP.setTarget(nextWP.getIndex());
            }

            prevWP = nextWP;
        }
    }

    private void linkRendezvousWPToCover(McuWaypoint rendezvousWP)
    {
        McuWaypoint escortedIngressWP = escortedFlight.getAllWaypoints().get(0);
        rendezvousWP.setTarget(escortFlight.getCoverTimer().getIndex());
        escortFlight.getCoverTimer().setTarget(escortFlight.getEscortedFlightWaypointTimer().getIndex());
        escortFlight.getEscortedFlightWaypointTimer().setTarget(escortedIngressWP.getIndex());
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
        List<McuWaypoint> escortedWaypoints = escortedFlight.getAllWaypoints();
        McuWaypoint escortedEgress = WaypointGeneratorBase.findWaypointByType(escortedWaypoints, WaypointType.EGRESS_WAYPOINT.getName());
        escortedEgress.setTarget(escortFlight.getForceCompleteTimer().getIndex());
        escortFlight.getForceCompleteTimer().setTarget(escortFlight.getEgressTimer().getIndex());
        escortFlight.getEgressTimer().setTarget(nextWP.getIndex());
    }

    private void linkCover() throws PWCGException
    {
        escortFlight.getCover().setTarget(escortedFlight.getPlanes().get(0).getEntity().getIndex());
    }
}
