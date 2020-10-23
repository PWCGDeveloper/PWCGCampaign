package pwcg.mission.mcu.group.virtual;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.validate.IndexLinkValidator;
import pwcg.mission.flight.waypoint.IWaypointPackage;
import pwcg.mission.mcu.BaseFlightMcu;

public class VirtualWaypointPackageValidator
{
    private Mission mission;

    public VirtualWaypointPackageValidator(Mission mission)
    {
        this.mission = mission;
    }

    public void validate() throws PWCGException
    {
        for (IFlight flight : mission.getMissionFlightBuilder().getAllAerialFlights())
        {
            validateIfVirtual(flight);
        }
    }

    private void validateIfVirtual(IFlight flight) throws PWCGException
    {
        if (flight.getFlightInformation().isVirtual())
        {
            verifyEveryVwpLinkedToWaypoint(flight);
            verifyEveryVirtualFlightLeaderLinkedToWaypoint(flight);
            verifyEveryVirtualFlightLeaderLinkedToFormation(flight);
            verifyEveryVirtualPlaneIsActivated(flight);
            verifyEveryVwpLinkedToNextVwp(flight);
        }
    }

    private void verifyEveryVwpLinkedToWaypoint(IFlight flight)
    {
        IWaypointPackage waypointPackage = flight.getWaypointPackage();
        boolean virtualWaypointIsLinkedToRealWaypoint = false;
        for (IVirtualWaypoint virtualWayPoint : flight.getVirtualWaypointPackage().getVirtualWaypoints())
        {
            VirtualWaypointTriggered activateContainer = virtualWayPoint.getVwpTriggered();
            for (BaseFlightMcu flightPoint : waypointPackage.getAllFlightPoints())
            {
                virtualWaypointIsLinkedToRealWaypoint = IndexLinkValidator.isIndexInTargetList(flightPoint.getIndex(),
                        activateContainer.getWaypointTimer().getTargets());
                if (virtualWaypointIsLinkedToRealWaypoint)
                {
                    break;
                }
            }
            assert (virtualWaypointIsLinkedToRealWaypoint);
        }
    }

    private void verifyEveryVirtualFlightLeaderLinkedToWaypoint(IFlight flight) throws PWCGException
    {
        IWaypointPackage waypointPackage = flight.getWaypointPackage();
        boolean virtualWaypointIsLinkedToRealWaypoint = false;
        for (IVirtualWaypoint virtualWayPoint : flight.getVirtualWaypointPackage().getVirtualWaypoints())
        {
            PlaneMcu virtualFlightLeader = virtualWayPoint.getVwpFlightLeader();
            for (BaseFlightMcu flightPoint : waypointPackage.getAllFlightPoints())
            {
                virtualWaypointIsLinkedToRealWaypoint = IndexLinkValidator.isIndexInTargetList(virtualFlightLeader.getLinkTrId(), flightPoint.getObjects());
            }
            assert (virtualWaypointIsLinkedToRealWaypoint);
        }
    }

    private void verifyEveryVirtualFlightLeaderLinkedToFormation(IFlight flight) throws PWCGException
    {
        for (IVirtualWaypoint virtualWayPoint : flight.getVirtualWaypointPackage().getVirtualWaypoints())
        {
            VirtualWaypointTriggered activateContainer = virtualWayPoint.getVwpTriggered();
            PlaneMcu virtualFlightLeader = virtualWayPoint.getVwpFlightLeader();
            boolean virtualWaypointIsLinkedToFormation = IndexLinkValidator.isIndexInTargetList(virtualFlightLeader.getLinkTrId(),
                    activateContainer.getFormation().getObjects());
            assert (virtualWaypointIsLinkedToFormation);
        }
    }

    private void verifyEveryVirtualPlaneIsActivated(IFlight flight) throws PWCGException
    {
        for (IVirtualWaypoint virtualWayPoint : flight.getVirtualWaypointPackage().getVirtualWaypoints())
        {
            VirtualWaypointTriggered activateContainer = virtualWayPoint.getVwpTriggered();
            for (PlaneMcu virtualPlane : virtualWayPoint.getVwpPlanes().getAllPlanes())
            {
                boolean virtualPlaneIsActivated = IndexLinkValidator.isIndexInTargetList(virtualPlane.getLinkTrId(),
                        activateContainer.getActivate().getObjects());
                assert (virtualPlaneIsActivated);
            }
        }
    }

    private void verifyEveryVwpLinkedToNextVwp(IFlight flight) throws PWCGException
    {
        IVirtualWaypoint previousVwp = null;
        for (IVirtualWaypoint virtualWayPoint : flight.getVirtualWaypointPackage().getVirtualWaypoints())
        {
            if (previousVwp != null)
            {
                IndexLinkValidator.isIndexInTargetList(virtualWayPoint.getVwpCheckZone().getVwpStartTimer().getIndex(),
                        previousVwp.getVwpNextVwpStart().getStartNextWaypointTriggeredTimer().getTargets());
                
                IndexLinkValidator.isIndexInTargetList(virtualWayPoint.getVwpUpstreamKill().getUpstreamKillTimer().getIndex(),
                        previousVwp.getVwpUpstreamKill().getUpstreamKillTimer().getTargets());
                
                IndexLinkValidator.isIndexInTargetList(virtualWayPoint.getVwpUpstreamKill().getUpstreamKillTimer().getIndex(),
                        previousVwp.getVwpTriggered().getDeleteUpstreamPlanesTimer().getTargets());
            }
            previousVwp = virtualWayPoint;
        }
    }
}
