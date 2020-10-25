package pwcg.mission.flight.waypoint.virtual;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.BaseFlightMcu;
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

    private static void linkVirtualWaypointToTarget(IFlight flight, IVirtualWaypoint virtualWaypoint)
    {
        BaseFlightMcu attackMcu = flight.getWaypointPackage().getAttackFlightPoint(); 
        if (attackMcu != null)
        {
            attackMcu.setObject(virtualWaypoint.getVwpPlanes().getLeadActivatePlane().getLinkTrId());
        }
        else
        {
            System.out.println("Did not find attack MCU");
        }
    }

    private static void adjustPayloadForSpawnAfterTarget(IFlight flight, IVirtualWaypoint virtualWaypoint) throws PWCGException
    {
        for (PlaneMcu plane : virtualWaypoint.getVwpPlanes().getAllPlanes())
        {
            plane.noOrdnance();
        }
    }
}
