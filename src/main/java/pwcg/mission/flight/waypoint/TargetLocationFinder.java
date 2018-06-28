package pwcg.mission.flight.waypoint;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
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
    
    public Coordinate createTargetCoordinates() throws PWCGException
    {       
        Coordinate pickupLocation = findLocationNearTown();      
        if (pickupLocation == null)
        {
            pickupLocation = findLocationNearBridge();       
        }
        
        if (pickupLocation == null)
        {
            pickupLocation = findLocationNearFront();       
        }
        
        if (pickupLocation == null)
        {
            throw new PWCGException("Unable to find spy extraction point for mission");       
        }

        return pickupLocation;
    }
    
    private Coordinate findLocationNearTown() throws PWCGException
    {
        Coordinate pickupLocation = null;

        GroupManager groupManager = PWCGContextManager.getInstance().getCurrentMap().getGroupManager();
        PWCGLocation town = groupManager.getTownFinder().findTownForSideWithinRadius(side, campaign.getDate(), targetGeneralLocation, radius);
        if (town != null)
        {
            pickupLocation = town.getPosition();
            int directionFromTown = RandomNumberGenerator.getRandom(360);
            pickupLocation = MathUtils.calcNextCoord(pickupLocation, directionFromTown, 3000.0);
        }
        
        return pickupLocation;
    }
    
    private Coordinate findLocationNearBridge() throws PWCGException
    {
        Coordinate pickupLocation = null;
        GroupManager groupManager = PWCGContextManager.getInstance().getCurrentMap().getGroupManager();
        Bridge bridge = groupManager.getBridgeFinder().findBridgeForSideWithinRadius(side, campaign.getDate(), targetGeneralLocation, radius);
        if (bridge != null)
        {
            pickupLocation = bridge.getPosition();
            int directionFromBridge = RandomNumberGenerator.getRandom(360);
            pickupLocation = MathUtils.calcNextCoord(pickupLocation, directionFromBridge, 1000.0);
        }
        
        return pickupLocation;
    }
    
    private Coordinate findLocationNearFront() throws PWCGException
    {
        FrontLinesForMap frontLinesForMap = PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());        
        Coordinate pickupLocation = frontLinesForMap.findPositionBehindLinesForSide(targetGeneralLocation, radius, 10000, 20000, side);
        return pickupLocation;
    }

}
