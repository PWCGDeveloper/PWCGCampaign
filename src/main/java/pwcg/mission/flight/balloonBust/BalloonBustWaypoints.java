package pwcg.mission.flight.balloonBust;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.mcu.McuWaypoint;

public class BalloonBustWaypoints extends WaypointGeneratorBase
{
	public BalloonBustWaypoints(Coordinate startCoords, 
					  		Coordinate targetCoords, 
					  		Flight flight,
					  		Mission mission) throws PWCGException 
	{
		super(startCoords, targetCoords, flight, mission);
	}
	

	protected void createTargetWaypoints(Coordinate startPosition) throws PWCGException  
	{
		createBalloonBustWP();
	}
	
	
	protected void createBalloonBustWP() throws PWCGException  
	{
		Coordinate coord = new Coordinate();
		coord.setXPos(targetCoords.getXPos() + 50.0);
		coord.setZPos(targetCoords.getZPos());
		coord.setYPos(targetCoords.getYPos() + 300.0);

		McuWaypoint balloonBustWP = WaypointFactory.createBalloonBustWaypointType();
		balloonBustWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
		balloonBustWP.setSpeed(waypointSpeed);
		balloonBustWP.setPosition(coord);	
		balloonBustWP.setTargetWaypoint(true);
		waypoints.add(balloonBustWP);		
	}
}
