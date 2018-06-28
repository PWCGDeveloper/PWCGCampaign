package pwcg.mission.flight.attackhunt;

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
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class GroundAttackHuntWaypoints extends WaypointGeneratorBase
{
	protected int xOffset = 0;
	protected int zOffset = 0;
	
	public GroundAttackHuntWaypoints(Coordinate startCoords, 
					  	  Coordinate targetCoords, 
					  	  Flight flight,
					  	  Mission mission) throws PWCGException 
{
		super(startCoords, targetCoords, flight, mission);
	
        xOffset = 100 - RandomNumberGenerator.getRandom(200);
        zOffset = 100 - RandomNumberGenerator.getRandom(200);
	}

	
	/* (non-Javadoc)
	 * @see pwcg.mission.flight.recon.ReconWaypoints#createTargetWaypoints(pwcg.core.utils.Coordinate)
	 */
	protected void createTargetWaypoints(Coordinate startPosition) throws PWCGException  
	{	    
        createFirstWaypoint();
        setWaypointsNonFighterPriority();
	}

	protected void createFirstWaypoint() throws PWCGException  
	{
		// Recursively create waypoints for the appropriate distance
		int PatrolDistanceBase = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.PatrolDistanceBaseKey);
		int PatrolDistanceRandom = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.PatrolDistanceRandomKey);
		int base = PatrolDistanceBase  * 1000 / 4 * 3;
		int randomizer = PatrolDistanceRandom * 1000 / 4 * 3;
		int random1 = RandomNumberGenerator.getRandom(randomizer);
		double patrolDistance = base + random1;
		
	      // For recon patrols we want enemy front lines
        Side enemySide = flight.getCountry().getSide().getOppositeSide();
        
		FrontLinesForMap frontLineMarker =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
		int startFrontIndex = frontLineMarker.findIndexForClosestPosition(targetCoords, enemySide);

        boolean goNorth = goNorth(startFrontIndex, enemySide);

		int frontIndex = startFrontIndex + 1;
		if (goNorth)
		{
			frontIndex = startFrontIndex - 1;
		}
		else
		{
		    frontIndex = startFrontIndex + 1;
		}

		// First target waypoint
		McuWaypoint tFirstWP = createWP(targetCoords.copy());
		tFirstWP.setTargetWaypoint(true);
		tFirstWP.setName(WaypointType.RECON_WAYPOINT.getName());
		waypoints.add(tFirstWP);
				
		createNextWaypoint(tFirstWP, patrolDistance, frontIndex, goNorth, enemySide);
	}

	private void createNextWaypoint(McuWaypoint lastWP, 
		    						double patrolDistance,
		    						int frontIndex,
		    						boolean goNorth,
		    						Side enemySide) throws PWCGException  
	{
        // Don't go past the edge of the map
        if (isEdgeOfMap(frontIndex, goNorth, enemySide))
        {
            return;
        }
        
		FrontLinesForMap frontLineMarker =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
		List<FrontLinePoint> frontLines = frontLineMarker.getFrontLines(enemySide);
		
		// Set the next waypoint just a bit beyond the direction we are going
        double minDistanceToNextWP = 2000;
        double actualDistanceToNextWP = 0.0;
        
        int numToAdvance = 1;
        int nextFrontIndex = frontIndex;
        Coordinate nextWPCoordinate = null;
        while (actualDistanceToNextWP < minDistanceToNextWP)
        {
            nextFrontIndex = getNextFrontIndex(nextFrontIndex, goNorth, numToAdvance, enemySide);

            nextWPCoordinate = frontLines.get(nextFrontIndex).getPosition().copy();
            
            actualDistanceToNextWP = MathUtils.calcDist(nextWPCoordinate, lastWP.getPosition());
            
            if (isEdgeOfMap(frontIndex, goNorth, enemySide))
            {
                break;
            }
       }
        
		// Or is that too far and we cut it short?
		// If yes then this is the last waypoint
		double distance = MathUtils.calcDist(nextWPCoordinate, lastWP.getPosition());
		patrolDistance -= distance;
		
		McuWaypoint tNextWP = createWP(nextWPCoordinate);
		tNextWP.setTargetWaypoint(true);
		waypoints.add(tNextWP);
		
		if (patrolDistance > 0)
		{
			createNextWaypoint(tNextWP, patrolDistance, nextFrontIndex, goNorth, enemySide);
		}
		else
		{
			return;
		}
	}

	public McuWaypoint createWP(Coordinate coord) throws PWCGException 
	{
		coord.setXPos(coord.getXPos() + xOffset);
		coord.setZPos(coord.getZPos() + zOffset);
		coord.setYPos(getFlightAlt());

		McuWaypoint wp = WaypointFactory.createPatrolWaypointType();
		wp.setTriggerArea(McuWaypoint.TARGET_AREA);
		wp.setSpeed(waypointSpeed);
		wp.setPosition(coord);
		
		return wp;
	}


}
