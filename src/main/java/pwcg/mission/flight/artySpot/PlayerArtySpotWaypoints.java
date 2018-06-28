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
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerArtySpotWaypoints extends WaypointGeneratorBase
{
	public PlayerArtySpotWaypoints(Coordinate startCoords, 
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
		Coordinate targetCoord = createArtySpotWaypoint(startPosition);		

		createArtySpotEgress(startPosition, targetCoord);		
	}

	private Coordinate createArtySpotWaypoint(Coordinate startPosition) throws PWCGException, PWCGException
	{
		McuWaypoint artySpotWP = WaypointFactory.createArtillerySpotWaypointType();

		artySpotWP.setTriggerArea(McuWaypoint.TARGET_AREA);
		artySpotWP.setSpeed(waypointSpeed);
		
		// Don't put the player right over the target
		double offsetDirection = RandomNumberGenerator.getRandom(360);
		double offsetDistance = 200 + RandomNumberGenerator.getRandom(1300);
		Coordinate targetCoord = MathUtils.calcNextCoord(targetCoords, offsetDirection, offsetDistance);
        double wpAltitude = getWaypointAltitude(startPosition, targetCoord, getFlightAlt());
        targetCoord.setYPos(wpAltitude);
		artySpotWP.setPosition(targetCoord);	

		artySpotWP.setTargetWaypoint(true);

		waypoints.add(artySpotWP);
		return targetCoord;
	}
		

	private void createArtySpotEgress(Coordinate startPosition, Coordinate targetCoord) throws PWCGException
	{
		// The egress WP is where we will turn off the media
		McuWaypoint artySpotEgressWP = WaypointFactory.createEgressWaypointType();
		artySpotEgressWP.setPriority(WaypointPriority.PRIORITY_MED);			

		artySpotEgressWP.setTriggerArea(McuWaypoint.TARGET_AREA);
		artySpotEgressWP.setSpeed(waypointSpeed);
		
		// Don't put the player right over the target
		double egressDirection = MathUtils.calcAngle(targetCoord, startPosition);
		Coordinate egressCoord = MathUtils.calcNextCoord(targetCoord, egressDirection, 5000.0);
		egressCoord.setYPos(getFlightAlt());
		artySpotEgressWP.setPosition(egressCoord);	

		artySpotEgressWP.setTargetWaypoint(false);

		waypoints.add(artySpotEgressWP);
	}

	/* (non-Javadoc)
	 * @see rof.campaign.mission.WaypointGeneratorBase#flightAltitude()
	 */
	@Override
	protected int determineFlightAltitude() 
	{

		int altitude = 200 + RandomNumberGenerator.getRandom(300);
		
		return altitude;
	}
}
