package pwcg.mission.flight.validate;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.escort.PlayerEscortedFlight;
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
    private PlayerEscortedFlight escortedFlight;

    public PlayerEscortFlightValidator (PlayerIsEscortFlight playerFlight)
    {
        this.playerFlight = playerFlight;
    }
    
	public void validateEscortFlight() throws PWCGException
	{
		assert(playerFlight.getWaypointPackage().getAllWaypoints().size() > 0);
        validateEscortedFlight();
        validateWaypointLinkage();
		validateWaypointTypes();
	}
    
    private void validateEscortedFlight() throws PWCGException
    {
        for (IFlight unit : playerFlight.getLinkedFlights().getLinkedFlights())
        {
            assert(unit instanceof PlayerEscortedFlight);
            escortedFlight = (PlayerEscortedFlight)unit;    
        }
        
        assert(escortedFlight != null);
        assert(escortedFlight.getFlightInformation().isVirtual() == false);
        assert(escortedFlight.getFlightInformation().isAirStart() == true);
        assert(escortedFlight.isPlayerFlight() == false);
        
        for (PlaneMcu escortedPlane : escortedFlight.getFlightPlanes().getPlanes())
        {
            assert(escortedPlane.getPosition().getYPos() > 1000.0);
        }

        PlaneMcu leadEscortedPlane = escortedFlight.getFlightPlanes().getFlightLeader();
        assert(leadEscortedPlane != null);
    }

	private void validateWaypointLinkage() throws PWCGException 
	{
		McuWaypoint escortedAirStartWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_START);
		assert(escortedAirStartWP.getIndex() == escortedFlight.getWaypointPackage().getAllWaypoints().get(0).getIndex());
		
        McuWaypoint escortedIngressWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_INGRESS);
        McuWaypoint escortedEgressWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_EGRESS);
        McuWaypoint escortedTargetWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_TARGET_APPROACH);
        McuWaypoint escortedTargetFinalWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_TARGET_FINAL);

        IMissionPointSet escortMissionPointSetInterface = playerFlight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ESCORT);
        MissionPointEscortWaypointSet escortMissionPointSet = (MissionPointEscortWaypointSet)escortMissionPointSetInterface;
		McuTimer coverTimer  = escortMissionPointSet.getEscortSequence().getCoverTimer();
		McuCover cover = escortMissionPointSet.getEscortSequence().getCover();
		McuTimer forceCompleteTimer = escortMissionPointSet.getEscortSequence().getForceCompleteTimer();
		McuForceComplete forceCompleteEntity = escortMissionPointSet.getEscortSequence().getForceComplete();
        McuTimer escortCompleteTimer = escortMissionPointSet.getEscortSequence().getEscortCompleteTimer();

		McuWaypoint rendezvousWaypoint = playerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_RENDEZVOUS);
        assert(rendezvousWaypoint != null);

        McuWaypoint egressWaypoint = playerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_EGRESS);
        assert(egressWaypoint != null);

		McuWaypoint prevWaypoint = null;
		for (McuWaypoint waypoint : playerFlight.getWaypointPackage().getAllWaypoints())
		{
			if (prevWaypoint != null)
			{
				boolean isNextWaypointLinked = isIndexInTargetList(waypoint.getIndex(), prevWaypoint.getTargets());
				if (prevWaypoint.getWpAction().equals(WaypointAction.WP_ACTION_RENDEZVOUS))
				{
				    PlaneMcu leadBomber = escortedFlight.getFlightPlanes().getFlightLeader();
					assert(!isNextWaypointLinked);
					assert(isIndexInTargetList(coverTimer.getIndex(), rendezvousWaypoint.getTargets()));
                    assert(isIndexInTargetList(cover.getIndex(), coverTimer.getTargets()));
					assert(isIndexInTargetList(leadBomber.getEntity().getIndex(), cover.getTargets()));
					
                    assert(isIndexInTargetList(escortedIngressWP.getIndex(), escortedAirStartWP.getTargets()));
                    assert(isIndexInTargetList(escortedTargetWP.getIndex(), escortedIngressWP.getTargets()));
                    assert(isIndexInTargetList(escortedTargetFinalWP.getIndex(), escortedTargetWP.getTargets()));
					
                    verifyEscortedPlanesCloseToRendezvous(leadBomber);			        
				}
				else if (prevWaypoint.getWpAction().equals(WaypointAction.WP_ACTION_EGRESS))
				{
					assert(isNextWaypointLinked);
                    assert(isIndexInTargetList(forceCompleteTimer.getIndex(), escortedEgressWP.getTargets()));
                    assert(isIndexInTargetList(forceCompleteEntity.getIndex(), forceCompleteTimer.getTargets()));
                    assert(isIndexInTargetList(escortCompleteTimer.getIndex(), forceCompleteTimer.getTargets()));
					assert(isIndexInTargetList(egressWaypoint.getIndex(), escortCompleteTimer.getTargets()));
				}
				else
				{
					assert(isNextWaypointLinked);
				}
			}
			
			prevWaypoint = waypoint;
		}
	}

    private void verifyEscortedPlanesCloseToRendezvous(PlaneMcu leadBomber) throws PWCGException
    {
        McuWaypoint rendezvousWaypoint = playerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_RENDEZVOUS);
        assert(rendezvousWaypoint != null);

        Coordinate leadEscortedPlanePosition = leadBomber.getPosition();
        Coordinate rendezvousPosition = rendezvousWaypoint.getPosition();
        double distance = MathUtils.calcDist(leadEscortedPlanePosition, rendezvousPosition);
        assert(distance < 5000.0);
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

	private void validateWaypointTypes() 
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
		
		assert(rendezvousFound);
	}
}
