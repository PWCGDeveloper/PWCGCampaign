package pwcg.campaign.target.locator.targettype;

import java.util.Date;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.shipping.ShippingLane;
import pwcg.campaign.shipping.ShippingLaneManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;
import pwcg.mission.ground.builder.DrifterManager;
import pwcg.mission.target.TacticalTarget;

public class TargetTypeAvailability
{
    private Side side;
    private Date date;
    
    public TargetTypeAvailability(Side side, Date date)
    {
        this.side = side;
        this.date = date;
    }
    
    public double getTargetTypeAvailability(TacticalTarget targetType, Coordinate targetGeneralLocation, double maxDistance) throws PWCGException
    {        
        Coordinate closestInstance = null;
        if (targetType ==TacticalTarget.TARGET_TROOP_CONCENTRATION)
        {
            closestInstance = isFrontLinePositionAvailable(targetGeneralLocation);
        }
        else if (targetType ==TacticalTarget.TARGET_ASSAULT)
        {
            closestInstance = isFrontLinePositionAvailable(targetGeneralLocation);
        }
        else if (targetType ==TacticalTarget.TARGET_DEFENSE)
        {
            closestInstance = isFrontLinePositionAvailable(targetGeneralLocation);
        }
        else if (targetType ==TacticalTarget.TARGET_ARTILLERY)
        {
            closestInstance = isFrontLinePositionAvailable(targetGeneralLocation);
        }
        else if (targetType ==TacticalTarget.TARGET_TRANSPORT)
        {
            closestInstance = isBridgeAvailable(targetGeneralLocation);
            maxDistance +=  10000;
        }
        else if (targetType ==TacticalTarget.TARGET_TRAIN)
        {
            closestInstance = isRailroadStationAvailable(targetGeneralLocation);
            maxDistance +=  10000;
        }
        else if (targetType ==TacticalTarget.TARGET_AIRFIELD)
        {
            closestInstance = isAirfieldAvailable(targetGeneralLocation);
            maxDistance +=  20000;
        }
        else if (targetType ==TacticalTarget.TARGET_DRIFTER)
        {
            closestInstance = isDrifterAvailable(targetGeneralLocation);
            maxDistance +=  20000;
        }
        else if (targetType ==TacticalTarget.TARGET_SHIPPING)
        {
            closestInstance = isSeaLaneAvailable(targetGeneralLocation);
            maxDistance +=  40000;
        }
        else
        {
            throw new PWCGException ("Unconfigured target type at isTargetTypeAvailable: " + targetType);
        }
                
        if (closestInstance != null)
        {
            double distanceOfClosestInstanceToReference = MathUtils.calcDist(targetGeneralLocation, closestInstance);
            return distanceOfClosestInstanceToReference;
        }

        return PositionFinder.ABSURDLY_LARGE_DISTANCE;
    }

    private Coordinate isFrontLinePositionAvailable(Coordinate targetGeneralLocation) throws PWCGException
    {
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(date);
        FrontLinePoint frontLinePoint = frontLinesForMap.findClosestFrontPositionForSide(targetGeneralLocation, side);
        if (frontLinePoint != null)
        {
            return frontLinePoint.getPosition();
        }
        
        return null;
    }
    
    private Coordinate isSeaLaneAvailable(Coordinate targetGeneralLocation) throws PWCGException
    {
        ShippingLaneManager shippingLaneManager = PWCGContext.getInstance().getCurrentMap().getShippingLaneManager();
        ShippingLane shippingLane = shippingLaneManager.getClosestShippingLaneBySide(targetGeneralLocation, side);
        if (shippingLane != null)
        {
            return shippingLane.getShippingLaneBox().getCenter();
        }
        
        return null;
    }
    
    private Coordinate isBridgeAvailable(Coordinate targetGeneralLocation) throws PWCGException
    {
        GroupManager groupManager = PWCGContext.getInstance().getCurrentMap().getGroupManager();
        Bridge bridge = groupManager.getBridgeFinder().findClosestBridgeForSide(side, date, targetGeneralLocation);
        if (bridge != null)
        {
            return bridge.getPosition();
        }
        
        return null;
    }

    private Coordinate isRailroadStationAvailable(Coordinate targetGeneralLocation) throws PWCGException
    {
        GroupManager groupManager = PWCGContext.getInstance().getCurrentMap().getGroupManager();
        Block railroadStation = groupManager.getRailroadStationFinder().getClosestTrainPositionBySide(side, date, targetGeneralLocation);
        if (railroadStation != null)
        {
            return railroadStation.getPosition();
        }
        
        return null;
    }

    private Coordinate isAirfieldAvailable(Coordinate targetGeneralLocation) throws PWCGException
    {
        AirfieldManager airfieldManager = PWCGContext.getInstance().getCurrentMap().getAirfieldManager();
        IAirfield airfield = airfieldManager.getAirfieldFinder().findClosestAirfieldForSide(targetGeneralLocation, date, side);
        if (airfield != null)
        {
            return airfield.getPosition();
        }
        
        return null;
    }

    private Coordinate isDrifterAvailable(Coordinate targetGeneralLocation) throws PWCGException
    {
        DrifterManager drifterManager = PWCGContext.getInstance().getCurrentMap().getDrifterManager();
        PWCGLocation drifterLocation = drifterManager.getBargePositions().findClosestLocationForSide(targetGeneralLocation, date, side);
        if (drifterLocation != null)
        {
            return drifterLocation.getPosition();
        }
        
        return null;
    }
}
