package pwcg.mission.flight.validate;

import java.util.List;

import org.junit.jupiter.api.Assertions;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.MissionFlights;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.NecessaryFlightType;
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
    private IFlight playerFlight;
    private IFlight escortedFlight;

    public PlayerEscortFlightValidator(MissionFlights missionFlights) throws PWCGException
    {
        this.playerFlight = missionFlights.getUnits().get(0);
        
        List<IFlight> escortForPlayerFlights = missionFlights.getNecessaryFlightsByType(NecessaryFlightType.PLAYER_ESCORTED);
        if (!escortForPlayerFlights.isEmpty())
        {
            escortedFlight = escortForPlayerFlights.get(0);
        }
    }

    public void validateEscortFlight() throws PWCGException
    {
        Assertions.assertTrue (playerFlight.getWaypointPackage().getAllWaypoints().size() > 0);
        validateEscortedFlight();
        validateEscortedWaypointLinkage();
        validateEscortWaypointLinkage();
        validateLinkageBetweenFlights();
        validateWaypointTypes();
    }

    private void validateEscortedFlight() throws PWCGException
    {
        assert(escortedFlight != null);        
        assert(playerFlight.getAssociatedFlight() != null);        

        Assertions.assertTrue (escortedFlight != null);
        Assertions.assertTrue (escortedFlight.getFlightInformation().isVirtual() == false);
        Assertions.assertTrue (escortedFlight.getFlightInformation().isAirStart() == true);
        Assertions.assertTrue (escortedFlight.isPlayerFlight() == false);

        for (PlaneMcu escortedPlane : escortedFlight.getFlightPlanes().getPlanes())
        {
            Assertions.assertTrue (escortedPlane.getPosition().getYPos() > 1000.0);
        }

        PlaneMcu leadEscortedPlane = escortedFlight.getFlightPlanes().getFlightLeader();
        Assertions.assertTrue (leadEscortedPlane != null);
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
        Assertions.assertTrue (rendezvousWaypoint != null);

        McuWaypoint egressWaypoint = playerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_EGRESS);
        Assertions.assertTrue (egressWaypoint != null);

        McuWaypoint prevWaypoint = null;
        for (McuWaypoint waypoint : playerFlight.getWaypointPackage().getAllWaypoints())
        {
            if (prevWaypoint != null)
            {
                boolean isNextWaypointLinked = IndexLinkValidator.isIndexInTargetList(prevWaypoint.getTargets(), waypoint.getIndex());
                if (prevWaypoint.getWpAction().equals(WaypointAction.WP_ACTION_RENDEZVOUS))
                {
                    Assertions.assertTrue (!isNextWaypointLinked);
                    Assertions.assertTrue (IndexLinkValidator.isIndexInTargetList(rendezvousWaypoint.getTargets(), coverTimer.getIndex()));
                    Assertions.assertTrue (IndexLinkValidator.isIndexInTargetList(coverTimer.getTargets(), cover.getIndex()));
                }
                else if (prevWaypoint.getWpAction().equals(WaypointAction.WP_ACTION_EGRESS))
                {
                    Assertions.assertTrue (isNextWaypointLinked);
                    Assertions.assertTrue (IndexLinkValidator.isIndexInTargetList(forceCompleteTimer.getTargets(), forceComplete.getIndex()));
                    Assertions.assertTrue (IndexLinkValidator.isIndexInTargetList(forceCompleteTimer.getTargets(), escortCompleteTimer.getIndex()));
                    Assertions.assertTrue (IndexLinkValidator.isIndexInTargetList(escortCompleteTimer.getTargets(), egressWaypoint.getIndex()));
                }
                else
                {
                    Assertions.assertTrue (isNextWaypointLinked);
                }
            }

            prevWaypoint = waypoint;
        }
    }

    private void validateEscortedWaypointLinkage() throws PWCGException
    {
        McuWaypoint escortedAirStartWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_START);
        Assertions.assertTrue (escortedAirStartWP.getIndex() == escortedFlight.getWaypointPackage().getAllWaypoints().get(0).getIndex());

        McuWaypoint escortedRendezvousWaypoint = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_RENDEZVOUS);
        McuWaypoint escortedIngressWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_INGRESS);
        McuWaypoint escortedTargetWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_TARGET_APPROACH);
        McuWaypoint escortedTargetFinalWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_TARGET_FINAL);

        int escortedWaypointsSize = escortedFlight.getWaypointPackage().getAllWaypoints().size();
        Assertions.assertTrue (escortedWaypointsSize == 8);

        PlaneMcu leadBomber = escortedFlight.getFlightPlanes().getFlightLeader();
        double distancePlaneToAirStart = MathUtils.calcDist(leadBomber.getPosition(), escortedAirStartWP.getPosition());
        Assertions.assertTrue (distancePlaneToAirStart < 2000.0);

        double distanceAirStartTORendezvous = MathUtils.calcDist(escortedAirStartWP.getPosition(), escortedRendezvousWaypoint.getPosition());
        Assertions.assertTrue (distanceAirStartTORendezvous < 4000.0);

        double distanceRendezvousToIngress = MathUtils.calcDist(escortedRendezvousWaypoint.getPosition(), escortedIngressWP.getPosition());
        Assertions.assertTrue (distanceRendezvousToIngress < 8000.0);

        McuWaypoint prevWaypoint = null;
        for (McuWaypoint waypoint : escortedFlight.getWaypointPackage().getAllWaypoints())
        {
            if (prevWaypoint != null)
            {
                boolean isNextWaypointLinked = IndexLinkValidator.isIndexInTargetList(prevWaypoint.getTargets(), waypoint.getIndex());
                if (prevWaypoint.getWpAction().equals(WaypointAction.WP_ACTION_RENDEZVOUS))
                {
                    Assertions.assertTrue (IndexLinkValidator.isIndexInTargetList(escortedAirStartWP.getTargets(), escortedRendezvousWaypoint.getIndex()));
                    Assertions.assertTrue (IndexLinkValidator.isIndexInTargetList(escortedIngressWP.getTargets(), escortedTargetWP.getIndex()));
                    Assertions.assertTrue (IndexLinkValidator.isIndexInTargetList(escortedTargetWP.getTargets(), escortedTargetFinalWP.getIndex()));
                    Assertions.assertTrue (!isNextWaypointLinked);                    
                }
                else if (prevWaypoint.getWpAction().equals(WaypointAction.WP_ACTION_TARGET_FINAL))
                {
                    Assertions.assertTrue (!isNextWaypointLinked);                    
                }
                else
                {
                    Assertions.assertTrue (isNextWaypointLinked);
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
        Assertions.assertTrue (IndexLinkValidator.isIndexInTargetList(cover.getTargets(), leadBomber.getLinkTrId()));

        Assertions.assertTrue (IndexLinkValidator.isIndexInTargetList(coverTimer.getTargets(), escortedIngressWP.getIndex()));
        Assertions.assertTrue (IndexLinkValidator.isIndexInTargetList(escortCompleteTimer.getTargets(), egressWaypoint.getIndex()));
        Assertions.assertTrue (IndexLinkValidator.isIndexInTargetList(escortedEgressWP.getTargets(), forceCompleteTimer.getIndex()));

    }
    
    private void verifyEscortedPlanesCloseToRendezvous() throws PWCGException
    {
        PlaneMcu leadBomber = escortedFlight.getFlightPlanes().getFlightLeader();
        McuWaypoint rendezvousWaypoint = escortedFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_RENDEZVOUS);
        Assertions.assertTrue (rendezvousWaypoint != null);

        Coordinate leadEscortedPlanePosition = leadBomber.getPosition();
        Coordinate rendezvousPosition = rendezvousWaypoint.getPosition();
        double distance = MathUtils.calcDist(leadEscortedPlanePosition, rendezvousPosition);
        Assertions.assertTrue (distance < 6000.0);
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

        Assertions.assertTrue (rendezvousFound);
    }
}
