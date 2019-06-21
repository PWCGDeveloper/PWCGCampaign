package pwcg.mission.flight.spy;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.mcu.McuWaypoint;

public class SpyExtractWaypoints extends WaypointGeneratorBase
{
	public SpyExtractWaypoints(Coordinate startCoords, 
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
        return waypoints;
    }
    
	protected void createTargetWaypoints(Coordinate startPosition) throws PWCGException  
	{
	    Coordinate pickupLocation = getSpyExtractLocation(startPosition);
		createSpyExtractWaypoint(pickupLocation);		
	}

    private Coordinate getSpyExtractLocation(Coordinate startPosition) throws PWCGException
    {
		Coordinate pickupLocation = targetCoords.copy();
        pickupLocation.setYPos(getFlightAlt());
        return pickupLocation;
    }

    private void createSpyExtractWaypoint(Coordinate pickupLocation)
    {
        McuWaypoint approachWP = WaypointFactory.createSpyExtractWaypointType();
        approachWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        approachWP.setSpeed(waypointSpeed);
        approachWP.setPosition(pickupLocation);
        approachWP.setTargetWaypoint(true);
        waypoints.add(approachWP);
    }

	@Override
	protected int determineFlightAltitude() 
	{

		int altitude = 500 + RandomNumberGenerator.getRandom(100);
		
		return altitude;
	}
}
