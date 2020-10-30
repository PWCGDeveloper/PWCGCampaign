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
                virtualWaypointIsLinkedToRealWaypoint = IndexLinkValidator.isIndexInTargetList(
                        activateContainer.getWaypointTimer().getTargets(), flightPoint.getIndex());
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
                virtualWaypointIsLinkedToRealWaypoint = IndexLinkValidator.isIndexInTargetList(flightPoint.getObjects(), virtualFlightLeader.getLinkTrId());
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
            boolean virtualWaypointIsLinkedToFormation = IndexLinkValidator.isIndexInTargetList(
                    activateContainer.getFormation().getObjects(), virtualFlightLeader.getLinkTrId());
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
                boolean virtualPlaneIsActivated = IndexLinkValidator.isIndexInTargetList(
                        activateContainer.getActivate().getObjects(), virtualPlane.getLinkTrId());
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
                IndexLinkValidator.isIndexInTargetList(
                        previousVwp.getVwpCheckZone().getTimedCheckZone().getTimedOutExternal().getTargets(),
                        virtualWayPoint.getVwpCheckZone().getTimedCheckZone().getActivateEntryPoint());
            }
            previousVwp = virtualWayPoint;
        }
    }
}
