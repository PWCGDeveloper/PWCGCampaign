package pwcg.mission.flight.validate;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.IWaypointPackage;
import pwcg.mission.flight.waypoint.VirtualWaypointPackage;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointFlightActivate;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.group.VirtualWayPoint;
import pwcg.mission.mcu.group.VwpSpawnContainer;

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
        VirtualWayPoint previousVirtualWayPoint = null;
        for (VirtualWayPoint virtualWayPoint : virtualWaypointPackage.getVirtualWaypoints())
        {
            
            verifyTargetAssociationsTriggered(virtualWayPoint);
            verifyTargetAssociationsTimedOut(virtualWayPoint, previousVirtualWayPoint);
            verifyTargetAssociationsForPlaneLimitReached(virtualWayPoint);
            verifySpawnContainers(flight, virtualWayPoint);            
            previousVirtualWayPoint = virtualWayPoint;
        }
    }
    
    private void verifyTargetAssociationsTimedOut(VirtualWayPoint virtualWayPoint, VirtualWayPoint previousVirtualWayPoint)
    {
        assert(IndexLinkValidator.isIndexInTargetList(virtualWayPoint.getVwpTimedOutTimer().getIndex(), virtualWayPoint.getVwpTimer().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(virtualWayPoint.getInitiateNextVirtualWaypointTimer().getIndex(), virtualWayPoint.getVwpTimedOutTimer().getTargets()));
        
        if (previousVirtualWayPoint != null)
        {
            assert(IndexLinkValidator.isIndexInTargetList(virtualWayPoint.getVwpTimer().getIndex(), previousVirtualWayPoint.getInitiateNextVirtualWaypointTimer().getTargets()));
        }
    }

    private void verifyTargetAssociationsTriggered(VirtualWayPoint virtualWayPoint)
    {
        assert(IndexLinkValidator.isIndexInTargetList(virtualWayPoint.getCheckZone().getActivateEntryPoint(), virtualWayPoint.getVwpTimer().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(virtualWayPoint.getVwpTriggeredTimer().getIndex(), virtualWayPoint.getCheckZone().getCheckZone().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(virtualWayPoint.getStopNextVwp().getIndex(), virtualWayPoint.getVwpTriggeredTimer().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(virtualWayPoint.getMasterSpawnTimer().getIndex(), virtualWayPoint.getVwpTriggeredTimer().getTargets()));
    }
    
    private void verifyTargetAssociationsForPlaneLimitReached(VirtualWayPoint virtualWayPoint)
    {
        assert(IndexLinkValidator.isIndexInTargetList(virtualWayPoint.getStopNextVwp().getIndex(), virtualWayPoint.getKillVwpTimer().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(virtualWayPoint.getCheckZone().getDeactivateEntryPoint(), virtualWayPoint.getKillVwpTimer().getTargets()));
    }
    
    private void verifySpawnContainers (IFlight flight, VirtualWayPoint virtualWayPoint)
    {
        VwpSpawnContainer previousSpawnContainer = null;
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            VwpSpawnContainer spawnContainer = virtualWayPoint.getVwpSpawnContainerForPlane(plane.getIndex());
            
            assert(IndexLinkValidator.isIndexInTargetList(spawnContainer.getEntryPoint(), virtualWayPoint.getMasterSpawnTimer().getTargets()) || 
                   IndexLinkValidator.isIndexInTargetList(spawnContainer.getEntryPoint(), previousSpawnContainer.getSpawnTimer().getTargets()));
            
            assert(IndexLinkValidator.isIndexInTargetList(spawnContainer.getSpawner().getIndex(), spawnContainer.getSpawnTimer().getTargets()));
            assert(IndexLinkValidator.isIndexInTargetList(spawnContainer.getWpActivateTimer().getIndex(), spawnContainer.getSpawnTimer().getTargets()));
            assert(IndexLinkValidator.isIndexInTargetList(spawnContainer.getWaypoint().getIndex(), spawnContainer.getWpActivateTimer().getTargets()));
            assert(IndexLinkValidator.isIndexInTargetList(plane.getAttackTimer().getIndex(), spawnContainer.getWpActivateTimer().getTargets()));

            assert(IndexLinkValidator.isIndexInTargetList(plane.getLinkTrId(), spawnContainer.getSpawner().getObjects()));

            previousSpawnContainer = spawnContainer;
        }
    }
    
    private void verifyEveryWaypointHasVwp(IFlight flight)
    {
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            IWaypointPackage waypointPackage = flight.getVirtualWaypointPackage().getWaypointsForPlane(plane.getIndex());
            boolean isFirstLinkedToRealWaypoint = false;
            boolean virtualWaypointIsLinkedToRealWaypoint = false;
            for (VirtualWayPoint virtualWayPoint : flight.getVirtualWaypointPackage().getVirtualWaypoints())
            {
                for (BaseFlightMcu flightPoint : waypointPackage.getAllFlightPoints())
                {
                    VwpSpawnContainer spawnContainer = virtualWayPoint.getVwpSpawnContainerForPlane(plane.getIndex());
                    virtualWaypointIsLinkedToRealWaypoint = IndexLinkValidator.isIndexInTargetList(flightPoint.getIndex(), spawnContainer.getWpActivateTimer().getTargets());
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
    }
    
    private void verifyVwpLinkedToActivate(IFlight flight) throws PWCGException
    {
        VirtualWaypointPackage virtualWaypointPackage = (VirtualWaypointPackage)flight.getVirtualWaypointPackage();
        MissionPointFlightActivate virtualFlightActivate = (MissionPointFlightActivate)virtualWaypointPackage.getDuplicatedWaypointSet().getActivateMissionPointSet();
        McuTimer activateTimer = virtualFlightActivate.getActivationTimer();
        
        this.virtualWaypointPackage = (VirtualWaypointPackage) flight.getVirtualWaypointPackage();
        VirtualWayPoint firstVirtualWaypoint = virtualWaypointPackage.getVirtualWaypoints().get(0);

        assert(IndexLinkValidator.isIndexInTargetList(firstVirtualWaypoint.getVwpTimer().getIndex(), activateTimer.getTargets()));
    }
}
