package pwcg.mission.flight.scramble;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.mcu.McuWaypoint;

public class ScrambleMission extends WaypointGeneratorBase
{
	public ScrambleMission(Coordinate startCoords, 
					  		Coordinate targetCoords, 
					  		Flight flight,
					  		Mission mission) throws PWCGException 
	{
		super(startCoords, targetCoords, flight, mission);
	}

	protected void createTargetWaypoints(Coordinate startPosition) throws PWCGException  
	{
	    Double angle = MathUtils.calcAngle(startPosition, targetCoords);
	    Orientation orientation = new Orientation();
	    orientation.setyOri(angle);

	    McuWaypoint scrambleTargetWP = createFirstScrambleWP(orientation);		

        createSecondScrambleWP(angle, orientation, scrambleTargetWP);        
	}

	private McuWaypoint createFirstScrambleWP(Orientation orientation)
	{
		Coordinate coord =  targetCoords.copy();
	    coord.setYPos(getFlightAlt());

	    // Two waypoints - one to the target and another beyond.
		McuWaypoint scrambleTargetWP = WaypointFactory.createPatrolWaypointType();
	    scrambleTargetWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
	    scrambleTargetWP.setSpeed(waypointSpeed);
	    scrambleTargetWP.setPosition(coord);	
	    scrambleTargetWP.setOrientation(orientation);
	    scrambleTargetWP.setTargetWaypoint(true);
	    waypoints.add(scrambleTargetWP);
		return scrambleTargetWP;
	}

	private void createSecondScrambleWP(Double angle, Orientation orientation, McuWaypoint scrambleTargetWP)
	        throws PWCGException
	{
		// This will help the situation where all of the WPs get triggered and the flight deletes itself.
	    Coordinate secondCoord =  MathUtils.calcNextCoord(targetCoords, angle, 10000.0);
	    secondCoord.setYPos(getFlightAlt());

	    McuWaypoint scrambleFurtherWP = WaypointFactory.createPatrolWaypointType();
	    scrambleFurtherWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
	    scrambleFurtherWP.setSpeed(waypointSpeed);
	    scrambleFurtherWP.setPosition(secondCoord);    
	    scrambleFurtherWP.setTargetWaypoint(false);
	    scrambleTargetWP.setOrientation(orientation);
	    waypoints.add(scrambleFurtherWP);
	}	
}
