package pwcg.mission.flight.artySpot;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.mcu.McuWaypoint;

public class ArtillerySpotWaypoints extends WaypointGeneratorBase
{
	public ArtillerySpotWaypoints(Coordinate startCoords, 
					  		Coordinate targetCoords, 
					  		Flight flight,
					  		Mission mission) throws PWCGException 
	{
		super(startCoords, targetCoords, flight, mission);
	}

	public List<McuWaypoint> createWaypoints() throws PWCGException 
	{
        super.createWaypoints();
        setWaypointsNonFighterPriority();
		dumpWaypoints();

		return waypoints;
	}

	protected void createTargetWaypoints(Coordinate startPosition) throws PWCGException  
	{
		McuWaypoint t1WP = WaypointFactory.createArtillerySpotWaypointType();

		t1WP.setTriggerArea(McuWaypoint.TARGET_AREA);
		t1WP.setSpeed(waypointSpeed);

		Coordinate coord = targetCoords.copy();
        double wpAltitude = getWaypointAltitude(startPosition, coord, getFlightAlt());
        coord.setYPos(wpAltitude);
		t1WP.setPosition(coord);	

		t1WP.setTargetWaypoint(true);

		waypoints.add(t1WP);		
		
		int iterNum = 1;
		
		double angle = RandomNumberGenerator.getRandom(360);	
		
		createNextWaypoint(t1WP, iterNum, angle);
	}
	
	/**
	 * @param lastWP
	 * @param iterNum
	 * @param angle
	 * @throws PWCGException 
	 * @
	 */
	private void createNextWaypoint(McuWaypoint lastWP, int iterNum, double angle) throws PWCGException  
	{
		McuWaypoint t2WP = WaypointFactory.createArtillerySpotWaypointType();
		++ iterNum;

		t2WP.setTriggerArea(McuWaypoint.TARGET_AREA);
		t2WP.setSpeed(waypointSpeed);

		double distance = 2000.0;
		Coordinate coord = MathUtils.calcNextCoord(lastWP.getPosition(), angle, distance);
        double wpAltitude = getWaypointAltitude(lastWP.getPosition(), coord, getFlightAlt());
        coord.setYPos(wpAltitude);
		t2WP.setPosition(coord);	
		t2WP.setTargetWaypoint(true);

		waypoints.add(t2WP);
		
		if (iterNum == 20)
		{
			return;
		}

		angle = MathUtils.adjustAngle (angle, 45);		
		
		createNextWaypoint(t2WP, iterNum, angle);
	}	
	
	
	/* (non-Javadoc)
	 * @see rof.campaign.mission.WaypointGeneratorBase#flightAltitude()
	 */
	@Override
	protected int determineFlightAltitude() 
	{

		int altitude = 200 + RandomNumberGenerator.getRandom(200);
		
		return altitude;
	}
}
