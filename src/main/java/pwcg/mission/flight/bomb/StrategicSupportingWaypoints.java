package pwcg.mission.flight.bomb;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.waypoint.GroundAttackWaypointHelper;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.mcu.McuWaypoint;

public class StrategicSupportingWaypoints extends WaypointGeneratorBase
{
	public StrategicSupportingWaypoints(Coordinate startCoords, 
					  		Coordinate targetCoords, 
					  		Flight flight,
					  		Mission mission) throws PWCGException 
	{
		super(startCoords, targetCoords, flight, mission);
	}
	
	public List<McuWaypoint> createWaypoints() throws PWCGException 
	{
		createTargetWaypoints(targetCoords);
						
		setWaypointsNonFighterPriority();
		dumpWaypoints();

		return waypoints;
	}

	@Override
	protected void createTargetWaypoints(Coordinate ingressPosition) throws PWCGException  
	{		
		int attackAltitude = determineFlightAltitude();
		GroundAttackWaypointHelper groundAttackWaypointHelper = new GroundAttackWaypointHelper(ingressPosition, targetCoords, attackAltitude, waypointSpeed);
		List<McuWaypoint> groundAttackWaypoints = groundAttackWaypointHelper.createTargetWaypoints();
		waypoints.addAll(groundAttackWaypoints);
		
		createApproachWaypoint(flight.getAirfield());	
	}

	@Override
	protected int determineFlightAltitude() 
	{
		int altitude = 3500;
		if (flight.getFlightType() == FlightTypes.STRATEGIC_BOMB)
		{
			altitude = 4000;
			int randomAlt = RandomNumberGenerator.getRandom(2000);
			
			altitude = altitude + randomAlt;
		}
		else
		{
			altitude = 800;
			int randomAlt = RandomNumberGenerator.getRandom(500);
			
			altitude = altitude + randomAlt;			
		}
		
		return altitude;
	}

}
