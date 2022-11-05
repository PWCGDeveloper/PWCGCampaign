package pwcg.mission.target.locator;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.GroupManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;

public class TargetLocationFinder
{
    private Campaign campaign;
    private Side side;
    private double radius;
    private Coordinate targetGeneralLocation;
    
    public TargetLocationFinder (Campaign campaign, Side side, Coordinate targetGeneralLocation, double radius)
    {
        this.campaign = campaign;
        this.side = side;
        this.targetGeneralLocation = targetGeneralLocation;
        this.radius = radius;
    }
    
    public Coordinate findTargetCoordinatesBehindEnemyLines() throws PWCGException
    {       
        Coordinate flightTargetCoordinates = findLocationNearTown();      
        if (flightTargetCoordinates == null)
        {
            flightTargetCoordinates = findLocationNearBridge();       
        }
        
        if (flightTargetCoordinates == null)
        {
            flightTargetCoordinates = findLocationBehindEnemyLines();       
        }
        
        if (flightTargetCoordinates == null)
        {
            throw new PWCGException("Unable to find suitable target point for mission");       
        }

        return flightTargetCoordinates;
    }
    
    public Coordinate findLocationNearTown() throws PWCGException
    {
        Coordinate flightTargetCoordinates = null;

        GroupManager groupManager = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getGroupManager();
        PWCGLocation town = groupManager.getTownFinder().findTownForSideWithinRadius(campaign.getCampaignMap(), side, campaign.getDate(), targetGeneralLocation, radius);
        if (town != null)
        {
            flightTargetCoordinates = town.getPosition();
            int directionFromTown = RandomNumberGenerator.getRandom(360);
            flightTargetCoordinates = MathUtils.calcNextCoord(campaign.getCampaignMap(), flightTargetCoordinates, directionFromTown, 3000.0);
        }
        
        return flightTargetCoordinates;
    }
    
    public Coordinate findLocationNearBridge() throws PWCGException
    {
        Coordinate flightTargetCoordinates = null;
        GroupManager groupManager = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getGroupManager();
        Bridge bridge = groupManager.getBridgeFinder().findBridgeForSideWithinRadius(side, campaign.getDate(), targetGeneralLocation, radius);
        if (bridge != null)
        {
            flightTargetCoordinates = bridge.getPosition();
            int directionFromBridge = RandomNumberGenerator.getRandom(360);
            flightTargetCoordinates = MathUtils.calcNextCoord(campaign.getCampaignMap(), flightTargetCoordinates, directionFromBridge, 1000.0);
        }
        
        return flightTargetCoordinates;
    }
    
    public Coordinate findLocationBehindEnemyLines() throws PWCGException
    {
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getFrontLinesForMap(campaign.getDate());        
        Coordinate flightTargetCoordinates = frontLinesForMap.findPositionBehindLinesForSide(campaign.getCampaignMap(), targetGeneralLocation, radius, 10000, 20000, side);
        return flightTargetCoordinates;
    }
    
    public Coordinate findLocationAtFront() throws PWCGException
    {
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getFrontLinesForMap(campaign.getDate());        
        FrontLinePoint targetCountryFrontPoint = frontLinesForMap.findCloseFrontPositionForSide(targetGeneralLocation, radius, side);
        return targetCountryFrontPoint.getPosition();
    }

}
