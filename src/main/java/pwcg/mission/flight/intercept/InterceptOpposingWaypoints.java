package pwcg.mission.flight.intercept;

import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.mcu.McuWaypoint;

public class InterceptOpposingWaypoints extends WaypointGeneratorBase
{
	public InterceptOpposingWaypoints(Coordinate startCoords, 
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
	protected void createTargetWaypoints(Coordinate targetCoords) throws PWCGException  
	{		
		createTargetApproachWaypoint();
		
		createTargetWaypoint();

		// Move back towards base for egress
		IAirfield field =  mission.getCampaign().getPlayerAirfield();
		Coordinate baseCoords = field.getPosition().copy();
		double angle = MathUtils.calcAngle(baseCoords, targetCoords);
		Coordinate egressCoords = MathUtils.calcNextCoord(baseCoords, angle, 10000.0);
		createEgressWaypoint(egressCoords);		
		createApproachWaypoint(flight.getAirfield());	
	}

	protected McuWaypoint createTargetApproachWaypoint() throws PWCGException  
	{
		double angle = 80.0;
		double distance = 4000.0;
		Coordinate coord = MathUtils.calcNextCoord(targetCoords, angle, distance);
		coord.setYPos(getFlightAlt());

		McuWaypoint targetApproachWP = WaypointFactory.createTargetApproachWaypointType();
		targetApproachWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
		targetApproachWP.setSpeed(waypointSpeed);
		targetApproachWP.setPosition(coord);
		targetApproachWP.setTargetWaypoint(false);
		waypoints.add(targetApproachWP);	
		
		return targetApproachWP;
	}

	private void createTargetWaypoint() throws PWCGException  
	{
		Coordinate coord = targetCoords.copy();
		coord.setYPos(getFlightAlt());

		McuWaypoint targetWP = WaypointFactory.createTargetFinalWaypointType();
		targetWP.setTriggerArea(McuWaypoint.TARGET_AREA);
		targetWP.setSpeed(waypointSpeed);
		targetWP.setPosition(coord);	
		targetWP.setTargetWaypoint(true);

		waypoints.add(targetWP);
	}	

	@Override
	protected int determineFlightAltitude() 
	{
        int altitude = 4000;
        int randomAlt = RandomNumberGenerator.getRandom(1000);
        
        altitude = altitude + randomAlt;
		
		return altitude;
	}
}
