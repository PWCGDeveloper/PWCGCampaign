package pwcg.mission.flight.waypoint.virtual;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.group.virtual.IVirtualWaypoint;

public class VirtualWaypointAttackFlightResolver
{

    public static void resolveForAttackFlight(IFlight flight, VirtualWaypointPackage vwpPackage) throws PWCGException
    {
        
        for (IVirtualWaypoint virtualWaypoint : vwpPackage.getVirtualWaypoints())
        {
            if (virtualWaypoint.isShouldLinkToAttack())
            {
                linkVirtualWaypointToTarget(flight, virtualWaypoint);
            }
            else
            {
                adjustPayloadForSpawnAfterTarget(flight, virtualWaypoint);
            }
        }
    }

    private static void linkVirtualWaypointToTarget(IFlight flight, IVirtualWaypoint virtualWaypoint) throws PWCGException
    {
        flight.getWaypointPackage().setAttackToTriggerOnPlane(virtualWaypoint.getVwpPlanes().getLeadActivatePlane().getLinkTrId());
    }

    private static void adjustPayloadForSpawnAfterTarget(IFlight flight, IVirtualWaypoint virtualWaypoint) throws PWCGException
    {
        for (PlaneMcu plane : virtualWaypoint.getVwpPlanes().getAllPlanes())
        {
            plane.noOrdnance();
        }
    }
}
