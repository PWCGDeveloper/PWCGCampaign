package pwcg.mission.flight.attack;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.GroundAttackWaypointHelper;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.mcu.McuWaypoint;

public class GroundAttackWaypoints extends WaypointGeneratorBase
{

	public GroundAttackWaypoints(Coordinate startCoords, 
					  			 Coordinate targetCoords, 
					  			 Flight flight,
					  			 Mission mission) throws PWCGException 
	{
		super(startCoords, targetCoords, flight, mission);
	}

    @Override
    public List<McuWaypoint> createWaypoints() throws PWCGException 
    {
        super.createWaypoints();
        setWaypointsNonFighterPriority();

        return waypoints;
    }

	protected void createTargetWaypoints(Coordinate ingressPosition) throws PWCGException  
	{
		int attackAltitude = createAttackAltitude();
		GroundAttackWaypointHelper groundAttackWaypointHelper = new GroundAttackWaypointHelper(ingressPosition, targetCoords, attackAltitude, waypointSpeed);
		List<McuWaypoint> groundAttackWaypoints = groundAttackWaypointHelper.createTargetWaypoints();
		waypoints.addAll(groundAttackWaypoints);
	}

	private int createAttackAltitude()
	{
		int randomVariation = RandomNumberGenerator.getRandom(30) + 1;
		randomVariation = randomVariation * 10;
		randomVariation = 100 - randomVariation;
		return 1000 + randomVariation;
	}

	@Override
	protected int determineFlightAltitude() 
	{
		int altitude = 800;
		int randomAlt = RandomNumberGenerator.getRandom(400);
		altitude = altitude + randomAlt;			

		return altitude;
	}

}
