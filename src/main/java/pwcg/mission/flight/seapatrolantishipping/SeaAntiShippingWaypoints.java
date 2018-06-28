package pwcg.mission.flight.seapatrolantishipping;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.plane.Role;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class SeaAntiShippingWaypoints extends WaypointGeneratorBase
{
	private int xOffset = 0;
	private int zOffset = 0;
		
	public SeaAntiShippingWaypoints(Coordinate startCoords, 
					    	  	   Coordinate targetCoords, 
					    	  	   Flight flight,
					    	  	   Mission mission) throws PWCGException 
	{
 		super(startCoords, targetCoords, flight, mission);
		
		xOffset = (-100) - (RandomNumberGenerator.getRandom(1000));
		zOffset = (-100) + RandomNumberGenerator.getRandom(1000);			
	}

    public List<McuWaypoint> createWaypoints() throws PWCGException 
    {
        super.createWaypoints();
        if (flight.getPlanes().get(0).isRole(Role.ROLE_SEA_PLANE_LARGE))
        {
            super.setWaypointsNonFighterPriority();
        }

        return waypoints;
    }

	protected void createTargetWaypoints(Coordinate startPosition) throws PWCGException  
	{
		createPatrolWaypoints();
	}

	protected void createPatrolWaypoints() throws PWCGException  
	{
		// Total waypoints in the patrol pattern
		int numWaypoints = 3 + RandomNumberGenerator.getRandom(2);
		
		// This is the waypoint where we will meet the enemy
		int targetWaypointIndex = RandomNumberGenerator.getRandom(numWaypoints);
		
		// This is where we will meet the enemy
		McuWaypoint targetWP = createWP(targetCoords, WaypointType.RECON_WAYPOINT.getName());
		
		// pattern orientation is the basic direction we are going
		int patternOrientation = RandomNumberGenerator.getRandom(360);

		// These are the waypoints before we will meet the enemy
		// For previous waypoints, zig zag towards the target		
		List<McuWaypoint> before = new ArrayList<McuWaypoint>();
		Coordinate lastCoord = targetWP.getPosition().copy();

		int angleAlternator = 0;
		double backAngle = MathUtils.adjustAngle(patternOrientation, -90);
		for (int i = 0; i < targetWaypointIndex; ++i)
		{
			double angle = MathUtils.adjustAngle(backAngle, (90 * angleAlternator * -1));
			
			int distance = 5 + RandomNumberGenerator.getRandom(5);
			distance *= 1000;
			Coordinate coord = MathUtils.calcNextCoord(lastCoord, angle, distance);
			McuWaypoint wp = createWP(coord, WaypointType.PATROL_WAYPOINT.getName());
			before.add(wp);
			
			lastCoord = coord;
			if (angleAlternator == 0)
			{
				angleAlternator = 1;
			}
			else
			{
				angleAlternator = 0;
			}
		}
		
		// These are the waypoints after we will meet the enemy
		// reverse the zig zag
        List<McuWaypoint> after = new ArrayList<McuWaypoint>();
		lastCoord = targetWP.getPosition().copy();

		angleAlternator = 0;
		int frontAngle = patternOrientation;
		for (int i = targetWaypointIndex + 1; i < numWaypoints; ++i)
		{
			double angle = MathUtils.adjustAngle(frontAngle, (90 * angleAlternator));

			int distance = 5 + RandomNumberGenerator.getRandom(5);
			distance *= 1000;
			Coordinate coord = MathUtils.calcNextCoord(lastCoord, angle, distance);
			McuWaypoint wp = createWP(coord, WaypointType.PATROL_WAYPOINT.getName());
			after.add(wp);
			
			lastCoord = coord;
			if (angleAlternator == 0)
			{
				angleAlternator = 1;
			}
			else
			{
				angleAlternator = 0;
			}
		}

		// Now form the complete list
		for (int i = before.size() - 1; i >= 0; --i)
		{
			waypoints.add(before.get(i));
		}
		
		waypoints.add(targetWP);
		
		for (McuWaypoint afterWp : after)
		{
			waypoints.add(afterWp);
		}
		

	}

	private McuWaypoint createWP(Coordinate coord, String wpName) throws PWCGException 
	{
		coord.setXPos(coord.getXPos() + xOffset);
		coord.setZPos(coord.getZPos() + zOffset);
		coord.setYPos(getFlightAlt());

		McuWaypoint wp = WaypointFactory.createPatrolWaypointType();
		wp.setTriggerArea(McuWaypoint.COMBAT_AREA);
		wp.setDesc(flight.getSquadron().determineDisplayName(campaign.getDate()), wpName);
		wp.setSpeed(waypointSpeed);

		wp.setPosition(coord);
		
		return wp;
	}

	@Override
	protected int determineFlightAltitude() 
	{
		int baseFlightAlt = 300;
		int randomAlt = RandomNumberGenerator.getRandom(300);
		
		return baseFlightAlt += randomAlt;
	}
	
}
