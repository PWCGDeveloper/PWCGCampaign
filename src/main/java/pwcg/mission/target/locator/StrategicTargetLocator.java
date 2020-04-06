package pwcg.mission.target.locator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.IFixedPosition;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroupManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.target.TargetType;

public class StrategicTargetLocator
{
    private Coordinate targetLocation;
    private int preferredRadius;
    private Date date;
    private Side side;

    public StrategicTargetLocator(int preferredRadius, Side side, Date date, Coordinate referenceLocation) throws PWCGException
    {
        this.preferredRadius = preferredRadius;
        this.side = side;
        this.date = date;
        this.targetLocation = referenceLocation;
    }

    public Map<TargetType, List<IFixedPosition>> getStrategicTargetAvailability() throws PWCGException
    {
        Map<TargetType, List<IFixedPosition>> availableTargets = new HashMap<>();
        
        List<IFixedPosition> factoryTargets = getFactoryTargets();
        if (factoryTargets.size() > 0)
        {
            availableTargets.put(TargetType.TARGET_FACTORY, factoryTargets);
        }

        List<IFixedPosition> railTargets = getRailTargets();
        if (railTargets.size() > 0)
        {
            availableTargets.put(TargetType.TARGET_RAIL, railTargets);
        }

        List<IFixedPosition> portTargets = getPortTargets();
        if (portTargets.size() > 0)
        {
            availableTargets.put(TargetType.TARGET_PORT, portTargets);
        }

        List<IFixedPosition> airfieldTargets = getAirfieldTargets();
        if (airfieldTargets.size() > 0)
        {
            availableTargets.put(TargetType.TARGET_AIRFIELD, airfieldTargets);
        }

        List<IFixedPosition> cityTargets = getCityTargets();
        if (cityTargets.size() > 0)
        {
            availableTargets.put(TargetType.TARGET_CITY, cityTargets);
        }
        
        return availableTargets;
    }
    
    public IFixedPosition getStrategicTargetLocation(TargetType targetType) throws PWCGException
    {
        List<IFixedPosition> possibleTargets = new ArrayList<>();

        if (targetType == TargetType.TARGET_FACTORY)
        {
            possibleTargets = getFactoryTargets();
        }
        else if (targetType == TargetType.TARGET_RAIL)
        {
            possibleTargets = getRailTargets();
        }
        else if (targetType == TargetType.TARGET_CITY)
        {
            possibleTargets = getCityTargets();
        }
        else if (targetType == TargetType.TARGET_AIRFIELD)
        {
            possibleTargets = getAirfieldTargets();
        }
        else if (targetType == TargetType.TARGET_PORT)
        {
            possibleTargets = getPortTargets();
        }

        int index = RandomNumberGenerator.getRandom(possibleTargets.size());
        return possibleTargets.get(index);
    }

    private List<IFixedPosition> getFactoryTargets() throws PWCGException
    {
        List<IFixedPosition> possibleTargets;
        possibleTargets = getBlockTargets("factory");
        List<IFixedPosition> morePossibleTargets = getBlockTargets("industrial");
        possibleTargets.addAll(morePossibleTargets);
        return possibleTargets;
    }

    private List<IFixedPosition> getRailTargets() throws PWCGException
    {
        List<IFixedPosition> possibleTargets;
        possibleTargets = getBlockTargets("railway");
        List<IFixedPosition> morePossibleTargets = getBlockTargets("railroad");
        possibleTargets.addAll(morePossibleTargets);
        List<IFixedPosition> evenMorePossibleTargets = getBlockTargets("rwstation");
        possibleTargets.addAll(evenMorePossibleTargets);
        return possibleTargets;
    }

    private List<IFixedPosition> getCityTargets() throws PWCGException
    {
        List<IFixedPosition> possibleTargets;
        possibleTargets = getBlockTargets("block");
        List<IFixedPosition> morePossibleTargets = getBlockTargets("town");
        possibleTargets.addAll(morePossibleTargets);
        return possibleTargets;
    }

    private List<IFixedPosition> getAirfieldTargets() throws PWCGException
    {
        List<IFixedPosition> possibleTargets;
        possibleTargets = getBlockTargets("hangar");
        return possibleTargets;
    }

    private List<IFixedPosition> getPortTargets() throws PWCGException
    {
        List<IFixedPosition> possibleTargets;
        possibleTargets = getBlockTargets("port_");
        return possibleTargets;
    }
    
    private List<IFixedPosition> getBlockTargets(String blockType) throws PWCGException
    {
        List<IFixedPosition> targets = new ArrayList<IFixedPosition>();

        GroupManager groupManager = PWCGContext.getInstance().getCurrentMap().getGroupManager();
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
                        double distanceToTarget = MathUtils.calcDist(targetLocation, block.getPosition());
                        if (distanceToTarget < preferredRadius)
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
}
