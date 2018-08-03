package pwcg.mission.flight.scramble;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuWaypoint;

public class ScrambleWaypoints
{
	protected int waypointSpeed = 130;
	protected List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();
	protected Coordinate startCoords = null;
	protected Coordinate targetCoords = null;
	protected double flightAlt = 2000.0;
	protected Flight flight = null;
	protected Mission mission = null;
	 	
	public ScrambleWaypoints(Coordinate startCoords, 
					  		Coordinate targetCoords, 
					  		Flight flight,
					  		Mission mission) 
	{
		this.startCoords = startCoords.copy();
		this.targetCoords = targetCoords.copy();
		this.flight = flight;
		this.mission = mission;
		this.waypointSpeed = flight.getPlanes().get(0).getCruisingSpeed();
	}
	
	public List<McuWaypoint> createWaypoints() throws PWCGException 
	{
		createStartWaypoint();
		return waypoints;
	}
	
	
	protected void createStartWaypoint() throws PWCGException  
	{
		double takeoffOrientation = flight.getAirfield().getTakeoffLocation().getOrientation().getyOri();
		int InitialWaypointDistance = flight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.InitialWaypointDistanceKey);
		startCoords = MathUtils.calcNextCoord(flight.getAirfield().getTakeoffLocation().getPosition().copy(), takeoffOrientation, InitialWaypointDistance);

		int InitialWaypointAltitude = flight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.InitialWaypointAltitudeKey);
		startCoords.setYPos(InitialWaypointAltitude);

		McuWaypoint startWP = WaypointFactory.createPatrolWaypointType();
		startWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
		startWP.setSpeed(waypointSpeed - 10);
		startWP.setPosition(startCoords);
		waypoints.add(startWP);
		
		createTargetWaypoints (startWP);
	}

	protected void createTargetWaypoints(McuWaypoint startWP) throws PWCGException  
	{
		double angle = MathUtils.calcAngle(startWP.getPosition(), targetCoords);
	    Orientation wpOrientation = new Orientation();
	    wpOrientation.setyOri(angle);
		
		Coordinate scrambleTargetCoords =  targetCoords.copy();
		scrambleTargetCoords.setYPos(flightAlt);
         
		McuWaypoint scrambleTargetWP = WaypointFactory.createPatrolWaypointType();
		scrambleTargetWP.setPosition(scrambleTargetCoords);	
		scrambleTargetWP.setOrientation(wpOrientation.copy());
		scrambleTargetWP.setTargetWaypoint(true);
		scrambleTargetWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
		scrambleTargetWP.setSpeed(waypointSpeed);
		waypoints.add(scrambleTargetWP);


		// Add another WP beyond the target to prevent premature exit
		double returnAngle = MathUtils.adjustAngle(angle, 180.0);
		Coordinate scrambleSecondaryCoords =  MathUtils.calcNextCoord(scrambleTargetCoords, returnAngle, 3000.0);
		scrambleSecondaryCoords.setYPos(flightAlt);

		McuWaypoint scrambleSecondaryWP = WaypointFactory.createPatrolWaypointType();
		scrambleSecondaryWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
		scrambleSecondaryWP.setSpeed(waypointSpeed);
		scrambleSecondaryWP.setPosition(scrambleSecondaryCoords);    
		scrambleSecondaryWP.setTargetWaypoint(true);
		scrambleSecondaryWP.setOrientation(wpOrientation.copy());
		waypoints.add(scrambleSecondaryWP);
	}	
}
