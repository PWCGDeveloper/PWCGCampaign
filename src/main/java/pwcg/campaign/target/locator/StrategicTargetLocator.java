package pwcg.campaign.target.locator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IFixedPosition;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;
import pwcg.core.utils.RandomNumberGenerator;

public class StrategicTargetLocator
{
    private Coordinate referenceLocation;
    private int preferredRadius;
    private Date date;
    private Side side;
    private static final double minDistance = 40000.0;


    public StrategicTargetLocator(int preferredRadius, Side side, Date date, Coordinate referenceLocation) throws PWCGException
    {
        this.preferredRadius = preferredRadius;
        this.side = side;
        this.date = date;
        this.referenceLocation = referenceLocation;
    }

    public Map<TacticalTarget, List<IFixedPosition>> getStrategicTargetAvailability() throws PWCGException
    {
        Map<TacticalTarget, List<IFixedPosition>> availableTargets = new HashMap<>();
        
        List<IFixedPosition> factoryTargets = getFactoryTargets();
        if (factoryTargets.size() > 0)
        {
            availableTargets.put(TacticalTarget.TARGET_FACTORY, factoryTargets);
        }

        List<IFixedPosition> railTargets = getRailTargets();
        if (railTargets.size() > 0)
        {
            availableTargets.put(TacticalTarget.TARGET_RAIL, railTargets);
        }

        List<IFixedPosition> portTargets = getBlockTargets("port facility");
        if (portTargets.size() > 0)
        {
            availableTargets.put(TacticalTarget.TARGET_PORT, portTargets);
        }

        List<IFixedPosition> airfieldTargets = getAirfieldTargets();
        if (airfieldTargets.size() > 0)
        {
            availableTargets.put(TacticalTarget.TARGET_AIRFIELD, airfieldTargets);
        }

        List<IFixedPosition> cityTargets = getBlockTargets("block");
        if (cityTargets.size() > 0)
        {
            availableTargets.put(TacticalTarget.TARGET_CITY, cityTargets);
        }
        
        return availableTargets;
    }
    
    public IFixedPosition getStrategicTargetLocation(TacticalTarget targetType) throws PWCGException
    {
        List<IFixedPosition> possibleTargets = new ArrayList<>();

        if (targetType == TacticalTarget.TARGET_FACTORY)
        {
            possibleTargets = getFactoryTargets();
        }
        else if (targetType == TacticalTarget.TARGET_CITY)
        {
            possibleTargets = getBlockTargets("block");
        }
        else if (targetType == TacticalTarget.TARGET_AIRFIELD)
        {
            possibleTargets = getAirfieldTargets();
        }
        else if (targetType == TacticalTarget.TARGET_RAIL)
        {
            possibleTargets = getRailTargets();
        }
        else if (targetType == TacticalTarget.TARGET_PORT)
        {
            possibleTargets = getBlockTargets("port facility");
        }

        int index = RandomNumberGenerator.getRandom(possibleTargets.size());
        return possibleTargets.get(index);
    }

    private List<IFixedPosition> getRailTargets() throws PWCGException
    {
        List<IFixedPosition> possibleTargets;
        possibleTargets = getBlockTargets("railway");
        List<IFixedPosition> morePossibleTargets = getBlockTargets("railroad");
        possibleTargets.addAll(morePossibleTargets);
        return possibleTargets;
    }

    private List<IFixedPosition> getFactoryTargets() throws PWCGException
    {
        List<IFixedPosition> possibleTargets;
        possibleTargets = getBlockTargets("factory");
        List<IFixedPosition> morePossibleTargets = getBlockTargets("industrial");
        possibleTargets.addAll(morePossibleTargets);
        return possibleTargets;
    }

    private List<IFixedPosition> getBlockTargets(String blockType) throws PWCGException
    {
        List<IFixedPosition> targets = new ArrayList<IFixedPosition>();

        GroupManager groupManager = PWCGContextManager.getInstance().getCurrentMap().getGroupManager();
        List<Block> blocks = groupManager.getStandaloneBlocks();
        
        while (targets.size() == 0 && preferredRadius < PositionFinder.ABSURDLY_LARGE_DISTANCE)
        {
            for (Block block : blocks)
            {
                if (block.getModel().contains(blockType))
                {
                    if (block.createCountry(date).isNeutral())
                    {
                        continue;
                    }
    
                    if (block.createCountry(date).getSide() == side)
                    {
                        double distanceToTarget = MathUtils.calcDist(referenceLocation, block.getPosition());
                        if (distanceToTarget > minDistance && distanceToTarget < preferredRadius)
                        {
                            targets.add(block);
                        }
                    }
                }
            }
            preferredRadius += 20000;
        }

        return targets;
    }

    private List<IFixedPosition> getAirfieldTargets() throws PWCGException 
    {
        List<IFixedPosition> targets = new ArrayList<IFixedPosition>();
        List<IAirfield> targetFields = PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirFieldsForSide(date, side);
        while (targets.size() == 0 && preferredRadius < PositionFinder.ABSURDLY_LARGE_DISTANCE)
        {
            for (IAirfield targetField : targetFields)
            {
                double distanceToTarget = MathUtils.calcDist(referenceLocation, targetField.getPosition());
                if (distanceToTarget < preferredRadius)
                {
                    targets.add(targetField);
                }
            }
            preferredRadius += 20000;
        }
                
        return targets;
    }
}
