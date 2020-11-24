package pwcg.mission.flight.validate;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.escort.EscortForPlayerFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointEscortWaypointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;
import pwcg.mission.mcu.McuCover;
import pwcg.mission.mcu.McuForceComplete;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class EscortForPlayerValidator 
{
    private IFlight playerFlight;
    private EscortForPlayerFlight escortForPlayerFlight;

    public EscortForPlayerValidator (IFlight playerFlight)
    {
        this.playerFlight = playerFlight;
    }
    
    public void validateEscortForPlayer() throws PWCGException
    {
        if (playerFlight.getMission().isNightMission())
        {
            validateNoEscortForPlayer();
        }
        else
        {
            validatePlayerEscortFlight();
        }
    }


    public void validateNoEscortForPlayer()
    {
        escortForPlayerFlight = playerFlight.getLinkedFlights().getEscortForPlayer();
        assert(escortForPlayerFlight == null);        
    }

    private void validatePlayerEscortFlight() throws PWCGException
	{
		assert(playerFlight.getWaypointPackage().getAllWaypoints().size() > 0);
        validateLinkedEscort();
        validateRendezvous();
        validateEscortLeadPlane();
        validateEscortFlags();
        validateEscortAltitude();
        validateWaypointLinkage();
	}

    private void validateRendezvous() throws PWCGException
    {
        assert(escortForPlayerFlight != null);
   
        PlaneMcu leadEscortedPlane = playerFlight.getFlightPlanes().getFlightLeader();
        
        IMissionPointSet escortMissionPointSetInterface = escortForPlayerFlight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ESCORT);
        MissionPointEscortWaypointSet escortMissionPointSet = (MissionPointEscortWaypointSet) escortMissionPointSetInterface;
        McuCover cover = escortMissionPointSet.getEscortSequence().getCover();
        assert(cover.getTargets().get(0).equals(Integer.valueOf(leadEscortedPlane.getLinkTrId()).toString()));
   
        McuWaypoint airstartWP = WaypointGeneratorUtils.findWaypointByType(escortForPlayerFlight.getWaypointPackage().getAllWaypoints(), WaypointType.AIR_START_WAYPOINT.getName());
        PlaneMcu leadEscortPlane = escortForPlayerFlight.getFlightPlanes().getFlightLeader();
        double distanceFromPlaneToAirStart = MathUtils.calcDist(leadEscortPlane.getPosition(), airstartWP.getPosition());
        assert(distanceFromPlaneToAirStart < 2000.0);

        McuWaypoint rendezvousWP = WaypointGeneratorUtils.findWaypointByType(escortForPlayerFlight.getWaypointPackage().getAllWaypoints(), WaypointType.RENDEZVOUS_WAYPOINT.getName());
        double distanceToRendezvous = MathUtils.calcDist(airstartWP.getPosition(), rendezvousWP.getPosition());
        assert(distanceToRendezvous < 4000.0);

        McuWaypoint rendezvousWPForPlayer = WaypointGeneratorUtils.findWaypointByType(playerFlight.getWaypointPackage().getAllWaypoints(), WaypointType.RENDEZVOUS_WAYPOINT.getName());
        double distanceBetweenRendezvous = MathUtils.calcDist(rendezvousWPForPlayer.getPosition(), rendezvousWP.getPosition());
        assert(distanceBetweenRendezvous < 100.0);
    }

    private void validateEscortLeadPlane() throws PWCGException
    {
        PlaneMcu leadEscortPlane = escortForPlayerFlight.getFlightPlanes().getFlightLeader();
        assert(leadEscortPlane != null);
    }

    private void validateLinkedEscort()
    {
        for (IFlight linkedFlight : playerFlight.getLinkedFlights().getLinkedFlights())
        {
            assert(linkedFlight instanceof EscortForPlayerFlight);
            escortForPlayerFlight = (EscortForPlayerFlight)linkedFlight;    
        }
    }

    private void validateEscortFlags() throws PWCGException
    {
        assert(escortForPlayerFlight != null);
        assert(escortForPlayerFlight.getFlightInformation().isVirtual() == false);
        assert(escortForPlayerFlight.getFlightInformation().isAirStart() == true);
        assert(escortForPlayerFlight.isPlayerFlight() == false);
    }

    private void validateEscortAltitude()
    {
        for (PlaneMcu escortPlane : escortForPlayerFlight.getFlightPlanes().getPlanes())
        {
            assert(escortPlane.getPosition().getYPos() > 1000.0);
        }
    }

	private void validateWaypointLinkage() throws PWCGException 
	{
        McuWaypoint escortAirStartWP = escortForPlayerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_START);
        assert(escortAirStartWP != null);

        McuWaypoint escortRendezvousWP = escortForPlayerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_RENDEZVOUS);
        assert(escortRendezvousWP != null);

        McuWaypoint escortReturnToBaseWP = escortForPlayerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_EGRESS);
        assert(escortReturnToBaseWP != null);

        McuWaypoint playerRendezvousWP = playerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_RENDEZVOUS);
        McuWaypoint playerEgressWP = playerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_EGRESS);

        IMissionPointSet escortMissionPointSetInterface = escortForPlayerFlight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ESCORT);
        MissionPointEscortWaypointSet escortMissionPointSet = (MissionPointEscortWaypointSet) escortMissionPointSetInterface;
        McuTimer forceCompleteTimer = escortMissionPointSet.getEscortSequence().getForceCompleteTimer();
        McuTimer escortCompleteTimer = escortMissionPointSet.getEscortSequence().getEscortCompleteTimer();
        McuTimer coverTimer = escortMissionPointSet.getEscortSequence().getCoverTimer();
        McuCover cover = escortMissionPointSet.getEscortSequence().getCover();
        McuForceComplete forceComplete = escortMissionPointSet.getEscortSequence().getForceComplete();

        assert(IndexLinkValidator.isIndexInTargetList(escortAirStartWP.getTargets(), escortRendezvousWP.getIndex()));
        assert(IndexLinkValidator.isIndexInTargetList(playerRendezvousWP.getTargets(), coverTimer.getIndex()));
        assert(IndexLinkValidator.isIndexInTargetList(coverTimer.getTargets(), cover.getIndex()));
        assert(IndexLinkValidator.isIndexInTargetList(cover.getTargets(), playerFlight.getFlightPlanes().getFlightLeader().getLinkTrId()));
		
        assert(IndexLinkValidator.isIndexInTargetList(playerEgressWP.getTargets(), forceCompleteTimer.getIndex()));
        assert(IndexLinkValidator.isIndexInTargetList(forceCompleteTimer.getTargets(), escortCompleteTimer.getIndex()));
        assert(IndexLinkValidator.isIndexInTargetList(forceCompleteTimer.getTargets(), forceComplete.getIndex()));
        assert(IndexLinkValidator.isIndexInTargetList(escortCompleteTimer.getTargets(), escortReturnToBaseWP.getIndex()));
	}
}
