package pwcg.mission.flight.offensive;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.mcu.McuWaypoint;

public abstract class OffensiveWaypoints extends WaypointGeneratorBase
{
	public OffensiveWaypoints(Coordinate startCoords, 
					  	  Coordinate targetCoords, 
					  	  Flight flight,
					  	  Mission mission) throws PWCGException 
{
		super(startCoords, targetCoords, flight, mission);
	}
	
	protected McuWaypoint createWP(Coordinate coord) throws PWCGException 
	{
		coord.setYPos(getFlightAlt());

		McuWaypoint wp = WaypointFactory.createPatrolWaypointType();
		wp.setTriggerArea(McuWaypoint.TARGET_AREA);
		wp.setSpeed(waypointSpeed);
		wp.setPosition(coord);
		
		return wp;
	}
}
