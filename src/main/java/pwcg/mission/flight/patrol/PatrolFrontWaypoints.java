package pwcg.mission.flight.patrol;

import java.util.List;

import pwcg.campaign.api.IMissionAltitudeGenerator;
import pwcg.campaign.factory.MissionAltitudeGeneratorFactory;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.waypoint.PathAlongFront;
import pwcg.mission.flight.waypoint.PathAlongFrontData;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class PatrolFrontWaypoints extends WaypointGeneratorBase
{
	public PatrolFrontWaypoints(Coordinate startCoords, 
					    	  	Coordinate targetCoords, 
					    	  	Flight flight,
					    	  	Mission mission) throws PWCGException 
	{
 		super(startCoords, targetCoords, flight, mission);
	}

    protected void createTargetWaypoints(Coordinate startPosition) throws PWCGException  
    {   
        PathAlongFrontData pathAlongFrontData = buildPathAlongFrontData(startPosition);
        PathAlongFront pathAlongFront = new PathAlongFront();
        List<Coordinate> patrolCoordinates = pathAlongFront.createPathAlongFront(pathAlongFrontData);
        
        for (Coordinate patrolCoordinate : patrolCoordinates)
        {
            McuWaypoint waypoint = createWP(patrolCoordinate.copy());
            waypoint.setTargetWaypoint(true);
            waypoint.setName(WaypointType.PATROL_WAYPOINT.getName());
            waypoints.add(waypoint);
        }
    }

    protected PathAlongFrontData buildPathAlongFrontData(Coordinate startPosition) throws PWCGException
    {
        int patrolDistanceBase = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.PatrolDistanceBaseKey) * 1000;
        patrolDistanceBase = patrolDistanceBase * 2;
        int patrolDistanceRandom = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.PatrolDistanceRandomKey) * 1000;

        int depthOfPenetrationMax = 3000;
        int depthOfPenetration = RandomNumberGenerator.getRandom(depthOfPenetrationMax);
        depthOfPenetration += 500;
        depthOfPenetration *= -1;
                
        PathAlongFrontData pathAlongFrontData = new PathAlongFrontData();
        pathAlongFrontData.setDate(campaign.getDate());
        pathAlongFrontData.setOffsetTowardsEnemy(depthOfPenetration);
        pathAlongFrontData.setPathDistance(patrolDistanceBase / 2);
        pathAlongFrontData.setRandomDistanceMax(patrolDistanceRandom / 2);
        pathAlongFrontData.setTargetGeneralLocation(startPosition);
        pathAlongFrontData.setReturnAlongRoute(true);
        pathAlongFrontData.setSide(flight.getSquadron().determineSquadronCountry(campaign.getDate()).getSide().getOppositeSide());
        
        return pathAlongFrontData;
    }

	private McuWaypoint createWP(Coordinate coord) throws PWCGException 
	{
		McuWaypoint wp = WaypointFactory.createPatrolWaypointType();
		wp.setTriggerArea(McuWaypoint.COMBAT_AREA);
		wp.setSpeed(waypointSpeed);
		wp.setPosition(coord);
		wp.getPosition().setYPos(getFlightAlt());

		return wp;
	}
	

    @Override
    protected int determineFlightAltitude() throws PWCGException 
    {
        IMissionAltitudeGenerator missionAltitudeGenerator = MissionAltitudeGeneratorFactory.createMissionAltitudeGeneratorFactory();
        if (flight.getFlightType() == FlightTypes.LOW_ALT_PATROL)
        {
            return missionAltitudeGenerator.getLowAltitudePatrolAltitude();
        }

        return missionAltitudeGenerator.flightAltitude(campaign);
    }

}
