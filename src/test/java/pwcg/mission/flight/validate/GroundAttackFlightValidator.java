package pwcg.mission.flight.validate;

import org.junit.jupiter.api.Assertions;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointAttackSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;
import pwcg.mission.mcu.McuWaypoint;

public class GroundAttackFlightValidator 
{
	public void validateGroundAttackFlight(IFlight flight) throws PWCGException
	{
		Assertions.assertTrue(flight.getWaypointPackage().getAllWaypoints().size() > 0);
		validateWaypointLinkage(flight);
		validateWaypointTypes(flight);
		validateAttackAreaMcu(flight);
	}

	private void validateWaypointLinkage(IFlight attackFlight) throws PWCGException 
	{
		McuWaypoint prevWaypoint = null;
		for (McuWaypoint waypoint : attackFlight.getWaypointPackage().getAllWaypoints())
		{
			if (prevWaypoint != null)
			{
				boolean isNextWaypointLinked = IndexLinkValidator.isIndexInTargetList(prevWaypoint.getTargets(), waypoint.getIndex());
				if (!waypoint.getWpAction().equals(WaypointAction.WP_ACTION_TARGET_EGRESS))
				{
					Assertions.assertTrue(isNextWaypointLinked);
				}
				else
				{
					Assertions.assertTrue(!isNextWaypointLinked);
				}
			}
			
			prevWaypoint = waypoint;
		}
	}

	private void validateWaypointTypes(IFlight attackFlight)  throws PWCGException
	{
		WaypointPriorityValidator.validateWaypointTypes(attackFlight);

        McuWaypoint targetAppoachWaypoint = attackFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_TARGET_APPROACH);
        McuWaypoint targetFinalWaypoint = attackFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_TARGET_FINAL);
        McuWaypoint targetEgressWaypoint = attackFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_TARGET_EGRESS);
		
		Assertions.assertNotNull(targetAppoachWaypoint);
		Assertions.assertNotNull(targetFinalWaypoint);
		Assertions.assertNotNull(targetEgressWaypoint);
	}
	
	private void validateAttackAreaMcu (IFlight flight) throws PWCGException
	{
        McuWaypoint targetFinalWaypoint = flight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_TARGET_FINAL);
        MissionPointAttackSet attackMissionPoint = (MissionPointAttackSet)flight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ATTACK);
        Coordinate attackPosition = attackMissionPoint.getAttackSequence().getAttackAreaMcu().getPosition();
        Assertions.assertEquals(targetFinalWaypoint.getPosition().getYPos(), attackPosition.getYPos());
	}
}
