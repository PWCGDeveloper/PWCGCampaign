package pwcg.mission.flight.offensive;

import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class OffensiveWaypointsFront extends OffensiveWaypoints
{
	protected int offset = 0;
	
	public OffensiveWaypointsFront(Coordinate startCoords, 
					  	  Coordinate targetCoords, 
					  	  Flight flight,
					  	  Mission mission) throws PWCGException 
{
		super(startCoords, targetCoords, flight, mission);
	
		offset = 10000 + RandomNumberGenerator.getRandom(8000);
	}
	
	
	/**
	 * @throws PWCGException 
	 * @throws PWCGException 
	 * @
	 */
	protected void createOffensivePatrolWaypoints() throws PWCGException, PWCGException  
	{	    
		// Recursively create waypoints for the appropriate distance
	    // Since offensive patrols fo in one direction we can make them twice as long
		int PatrolDistanceBase = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.PatrolDistanceBaseKey) * 2;
		int PatrolDistanceRandom = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.PatrolDistanceRandomKey);
		int base = PatrolDistanceBase  * 1000;
		int randomizer = PatrolDistanceRandom * 1000;
		int random1 = RandomNumberGenerator.getRandom(randomizer);
		double patrolDistance = base + random1;
		
	      // For offensive patrols we want enemy front lines
        Side enemySide = flight.getCountry().getSide().getOppositeSide();
        
		FrontLinesForMap frontLineMarker =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
		int startFrontIndex = frontLineMarker.findIndexForClosestPosition(targetCoords, enemySide);

        
        // At northern edge - go south
        boolean goNorth = goNorth(startFrontIndex, enemySide);
        
        // Get the starting point
		List<FrontLinePoint> frontLines = frontLineMarker.getFrontLines(enemySide);
		startFrontIndex = correctStartFrontIndex( startFrontIndex, frontLines);

		// Advance along the front
        int numToAdvance = 2;
        int frontIndex = getNextFrontIndex(startFrontIndex, goNorth, numToAdvance, enemySide);

		// First target waypoint
        Coordinate coord = frontLines.get(frontIndex).getPosition().copy();
        Coordinate wpCoordinate = createWPCoordinate(coord, frontLines.get(frontIndex).getOrientation(campaign.getDate()));
		McuWaypoint tFirstWP = super.createWP(wpCoordinate);
		tFirstWP.setTargetWaypoint(true);
		tFirstWP.setName(WaypointType.RECON_WAYPOINT.getName());
		waypoints.add(tFirstWP);
				
		
	      // Set the next waypoint
        int nextFrontIndex = 0;
        if (goNorth)
        {
            nextFrontIndex = frontIndex - 3;
        }
        else
        {
            nextFrontIndex = frontIndex + 3;
        }
        
		createNextWaypoint(tFirstWP, patrolDistance, nextFrontIndex, goNorth, enemySide);
	}
	
	
	/**
	 * @param lastWP
	 * @param patrolDistance
	 * @param frontIndex
	 * @param goNorth
	 * @throws PWCGException 
	 * @
	 */
	private void createNextWaypoint(McuWaypoint lastWP, 
		    						double patrolDistance,
		    						int frontIndex,
		    						boolean goNorth,
		    						Side enemySide) throws PWCGException  
	{
		FrontLinesForMap frontLineMarker =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
		List<FrontLinePoint> frontLines = frontLineMarker.getFrontLines(enemySide);
		
		// Don't go past the edge of the map
		if (isEdgeOfMap(frontIndex, goNorth, enemySide))
		{
            return;
		}
		
		// Set the next waypoint just a bit beyond the direction we are going
		Coordinate coord = frontLines.get(frontIndex).getPosition().copy();
		
		// Or is that too far and we cut it short?
		// If yes then this is the last waypoint
        Coordinate wpCoordinate = createWPCoordinate(coord, frontLines.get(frontIndex).getOrientation(campaign.getDate()));
		double distance = MathUtils.calcDist(wpCoordinate, lastWP.getPosition());
		McuWaypoint referenceWaypoint = null;
		if (distance > 4000)
		{
    		patrolDistance -= distance;
    		
    		McuWaypoint waypoint = super.createWP(wpCoordinate);
    		waypoint.setTargetWaypoint(true);
    		waypoints.add(waypoint);
    		
    		referenceWaypoint = waypoint;
		}
		else
		{
		    referenceWaypoint = lastWP;
		}
		
		// Set the next waypoint
		int nextFrontIndex = frontIndex + 3;
		if (goNorth)
		{
			nextFrontIndex = frontIndex - 3;
		}
		
		
		if (patrolDistance > 1000.0)
		{
			createNextWaypoint(referenceWaypoint, patrolDistance, nextFrontIndex, goNorth, enemySide);
		}
		else
		{
			return;
		}
	}

	protected Coordinate createWPCoordinate(Coordinate coord, double frontOrientation) throws PWCGException 
	{
        double angle = MathUtils.adjustAngle(frontOrientation, 180.0);
	    Coordinate adjusted = MathUtils.calcNextCoord(coord, angle, offset);
		
	    return adjusted;
	}
}
