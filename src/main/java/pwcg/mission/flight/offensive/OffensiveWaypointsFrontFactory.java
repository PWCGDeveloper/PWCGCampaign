package pwcg.mission.flight.offensive;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.flight.waypoint.patterns.PathAlongFront;
import pwcg.mission.flight.waypoint.patterns.PathAlongFrontData;
import pwcg.mission.mcu.McuWaypoint;

public class OffensiveWaypointsFrontFactory
{
    private IFlight flight;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();
    
    public OffensiveWaypointsFrontFactory(IFlight flight) throws PWCGException
    {
        this.flight = flight;
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet.addWaypoint(ingressWaypoint);
        
        List<McuWaypoint> waypoints = createTargetWaypoints(ingressWaypoint.getPosition());
        missionPointSet.addWaypoints(waypoints);

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        missionPointSet.addWaypoint(egressWaypoint);

        return missionPointSet;
    }

    private List<McuWaypoint> createTargetWaypoints(Coordinate ingressPosition) throws PWCGException
    {
        List<McuWaypoint> targetWaypoints = new ArrayList<McuWaypoint>();

        PathAlongFrontData pathAlongFrontData = buildPathAlongFrontData(ingressPosition);
        PathAlongFront pathAlongFront = new PathAlongFront();
        List<Coordinate> patrolCoordinates = pathAlongFront.createPathAlongFront(pathAlongFrontData);

        for (Coordinate patrolCoordinate : patrolCoordinates)
        {
            McuWaypoint waypoint = createWP(patrolCoordinate.copy());
            waypoint.setTargetWaypoint(true);
            waypoint.setName(WaypointType.PATROL_WAYPOINT.getName());
            targetWaypoints.add(waypoint);
        }
        return targetWaypoints;
    }

    private PathAlongFrontData buildPathAlongFrontData(Coordinate startPosition) throws PWCGException
    {
        Campaign campaign = flight.getCampaign();
        
        int patrolDistanceBase = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.PatrolDistanceBaseKey) * 1000;
        patrolDistanceBase = patrolDistanceBase * 2;
        int patrolDistanceRandom = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.PatrolDistanceRandomKey) * 1000;

        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int depthOfPenetration = productSpecific.getVerySmallMissionRadius();
        depthOfPenetration += RandomNumberGenerator.getRandom(productSpecific.getVerySmallMissionRadius());
        depthOfPenetration *= -1;

        PathAlongFrontData pathAlongFrontData = new PathAlongFrontData();
        pathAlongFrontData.setMission(flight.getMission());
        pathAlongFrontData.setDate(campaign.getDate());
        pathAlongFrontData.setOffsetTowardsEnemy(depthOfPenetration);
        pathAlongFrontData.setPathDistance(patrolDistanceBase / 2);
        pathAlongFrontData.setRandomDistanceMax(patrolDistanceRandom / 2);
        pathAlongFrontData.setTargetGeneralLocation(startPosition);
        pathAlongFrontData.setReturnAlongRoute(true);
        pathAlongFrontData.setSide(flight.getSquadron().determineSquadronCountry(campaign.getDate()).getSide().getOppositeSide());

        return pathAlongFrontData;
    }

    private McuWaypoint createWP(Coordinate frontPosition) throws PWCGException 
    {
        frontPosition.setYPos(flight.getFlightInformation().getAltitude());
        McuWaypoint wp = WaypointFactory.createPatrolWaypointType();
        wp.setTriggerArea(McuWaypoint.TARGET_AREA);
        wp.setSpeed(flight.getFlightPlanes().getFlightCruisingSpeed());
        wp.setPosition(frontPosition);
        
        return wp;
    }
}
