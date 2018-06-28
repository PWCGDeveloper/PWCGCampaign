package pwcg.mission.flight.escort;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerEscortWaypoints extends WaypointGeneratorBase
{

	public PlayerEscortWaypoints(Coordinate startCoords, 
					  		Coordinate targetCoords, 
					  		Flight flight,
					  		Mission mission) throws PWCGException 
	{
		super(startCoords, targetCoords, flight, mission);
	}

	protected void createTargetWaypoints(Coordinate startPosition) throws PWCGException  
	{
		createFirstWaypoint();
	}

	protected void createFirstWaypoint() throws PWCGException  
	{
		Coordinate coord = new Coordinate();
		coord.setXPos(targetCoords.getXPos() + 50.0);
		coord.setZPos(targetCoords.getZPos());
		coord.setYPos(targetCoords.getYPos() + 500.0);

		McuWaypoint rendezvousWaypoint = WaypointFactory.createRendezvousWaypointType();
		rendezvousWaypoint.setTriggerArea(McuWaypoint.COMBAT_AREA);		
		rendezvousWaypoint.setPosition(coord);	
		rendezvousWaypoint.setTargetWaypoint(true);

		waypoints.add(rendezvousWaypoint);		
	}
}
