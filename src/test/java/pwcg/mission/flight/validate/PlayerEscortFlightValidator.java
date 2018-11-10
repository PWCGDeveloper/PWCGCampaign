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
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.mcu.McuCover;
import pwcg.mission.mcu.McuDeactivate;
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
		McuWaypoint escortedIngressWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_INGRESS);
		assert(escortedIngressWP.getIndex() == escortedFlight.getWaypointPackage().getWaypointsForLeadPlane().get(0).getIndex());
		
        McuWaypoint escortedEgressWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_EGRESS);
        McuWaypoint escortedTargetWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_TARGET_APPROACH);
        McuWaypoint escortedTargetFinalWP = getEscortedFlightWaypoint(WaypointAction.WP_ACTION_TARGET_FINAL);

		McuTimer coverTimer  = playerFlight.getCoverTimer();
		McuCover cover = playerFlight.getCover();
	    McuTimer escortedFlightWaypointTimer = playerFlight.getEscortedFlightWaypointTimer();

		McuTimer deactivateCoverTimer = playerFlight.getDeactivateCoverTimer();
		McuDeactivate deactivateCoverEntity = playerFlight.getDeactivateCoverEntity();
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
                    assert(isIndexInTargetList(escortedIngressWP.getIndex(), escortedFlightWaypointTimer.getTargets()));
                    assert(isIndexInTargetList(escortedTargetWP.getIndex(), escortedIngressWP.getTargets()));
                    assert(isIndexInTargetList(escortedTargetFinalWP.getIndex(), escortedTargetWP.getTargets()));
					
                    verifyEscortedPlanesCloseToRendezvous(leadBomber);			        
				}
				else if (prevWaypoint.getWpAction().equals(WaypointAction.WP_ACTION_EGRESS))
				{
					assert(isNextWaypointLinked);
					assert(isIndexInTargetList(deactivateCoverTimer.getIndex(), escortedEgressWP.getTargets()));
					assert(isIndexInTargetList(deactivateCoverEntity.getIndex(), deactivateCoverTimer.getTargets()));
					assert(isIndexInTargetList(egressTimer.getIndex(), deactivateCoverTimer.getTargets()));
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

		for (McuWaypoint waypoint : playerFlight.getWaypointPackage().getWaypointsForLeadPlane())
		{
			if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_TAKEOFF))
			{
				assert(waypoint.getPriority() == WaypointPriority.PRIORITY_HIGH);
			}
			else
			{
				assert(waypoint.getPriority() == WaypointPriority.PRIORITY_LOW);
			}
			
			if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_RENDEZVOUS))
			{
				rendezvousFound = true;
			}
		}
		
		assert(rendezvousFound);
	}
}
