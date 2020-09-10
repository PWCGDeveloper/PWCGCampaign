package pwcg.mission.flight.validate;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.IWaypointPackage;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointFlightActivate;
import pwcg.mission.flight.waypoint.virtual.VirtualWaypointPackage;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.group.IVirtualWaypoint;
import pwcg.mission.mcu.group.ActivateContainer;

public class VirtualWaypointPackageValidator
{
    private Mission mission;
    private VirtualWaypointPackage virtualWaypointPackage;
    
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
            this.virtualWaypointPackage = (VirtualWaypointPackage) flight.getVirtualWaypointPackage();
            validateVirtualWaypointPackage(flight);
            verifyEveryWaypointHasVwp(flight);
            verifyVwpLinkedToActivate(flight);
        }
    }

    private void validateVirtualWaypointPackage(IFlight flight) throws PWCGException
    {        
        IVirtualWaypoint previousVirtualWayPoint = null;
        for (IVirtualWaypoint virtualWayPoint : virtualWaypointPackage.getVirtualWaypoints())
        {
            
            verifyTargetAssociationsTriggered(virtualWayPoint);
            verifyTargetAssociationsTimedOut(virtualWayPoint, previousVirtualWayPoint);
            verifyTargetAssociationsForPlaneLimitReached(virtualWayPoint);
            verifySpawnContainers(flight, virtualWayPoint);            
            previousVirtualWayPoint = virtualWayPoint;
        }
    }
    
    private void verifyTargetAssociationsTimedOut(IVirtualWaypoint virtualWayPoint, IVirtualWaypoint previousVirtualWayPoint)
    {
        assert(IndexLinkValidator.isIndexInTargetList(virtualWayPoint.getVwpTimedOutTimer().getIndex(), virtualWayPoint.getKillVwpTimer().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(virtualWayPoint.getInitiateNextVwpTimer().getIndex(), virtualWayPoint.getVwpTimedOutTimer().getTargets()));
        
        if (previousVirtualWayPoint != null)
        {
            assert(IndexLinkValidator.isIndexInTargetList(virtualWayPoint.getKillVwpTimer().getIndex(), previousVirtualWayPoint.getInitiateNextVwpTimer().getTargets()));
        }
    }

    private void verifyTargetAssociationsTriggered(IVirtualWaypoint virtualWayPoint)
    {
        assert(IndexLinkValidator.isIndexInTargetList(virtualWayPoint.getCheckZone().getActivateEntryPoint(), virtualWayPoint.getKillVwpTimer().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(virtualWayPoint.getVwpTriggeredTimer().getIndex(), virtualWayPoint.getCheckZone().getCheckZone().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(virtualWayPoint.getStopNextVwp().getIndex(), virtualWayPoint.getVwpTriggeredTimer().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(virtualWayPoint.getMasterVwpTimer().getIndex(), virtualWayPoint.getVwpTriggeredTimer().getTargets()));
    }
    
    private void verifyTargetAssociationsForPlaneLimitReached(IVirtualWaypoint virtualWayPoint)
    {
        assert(IndexLinkValidator.isIndexInTargetList(virtualWayPoint.getStopNextVwp().getIndex(), virtualWayPoint.getKillVwpTimer().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(virtualWayPoint.getCheckZone().getDeactivateEntryPoint(), virtualWayPoint.getKillVwpTimer().getTargets()));
    }
    
    private void verifySpawnContainers (IFlight flight, IVirtualWaypoint virtualWayPoint)
    {
        ActivateContainer activateContainer = virtualWayPoint.getActivateContainer();
        
        assert(IndexLinkValidator.isIndexInTargetList(activateContainer.getEntryPoint(), virtualWayPoint.getMasterVwpTimer().getTargets()));

        assert(IndexLinkValidator.isIndexInTargetList(activateContainer.getFormationTimer().getIndex(), activateContainer.getActivateTimer().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(activateContainer.getWpActivateTimer().getIndex(), activateContainer.getFormationTimer().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(activateContainer.getPlaneAttackTimer().getIndex(), activateContainer.getWpActivateTimer().getTargets()));

        assert(IndexLinkValidator.isIndexInTargetList(activateContainer.getActivate().getIndex(), activateContainer.getActivateTimer().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(activateContainer.getFormation().getIndex(), activateContainer.getFormationTimer().getTargets()));
        assert(activateContainer.getPlaneAttackTimer().getTargets().size() > 0);
        assert(activateContainer.getWpActivateTimer().getTargets().size() == 2);
    }
    
    private void verifyEveryWaypointHasVwp(IFlight flight)
    {
        IWaypointPackage waypointPackage = flight.getVirtualWaypointPackage().getWaypointsForPlane(activateContainer.getP.getIndex());
        boolean isFirstLinkedToRealWaypoint = false;
        boolean virtualWaypointIsLinkedToRealWaypoint = false;
        for (IVirtualWaypoint virtualWayPoint : flight.getVirtualWaypointPackage().getVirtualWaypoints())
        {
            for (BaseFlightMcu flightPoint : waypointPackage.getAllFlightPoints())
            {
                ActivateContainer activateContainer = virtualWayPoint.getVwpSpawnContainerForPlane(plane.getIndex());
                virtualWaypointIsLinkedToRealWaypoint = IndexLinkValidator.isIndexInTargetList(flightPoint.getIndex(), activateContainer.getWpActivateTimer().getTargets());
                if (virtualWaypointIsLinkedToRealWaypoint)
                {
                    isFirstLinkedToRealWaypoint = true;
                    break;
                }
            }
            
            if (isFirstLinkedToRealWaypoint)
            {
                assert(virtualWaypointIsLinkedToRealWaypoint);
            }
        }
    }
    
    private void verifyVwpLinkedToActivate(IFlight flight) throws PWCGException
    {
        VirtualWaypointPackage virtualWaypointPackage = (VirtualWaypointPackage)flight.getVirtualWaypointPackage();
        MissionPointFlightActivate virtualFlightActivate = (MissionPointFlightActivate)virtualWaypointPackage.getDuplicatedWaypointSet().getActivateMissionPointSet();
        McuTimer activateTimer = virtualFlightActivate.getActivationTimer();
        
        this.virtualWaypointPackage = (VirtualWaypointPackage) flight.getVirtualWaypointPackage();
        IVirtualWaypoint firstVirtualWaypoint = virtualWaypointPackage.getVirtualWaypoints().get(0);

        assert(IndexLinkValidator.isIndexInTargetList(firstVirtualWaypoint.getKillVwpTimer().getIndex(), activateTimer.getTargets()));
    }
}
