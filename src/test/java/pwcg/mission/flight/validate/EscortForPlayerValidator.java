package pwcg.mission.flight.validate;

import java.util.List;

import pwcg.core.exception.PWCGException;
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
        assert(cover.getTargets().get(0).equals(Integer.valueOf(leadEscortedPlane.getEntity().getIndex()).toString()));
   
        McuWaypoint ingressWP = WaypointGeneratorUtils.findWaypointByType(playerFlight.getWaypointPackage().getAllWaypoints(), WaypointType.INGRESS_WAYPOINT.getName());
        PlaneMcu leadEscortPlane = escortForPlayerFlight.getFlightPlanes().getFlightLeader();
        assert(leadEscortPlane.getPosition().getXPos() == ingressWP.getPosition().getXPos());
        assert(leadEscortPlane.getPosition().getZPos() == ingressWP.getPosition().getZPos());
        assert(leadEscortPlane.getPosition().getYPos() > ingressWP.getPosition().getYPos());
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
        McuWaypoint escortIngressWP = escortForPlayerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_INGRESS);
        McuWaypoint escortReturnToBaseWP = escortForPlayerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_EGRESS);
		
        McuWaypoint playerIngressWP = playerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_INGRESS);
        McuWaypoint playerEgressWP = playerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_EGRESS);

        IMissionPointSet escortMissionPointSetInterface = escortForPlayerFlight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ESCORT);
        MissionPointEscortWaypointSet escortMissionPointSet = (MissionPointEscortWaypointSet) escortMissionPointSetInterface;
        McuTimer forceCompleteTimer = escortMissionPointSet.getEscortSequence().getForceCompleteTimer();
        McuTimer escortCompleteTimer = escortMissionPointSet.getEscortSequence().getEscortCompleteTimer();
        McuTimer coverTimer = escortMissionPointSet.getEscortSequence().getCoverTimer();
        McuCover cover = escortMissionPointSet.getEscortSequence().getCover();
        McuForceComplete forceComplete = escortMissionPointSet.getEscortSequence().getForceComplete();

        assert(isIndexInTargetList(escortIngressWP.getIndex(), escortAirStartWP.getTargets()));
        assert(isIndexInTargetList(coverTimer.getIndex(), playerIngressWP.getTargets()));
        assert(isIndexInTargetList(cover.getIndex(), coverTimer.getTargets()));
        assert(isIndexInTargetList(playerFlight.getFlightPlanes().getFlightLeader().getEntity().getIndex(), cover.getTargets()));
		
        assert(isIndexInTargetList(forceCompleteTimer.getIndex(), playerEgressWP.getTargets()));
        assert(isIndexInTargetList(escortCompleteTimer.getIndex(), forceCompleteTimer.getTargets()));
        assert(isIndexInTargetList(forceComplete.getIndex(), forceCompleteTimer.getTargets()));
        assert(isIndexInTargetList(escortReturnToBaseWP.getIndex(), escortCompleteTimer.getTargets()));
	}

	private boolean isIndexInTargetList(int targetIndexToFind, List<String>targetsFromMCU) 
	{
		boolean isIndexInTargetList = false;
		for (String targetIndex : targetsFromMCU)
		{
			if (targetIndex.equals(new String("" + targetIndexToFind)))
			{
				isIndexInTargetList = true;	
			}
		}
		return isIndexInTargetList;
	}
}
