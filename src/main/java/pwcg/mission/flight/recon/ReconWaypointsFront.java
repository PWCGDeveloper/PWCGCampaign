package pwcg.mission.flight.recon;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.flight.waypoint.frontlines.PathAlongFront;
import pwcg.mission.flight.waypoint.frontlines.PathAlongFrontData;
import pwcg.mission.mcu.McuWaypoint;

public class ReconWaypointsFront extends ReconWaypoints
{
    private List<McuWaypoint> targetWaypoints = new ArrayList<McuWaypoint>();
    private int xOffset = 0;
    private int zOffset = 0;

    public ReconWaypointsFront(Flight flight) throws PWCGException 
    {
        super(flight);
        xOffset = 100 - RandomNumberGenerator.getRandom(200);
        zOffset = 100 - RandomNumberGenerator.getRandom(200);
    }

    protected List<McuWaypoint> createTargetWaypoints(Coordinate ingressPosition) throws PWCGException
    {
        PathAlongFrontData pathAlongFrontData = buildPathAlongFrontData(ingressPosition);
        PathAlongFront pathAlongFront = new PathAlongFront();
        List<Coordinate> patrolCoordinates = pathAlongFront.createPathAlongFront(pathAlongFrontData);
        
        for (Coordinate patrolCoordinate : patrolCoordinates)
        {
            McuWaypoint waypoint = createWP(patrolCoordinate.copy());
            waypoint.setTargetWaypoint(true);
            waypoint.setName(WaypointType.RECON_WAYPOINT.getName());
            targetWaypoints.add(waypoint);
        }
        return targetWaypoints;
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
        pathAlongFrontData.setMission(flight.getMission());
        pathAlongFrontData.setDate(campaign.getDate());
        pathAlongFrontData.setOffsetTowardsEnemy(depthOfPenetration);
        pathAlongFrontData.setPathDistance(patrolDistanceBase);
        pathAlongFrontData.setRandomDistanceMax(patrolDistanceRandom);
        pathAlongFrontData.setTargetGeneralLocation(startPosition);
        pathAlongFrontData.setReturnAlongRoute(false);
        pathAlongFrontData.setSide(flight.getSquadron().determineSquadronCountry(campaign.getDate()).getSide().getOppositeSide());
        
        return pathAlongFrontData;
    }

	public McuWaypoint createWP(Coordinate coord) throws PWCGException 
	{
		coord.setXPos(coord.getXPos() + xOffset);
		coord.setZPos(coord.getZPos() + zOffset);
		coord.setYPos(flight.getFlightAltitude());

		McuWaypoint wp = WaypointFactory.createReconWaypointType();
		wp.setTriggerArea(McuWaypoint.TARGET_AREA);
		wp.setSpeed(flight.getFlightCruisingSpeed());
		wp.setPosition(coord);
		
		return wp;
	}
}
