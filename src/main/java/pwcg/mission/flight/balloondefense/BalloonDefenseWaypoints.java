package pwcg.mission.flight.balloondefense;

import pwcg.campaign.plane.Balloon;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.mcu.McuWaypoint;

public class BalloonDefenseWaypoints extends WaypointGeneratorBase
{
	public BalloonDefenseWaypoints(Coordinate startCoords, 
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
		coord.setZPos(targetCoords.getZPos() + 50.0);
		coord.setYPos(Balloon.BALLOON_ALTITUDE + 500.0);

		McuWaypoint balloonDefenseWP = WaypointFactory.createBalloonDefenseWaypointType();		
		balloonDefenseWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
		balloonDefenseWP.setSpeed(waypointSpeed);
		balloonDefenseWP.setPosition(coord);	
		balloonDefenseWP.setTargetWaypoint(true);
		waypoints.add(balloonDefenseWP);		
	}
}
