package pwcg.mission.flight.recon;

import java.util.ArrayList;
import java.util.List;

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

public class ReconFrontWaypointsFactory
{
    private IFlight flight;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();

    public ReconFrontWaypointsFactory(IFlight flight) throws PWCGException
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
            waypoint.setName(WaypointType.RECON_WAYPOINT.getName());
            targetWaypoints.add(waypoint);
        }
        return targetWaypoints;
    }

    private PathAlongFrontData buildPathAlongFrontData(Coordinate startPosition) throws PWCGException
    {
        int patrolDistanceBase = flight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.PatrolDistanceBaseKey) * 1000;
        patrolDistanceBase = patrolDistanceBase * 2;
        int patrolDistanceRandom = flight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.PatrolDistanceRandomKey) * 1000;

        int depthOfPenetrationMax = 3000;
        int depthOfPenetration = RandomNumberGenerator.getRandom(depthOfPenetrationMax);
        depthOfPenetration += 500;
        depthOfPenetration *= -1;
                
        PathAlongFrontData pathAlongFrontData = new PathAlongFrontData();
        pathAlongFrontData.setMission(flight.getMission());
        pathAlongFrontData.setDate(flight.getCampaign().getDate());
        pathAlongFrontData.setOffsetTowardsEnemy(depthOfPenetration);
        pathAlongFrontData.setPathDistance(patrolDistanceBase);
        pathAlongFrontData.setRandomDistanceMax(patrolDistanceRandom);
        pathAlongFrontData.setTargetGeneralLocation(startPosition);
        pathAlongFrontData.setReturnAlongRoute(false);
        pathAlongFrontData.setSide(flight.getSquadron().determineSquadronCountry(flight.getCampaign().getDate()).getSide().getOppositeSide());
        
        return pathAlongFrontData;
    }

    private McuWaypoint createWP(Coordinate coord) throws PWCGException 
	{
        int xOffset = 100 - RandomNumberGenerator.getRandom(200);
        int zOffset = 100 - RandomNumberGenerator.getRandom(200);

		coord.setXPos(coord.getXPos() + xOffset);
		coord.setZPos(coord.getZPos() + zOffset);
		coord.setYPos(flight.getFlightInformation().getAltitude());

		McuWaypoint wp = WaypointFactory.createReconWaypointType();
		wp.setTriggerArea(McuWaypoint.TARGET_AREA);
		wp.setSpeed(flight.getFlightCruisingSpeed());
		wp.setPosition(coord);
		
		return wp;
	}
}
