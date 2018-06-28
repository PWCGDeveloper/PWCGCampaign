package pwcg.mission.flight.bomb;

import java.util.List;

import pwcg.campaign.api.IMissionAltitudeGenerator;
import pwcg.campaign.factory.MissionAltitudeGeneratorFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.GroundAttackWaypointHelper;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.mcu.McuWaypoint;

public class BombingWaypoints extends WaypointGeneratorBase
{    
	protected BombingAltitudeLevel bombingAltitudeLevel = BombingAltitudeLevel.MED;
	
	protected boolean escortedByPlayer = false;

	public enum BombingAltitudeLevel
	{
		LOW,
		MED,
		HIGH
	}
	
	public BombingWaypoints(Coordinate startCoords, 
					  		Coordinate targetCoords, 
					  		Flight flight,
					  		Mission mission,
					  		BombingAltitudeLevel bombingAltitude) throws PWCGException 
	{
		super(startCoords, targetCoords, flight, mission);
		
		this.bombingAltitudeLevel = bombingAltitude;		
		this.flightAlt = determineFlightAltitude();
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
		int attackAltitude = determineFlightAltitude();
		GroundAttackWaypointHelper groundAttackWaypointHelper = new GroundAttackWaypointHelper(ingressPosition, targetCoords, attackAltitude, waypointSpeed);
		List<McuWaypoint> groundAttackWaypoints = groundAttackWaypointHelper.createTargetWaypoints();
		waypoints.addAll(groundAttackWaypoints);
	}

	@Override
	protected int determineFlightAltitude() 
	{
        IMissionAltitudeGenerator missionAltitudeGenerator = MissionAltitudeGeneratorFactory.createMissionAltitudeGeneratorFactory();
		return missionAltitudeGenerator.getBombingAltitude(bombingAltitudeLevel);
	}

}
