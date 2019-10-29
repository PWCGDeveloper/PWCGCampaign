package pwcg.mission.flight.validate;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Unit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.bomb.BombingFlight;
import pwcg.mission.flight.escort.PlayerEscortFlight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.mcu.McuCover;
import pwcg.mission.mcu.McuForceComplete;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerEscortFlightValidator 
{
    private McuWaypoint rendezvousWp = null;
    private PlayerEscortFlight playerFlight;
    private BombingFlight escortedFlight;

    public PlayerEscortFlightValidator (PlayerEscortFlight playerFlight)
    {
        this.playerFlight = playerFlight;
    }
    
	public void validateEscortFlight() throws PWCGException
	{
		assert(playerFlight.getWaypointPackage().getWaypointsForLeadPlane().size() > 0);
        validateEscortedFlight();
        validateWaypointLinkage();
		validateWaypointTypes();
	}
    
    private void validateEscortedFlight() throws PWCGException
    {
        for (Unit unit : playerFlight.getLinkedUnits())
        {
            if (unit instanceof Flight)
            {
                assert(unit instanceof BombingFlight);
                escortedFlight = (BombingFlight)unit;    
            }
        }
        
        assert(escortedFlight != null);
        assert(escortedFlight.isVirtual() == false);
        assert(escortedFlight.isAirStart() == true);
        
        for (PlaneMCU escortedPlane : escortedFlight.getPlanes())
        {
            assert(escortedPlane.getPosition().getYPos() > 1000.0);
        }
        
        PlaneMCU leadEscortedPlane = escortedFlight.getPlanes().get(0);
        assert(leadEscortedPlane != null);
    }

	private void validateWaypointLinkage() throws PWCGException 
	{
		McuWaypoint escortedAirStartWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_START);
		assert(escortedAirStartWP.getIndex() == escortedFlight.getWaypointPackage().getWaypointsForLeadPlane().get(0).getIndex());
		
        McuWaypoint escortedIngressWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_INGRESS);
        McuWaypoint escortedEgressWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_EGRESS);
        McuWaypoint escortedTargetWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_TARGET_APPROACH);
        McuWaypoint escortedTargetFinalWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_TARGET_FINAL);

		McuTimer coverTimer  = playerFlight.getCoverTimer();
		McuCover cover = playerFlight.getCover();
	    McuTimer escortedFlightWaypointTimer = playerFlight.getEscortedFlightWaypointTimer();

		McuTimer forceCompleteTimer = playerFlight.getForceCompleteTimer();
		McuForceComplete forceCompleteEntity = playerFlight.getForceCompleteEntity();
	    McuTimer egressTimer = playerFlight.getEgressTimer();

		McuWaypoint prevWaypoint = null;
		for (McuWaypoint waypoint : playerFlight.getWaypointPackage().getWaypointsForLeadPlane())
		{
            if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_RENDEZVOUS))
            {
                rendezvousWp = waypoint;
            }
            
			if (prevWaypoint != null)
			{
				boolean isNextWaypointLinked = isIndexInTargetList(waypoint.getIndex(), prevWaypoint.getTargets());
				if (prevWaypoint.getWpAction().equals(WaypointAction.WP_ACTION_RENDEZVOUS))
				{
				    PlaneMCU leadBomber = escortedFlight.getPlanes().get(0);
					assert(!isNextWaypointLinked);
					assert(isIndexInTargetList(coverTimer.getIndex(), rendezvousWp.getTargets()));
					assert(isIndexInTargetList(leadBomber.getEntity().getIndex(), cover.getTargets()));
					assert(isIndexInTargetList(cover.getIndex(), coverTimer.getTargets()));
					assert(isIndexInTargetList(escortedFlightWaypointTimer.getIndex(), coverTimer.getTargets()));
                    assert(isIndexInTargetList(escortedAirStartWP.getIndex(), escortedFlightWaypointTimer.getTargets()));
                    assert(isIndexInTargetList(escortedTargetWP.getIndex(), escortedIngressWP.getTargets()));
                    assert(isIndexInTargetList(escortedTargetFinalWP.getIndex(), escortedTargetWP.getTargets()));
					
                    verifyEscortedPlanesCloseToRendezvous(leadBomber);			        
				}
				else if (prevWaypoint.getWpAction().equals(WaypointAction.WP_ACTION_EGRESS))
				{
					assert(isNextWaypointLinked);
					assert(isIndexInTargetList(forceCompleteTimer.getIndex(), escortedEgressWP.getTargets()));
					assert(isIndexInTargetList(forceCompleteEntity.getIndex(), forceCompleteTimer.getTargets()));
					assert(isIndexInTargetList(egressTimer.getIndex(), forceCompleteTimer.getTargets()));
					assert(isIndexInTargetList(prevWaypoint.getIndex(), egressTimer.getTargets()));
				}
				else
				{
					assert(isNextWaypointLinked);
				}
			}
			
			prevWaypoint = waypoint;
		}
	}

    private void verifyEscortedPlanesCloseToRendezvous(PlaneMCU leadBomber)
    {
        Coordinate leadEscortedPlanePosition = leadBomber.getPosition();
        Coordinate rendezvousPosition = rendezvousWp.getPosition();
        double distance = MathUtils.calcDist(leadEscortedPlanePosition, rendezvousPosition);
        assert(distance < 1000.0);
    }
	
	private McuWaypoint getEscortedFlightWaypoint(WaypointAction wpActionIngress) throws PWCGException
	{
		for (McuWaypoint waypoint : escortedFlight.getWaypointPackage().getWaypointsForLeadPlane())
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

		for (McuWaypoint waypoint : playerFlight.getWaypointPackage().getWaypointsForLeadPlane())
		{
			if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_RENDEZVOUS))
			{
				rendezvousFound = true;
			}
		}
		
		assert(rendezvousFound);
	}
}
