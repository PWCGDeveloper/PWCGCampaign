package pwcg.mission.flight.offensive;

import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.PathAlongFront;
import pwcg.mission.flight.waypoint.PathAlongFrontData;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class OffensiveWaypointsFront extends OffensiveWaypoints
{
    public OffensiveWaypointsFront(Coordinate startCoords, Coordinate targetCoords, Flight flight, Mission mission) throws PWCGException
    {
        super(startCoords, targetCoords, flight, mission);
    }

    @Override
    protected void createTargetWaypoints(Coordinate ingressPosition) throws PWCGException
    {
        PathAlongFrontData pathAlongFrontData = buildPathAlongFrontData(ingressPosition);
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

        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int depthOfPenetration = productSpecific.getVerySmallMissionRadius();
        depthOfPenetration += RandomNumberGenerator.getRandom(productSpecific.getVerySmallMissionRadius());
        depthOfPenetration *= -1;

        PathAlongFrontData pathAlongFrontData = new PathAlongFrontData();
        pathAlongFrontData.setMission(mission);
        pathAlongFrontData.setDate(campaign.getDate());
        pathAlongFrontData.setOffsetTowardsEnemy(depthOfPenetration);
        pathAlongFrontData.setPathDistance(patrolDistanceBase / 2);
        pathAlongFrontData.setRandomDistanceMax(patrolDistanceRandom / 2);
        pathAlongFrontData.setTargetGeneralLocation(startPosition);
        pathAlongFrontData.setReturnAlongRoute(true);
        pathAlongFrontData.setSide(flight.getSquadron().determineSquadronCountry(campaign.getDate()).getSide().getOppositeSide());

        return pathAlongFrontData;
    }
}
