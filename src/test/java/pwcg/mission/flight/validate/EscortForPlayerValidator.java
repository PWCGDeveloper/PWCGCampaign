package pwcg.mission.flight.validate;

import java.util.List;

import org.junit.jupiter.api.Assertions;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.mission.MissionFlights;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.NecessaryFlightType;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointEscortForPlayerWaypointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;
import pwcg.mission.mcu.McuCheckZone;
import pwcg.mission.mcu.McuCover;
import pwcg.mission.mcu.McuForceComplete;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class EscortForPlayerValidator 
{
    private MissionFlights missionFlights;
    private IFlight playerFlight = null;
    private IFlight escortForPlayerFlight = null;

    public EscortForPlayerValidator (MissionFlights missionFlights) throws PWCGException
    {
        this.missionFlights = missionFlights;
        this.playerFlight = missionFlights.getPlayerFlights().get(0);
        
        List<IFlight> escortForPlayerFlights = missionFlights.getNecessaryFlightsByType(NecessaryFlightType.PLAYER_ESCORT);
        if (!escortForPlayerFlights.isEmpty())
        {
            escortForPlayerFlight = escortForPlayerFlights.get(0);
        }
    }
    
    public void validateEscortForPlayer() throws PWCGException
    {
        IFlight playerFlight = missionFlights.getPlayerFlights().get(0);
        if (playerFlight.getMission().isNightMission())
        {
            validateNoEscortForPlayer();
        }
        else
        {
            validatePlayerEscortFlight();
        }
    }

    public void validateNoEscortForPlayer() throws PWCGException
    {
        Assertions.assertTrue(escortForPlayerFlight == null);        
        Assertions.assertTrue(playerFlight.getAssociatedFlight() == null);        
    }

    private void validatePlayerEscortFlight() throws PWCGException
	{
		Assertions.assertTrue(playerFlight.getWaypointPackage().getAllWaypoints().size() > 0);
        validateLinkedEscort();
        validateRendezvous();
        validateEscortLeadPlane();
        validateEscortFlags();
        validateEscortAltitude();
        validateWaypointLinkage();
	}

    private void validateRendezvous() throws PWCGException
    {
        Assertions.assertTrue(escortForPlayerFlight != null);
   
        PlaneMcu leadEscortedPlane = playerFlight.getFlightPlanes().getFlightLeader();
        
        IMissionPointSet escortMissionPointSetInterface = escortForPlayerFlight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ESCORT);
        MissionPointEscortForPlayerWaypointSet escortMissionPointSet = (MissionPointEscortForPlayerWaypointSet) escortMissionPointSetInterface;
        McuCover cover = escortMissionPointSet.getEscortSequence().getCover();
        Assertions.assertTrue(cover.getTargets().get(0).equals(Integer.valueOf(leadEscortedPlane.getLinkTrId()).toString()));
   
        McuWaypoint airstartWP = WaypointGeneratorUtils.findWaypointByType(escortForPlayerFlight.getWaypointPackage().getAllWaypoints(), WaypointType.AIR_START_WAYPOINT.getName());
        PlaneMcu leadEscortPlane = escortForPlayerFlight.getFlightPlanes().getFlightLeader();
        double distanceFromPlaneToAirStart = MathUtils.calcDist(leadEscortPlane.getPosition(), airstartWP.getPosition());
        Assertions.assertTrue(distanceFromPlaneToAirStart < 2000.0);

        McuWaypoint rendezvousWP = WaypointGeneratorUtils.findWaypointByType(escortForPlayerFlight.getWaypointPackage().getAllWaypoints(), WaypointType.RENDEZVOUS_WAYPOINT.getName());
        double distanceToRendezvous = MathUtils.calcDist(airstartWP.getPosition(), rendezvousWP.getPosition());
        Assertions.assertTrue(distanceToRendezvous < 4000.0);

        McuWaypoint rendezvousWPForPlayer = WaypointGeneratorUtils.findWaypointByType(playerFlight.getWaypointPackage().getAllWaypoints(), WaypointType.RENDEZVOUS_WAYPOINT.getName());
        double distanceBetweenRendezvous = MathUtils.calcDist(rendezvousWPForPlayer.getPosition(), rendezvousWP.getPosition());
        Assertions.assertTrue(distanceBetweenRendezvous < 1000.0);
        
        McuTimer rendezvousTimer = escortMissionPointSet.getRendezvousCheckZoneTimer();
        McuCheckZone rendezvousCZ = escortMissionPointSet.getRendezvousCheckZone();
        Assertions.assertTrue(escortMissionPointSet.getLastWaypointBefore().isTarget("" + rendezvousTimer.getIndex()));
        Assertions.assertTrue(rendezvousTimer.isTarget("" + rendezvousCZ.getIndex()));
        Assertions.assertTrue(rendezvousCZ.isObject("" + playerFlight.getFlightPlanes().getFlightLeader().getLinkTrId()));
    }

    private void validateEscortLeadPlane() throws PWCGException
    {
        PlaneMcu leadEscortPlane = escortForPlayerFlight.getFlightPlanes().getFlightLeader();
        Assertions.assertTrue(leadEscortPlane != null);
    }

    private void validateLinkedEscort()
    {
        Assertions.assertTrue(escortForPlayerFlight != null);        
        Assertions.assertTrue(playerFlight.getAssociatedFlight() != null);        
    }

    private void validateEscortFlags() throws PWCGException
    {
        Assertions.assertTrue(escortForPlayerFlight != null);
        Assertions.assertTrue(escortForPlayerFlight.getFlightInformation().isVirtual() == false);
        Assertions.assertTrue(escortForPlayerFlight.getFlightInformation().isAirStart() == true);
        Assertions.assertTrue(escortForPlayerFlight.isPlayerFlight() == false);
    }

    private void validateEscortAltitude()
    {
        for (PlaneMcu escortPlane : escortForPlayerFlight.getFlightPlanes().getPlanes())
        {
            Assertions.assertTrue(escortPlane.getPosition().getYPos() > 1000.0);
        }
    }

	private void validateWaypointLinkage() throws PWCGException 
	{
        McuWaypoint escortAirStartWP = escortForPlayerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_START);
        Assertions.assertTrue(escortAirStartWP != null);

        McuWaypoint escortRendezvousWP = escortForPlayerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_RENDEZVOUS);
        Assertions.assertTrue(escortRendezvousWP != null);

        McuWaypoint escortReturnToBaseWP = escortForPlayerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_EGRESS);
        Assertions.assertTrue(escortReturnToBaseWP != null);

        McuWaypoint playerRendezvousWP = playerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_RENDEZVOUS);
        McuWaypoint playerEgressWP = playerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_EGRESS);

        IMissionPointSet escortMissionPointSetInterface = escortForPlayerFlight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ESCORT);
        MissionPointEscortForPlayerWaypointSet escortMissionPointSet = (MissionPointEscortForPlayerWaypointSet) escortMissionPointSetInterface;
        McuTimer forceCompleteTimer = escortMissionPointSet.getEscortSequence().getForceCompleteTimer();
        McuTimer escortCompleteTimer = escortMissionPointSet.getEscortSequence().getEscortCompleteTimer();
        McuTimer coverTimer = escortMissionPointSet.getEscortSequence().getCoverTimer();
        McuCover cover = escortMissionPointSet.getEscortSequence().getCover();
        McuForceComplete forceComplete = escortMissionPointSet.getEscortSequence().getForceComplete();

        Assertions.assertTrue(IndexLinkValidator.isIndexInTargetList(escortAirStartWP.getTargets(), escortRendezvousWP.getIndex()));
        Assertions.assertTrue(IndexLinkValidator.isIndexInTargetList(playerRendezvousWP.getTargets(), coverTimer.getIndex()));
        Assertions.assertTrue(IndexLinkValidator.isIndexInTargetList(coverTimer.getTargets(), cover.getIndex()));
        Assertions.assertTrue(IndexLinkValidator.isIndexInTargetList(cover.getTargets(), playerFlight.getFlightPlanes().getFlightLeader().getLinkTrId()));
		
        Assertions.assertTrue(IndexLinkValidator.isIndexInTargetList(playerEgressWP.getTargets(), forceCompleteTimer.getIndex()));
        Assertions.assertTrue(IndexLinkValidator.isIndexInTargetList(forceCompleteTimer.getTargets(), escortCompleteTimer.getIndex()));
        Assertions.assertTrue(IndexLinkValidator.isIndexInTargetList(forceCompleteTimer.getTargets(), forceComplete.getIndex()));
        Assertions.assertTrue(IndexLinkValidator.isIndexInTargetList(escortCompleteTimer.getTargets(), escortReturnToBaseWP.getIndex()));
	}
}
