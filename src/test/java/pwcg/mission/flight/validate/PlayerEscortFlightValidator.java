package pwcg.mission.flight.validate;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.escort.EscortedByPlayerFlight;
import pwcg.mission.flight.escort.PlayerIsEscortFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointEscortWaypointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;
import pwcg.mission.mcu.McuCover;
import pwcg.mission.mcu.McuForceComplete;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerEscortFlightValidator
{
    private PlayerIsEscortFlight playerFlight;
    private EscortedByPlayerFlight escortedFlight;

    public PlayerEscortFlightValidator(PlayerIsEscortFlight playerFlight)
    {
        this.playerFlight = playerFlight;
    }

    public void validateEscortFlight() throws PWCGException
    {
        assert (playerFlight.getWaypointPackage().getAllWaypoints().size() > 0);
        validateEscortedFlight();
        validateEscortedWaypointLinkage();
        validateEscortWaypointLinkage();
        validateLinkageBetweenFlights();
        validateWaypointTypes();
    }

    private void validateEscortedFlight() throws PWCGException
    {
        for (IFlight unit : playerFlight.getLinkedFlights().getLinkedFlights())
        {
            assert (unit instanceof EscortedByPlayerFlight);
            escortedFlight = (EscortedByPlayerFlight) unit;
        }

        assert (escortedFlight != null);
        assert (escortedFlight.getFlightInformation().isVirtual() == false);
        assert (escortedFlight.getFlightInformation().isAirStart() == true);
        assert (escortedFlight.isPlayerFlight() == false);

        for (PlaneMcu escortedPlane : escortedFlight.getFlightPlanes().getPlanes())
        {
            assert (escortedPlane.getPosition().getYPos() > 1000.0);
        }

        PlaneMcu leadEscortedPlane = escortedFlight.getFlightPlanes().getFlightLeader();
        assert (leadEscortedPlane != null);
    }

    private void validateEscortWaypointLinkage() throws PWCGException
    {
        IMissionPointSet escortMissionPointSetInterface = playerFlight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ESCORT);
        MissionPointEscortWaypointSet escortMissionPointSet = (MissionPointEscortWaypointSet) escortMissionPointSetInterface;
        McuTimer coverTimer = escortMissionPointSet.getEscortSequence().getCoverTimer();
        McuCover cover = escortMissionPointSet.getEscortSequence().getCover();
        McuTimer forceCompleteTimer = escortMissionPointSet.getEscortSequence().getForceCompleteTimer();
        McuTimer escortCompleteTimer = escortMissionPointSet.getEscortSequence().getEscortCompleteTimer();
        McuForceComplete forceComplete = escortMissionPointSet.getEscortSequence().getForceComplete();

        McuWaypoint rendezvousWaypoint = playerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_RENDEZVOUS);
        assert (rendezvousWaypoint != null);

        McuWaypoint egressWaypoint = playerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_EGRESS);
        assert (egressWaypoint != null);

        McuWaypoint prevWaypoint = null;
        for (McuWaypoint waypoint : playerFlight.getWaypointPackage().getAllWaypoints())
        {
            if (prevWaypoint != null)
            {
                boolean isNextWaypointLinked = IndexLinkValidator.isIndexInTargetList(prevWaypoint.getTargets(), waypoint.getIndex());
                if (prevWaypoint.getWpAction().equals(WaypointAction.WP_ACTION_RENDEZVOUS))
                {
                    assert (!isNextWaypointLinked);
                    assert (IndexLinkValidator.isIndexInTargetList(rendezvousWaypoint.getTargets(), coverTimer.getIndex()));
                    assert (IndexLinkValidator.isIndexInTargetList(coverTimer.getTargets(), cover.getIndex()));
                }
                else if (prevWaypoint.getWpAction().equals(WaypointAction.WP_ACTION_EGRESS))
                {
                    assert (isNextWaypointLinked);
                    assert (IndexLinkValidator.isIndexInTargetList(forceCompleteTimer.getTargets(), forceComplete.getIndex()));
                    assert (IndexLinkValidator.isIndexInTargetList(forceCompleteTimer.getTargets(), escortCompleteTimer.getIndex()));
                    assert (IndexLinkValidator.isIndexInTargetList(escortCompleteTimer.getTargets(), egressWaypoint.getIndex()));
                }
                else
                {
                    assert (isNextWaypointLinked);
                }
            }

            prevWaypoint = waypoint;
        }
    }

    private void validateEscortedWaypointLinkage() throws PWCGException
    {
        McuWaypoint escortedAirStartWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_START);
        assert (escortedAirStartWP.getIndex() == escortedFlight.getWaypointPackage().getAllWaypoints().get(0).getIndex());

        McuWaypoint escortedRendezvousWaypoint = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_RENDEZVOUS);
        McuWaypoint escortedIngressWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_INGRESS);
        McuWaypoint escortedTargetWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_TARGET_APPROACH);
        McuWaypoint escortedTargetFinalWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_TARGET_FINAL);

        int escortedWaypointsSize = escortedFlight.getWaypointPackage().getAllWaypoints().size();
        assert (escortedWaypointsSize == 8);

        PlaneMcu leadBomber = escortedFlight.getFlightPlanes().getFlightLeader();
        double distancePlaneToAirStart = MathUtils.calcDist(leadBomber.getPosition(), escortedAirStartWP.getPosition());
        assert (distancePlaneToAirStart < 2000.0);

        double distanceAirStartTORendezvous = MathUtils.calcDist(escortedAirStartWP.getPosition(), escortedRendezvousWaypoint.getPosition());
        assert (distanceAirStartTORendezvous < 4000.0);

        double distanceRendezvousToIngress = MathUtils.calcDist(escortedRendezvousWaypoint.getPosition(), escortedIngressWP.getPosition());
        assert (distanceRendezvousToIngress < 8000.0);

        McuWaypoint prevWaypoint = null;
        for (McuWaypoint waypoint : escortedFlight.getWaypointPackage().getAllWaypoints())
        {
            if (prevWaypoint != null)
            {
                boolean isNextWaypointLinked = IndexLinkValidator.isIndexInTargetList(prevWaypoint.getTargets(), waypoint.getIndex());
                if (prevWaypoint.getWpAction().equals(WaypointAction.WP_ACTION_RENDEZVOUS))
                {
                    assert (IndexLinkValidator.isIndexInTargetList(escortedAirStartWP.getTargets(), escortedRendezvousWaypoint.getIndex()));
                    assert (IndexLinkValidator.isIndexInTargetList(escortedIngressWP.getTargets(), escortedTargetWP.getIndex()));
                    assert (IndexLinkValidator.isIndexInTargetList(escortedTargetWP.getTargets(), escortedTargetFinalWP.getIndex()));
                    assert (!isNextWaypointLinked);                    
                }
                else if (prevWaypoint.getWpAction().equals(WaypointAction.WP_ACTION_TARGET_FINAL))
                {
                    assert (!isNextWaypointLinked);                    
                }
                else
                {
                    assert (isNextWaypointLinked);
                }
            }

            prevWaypoint = waypoint;
        }
        
        verifyEscortedPlanesCloseToRendezvous();
    }

    private void validateLinkageBetweenFlights() throws PWCGException
    {
        IMissionPointSet escortMissionPointSetInterface = playerFlight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ESCORT);
        MissionPointEscortWaypointSet escortMissionPointSet = (MissionPointEscortWaypointSet) escortMissionPointSetInterface;
        McuTimer forceCompleteTimer = escortMissionPointSet.getEscortSequence().getForceCompleteTimer();
        McuTimer escortCompleteTimer = escortMissionPointSet.getEscortSequence().getEscortCompleteTimer();
        McuTimer coverTimer = escortMissionPointSet.getEscortSequence().getCoverTimer();
        McuCover cover = escortMissionPointSet.getEscortSequence().getCover();
        McuWaypoint escortedIngressWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_INGRESS);

        McuWaypoint egressWaypoint = playerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_EGRESS);
        McuWaypoint escortedEgressWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_EGRESS);

        PlaneMcu leadBomber = escortedFlight.getFlightPlanes().getFlightLeader();
        assert (IndexLinkValidator.isIndexInTargetList(cover.getTargets(), leadBomber.getLinkTrId()));

        assert (IndexLinkValidator.isIndexInTargetList(coverTimer.getTargets(), escortedIngressWP.getIndex()));
        assert (IndexLinkValidator.isIndexInTargetList(escortCompleteTimer.getTargets(), egressWaypoint.getIndex()));
        assert (IndexLinkValidator.isIndexInTargetList(escortedEgressWP.getTargets(), forceCompleteTimer.getIndex()));

    }
    
    private void verifyEscortedPlanesCloseToRendezvous() throws PWCGException
    {
        PlaneMcu leadBomber = escortedFlight.getFlightPlanes().getFlightLeader();
        McuWaypoint rendezvousWaypoint = escortedFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_RENDEZVOUS);
        assert (rendezvousWaypoint != null);

        Coordinate leadEscortedPlanePosition = leadBomber.getPosition();
        Coordinate rendezvousPosition = rendezvousWaypoint.getPosition();
        double distance = MathUtils.calcDist(leadEscortedPlanePosition, rendezvousPosition);
        assert (distance < 6000.0);
    }

    private McuWaypoint getEscortedFlightWaypoint(WaypointAction wpActionIngress) throws PWCGException
    {
        for (McuWaypoint waypoint : escortedFlight.getWaypointPackage().getAllWaypoints())
        {
            if (waypoint.getWpAction().equals(wpActionIngress))
            {
                return waypoint;
            }
        }

        throw new PWCGException("No waypoint of type found: " + wpActionIngress);
    }

    private void validateWaypointTypes() throws PWCGException
    {
        boolean rendezvousFound = false;

        WaypointPriorityValidator.validateWaypointTypes(playerFlight);

        for (McuWaypoint waypoint : playerFlight.getWaypointPackage().getAllWaypoints())
        {
            if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_RENDEZVOUS))
            {
                rendezvousFound = true;
            }
        }

        assert (rendezvousFound);
    }
}
