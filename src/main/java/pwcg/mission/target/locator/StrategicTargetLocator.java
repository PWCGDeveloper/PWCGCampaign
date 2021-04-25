package pwcg.mission.target.locator;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IFixedPosition;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroupManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class StrategicTargetLocator
{
    private FlightInformation flightInformation;
    private Coordinate targetLocation;
    private int preferredRadius;
    private List<TargetDefinition> availableTargets = new ArrayList<>();

    public StrategicTargetLocator(FlightInformation flightInformation, int preferredRadius, Coordinate referenceLocation) throws PWCGException
    {
        this.preferredRadius = preferredRadius;
        this.flightInformation = flightInformation;
        this.targetLocation = referenceLocation;
    }

    public List<TargetDefinition> getStrategicTargetAvailability() throws PWCGException
    {
        
        List<IFixedPosition> factoryTargets = getFactoryTargets();
        if (factoryTargets.size() > 0)
        {
            addAvailableTarget(TargetType.TARGET_FACTORY, factoryTargets);
        }

        List<IFixedPosition> railTargets = getRailTargets();
        if (railTargets.size() > 0)
        {
            addAvailableTarget(TargetType.TARGET_RAIL, railTargets);
        }

        List<IFixedPosition> portTargets = getPortTargets();
        if (portTargets.size() > 0)
        {
            addAvailableTarget(TargetType.TARGET_PORT, portTargets);
        }

        List<IFixedPosition> airfieldTargets = getAirfieldTargets();
        if (airfieldTargets.size() > 0)
        {
            addAvailableTarget(TargetType.TARGET_AIRFIELD, airfieldTargets);
        }

        List<IFixedPosition> cityTargets = getCityTargets();
        if (cityTargets.size() > 0)
        {
            addAvailableTarget(TargetType.TARGET_CITY, cityTargets);
        }
        
        return availableTargets;
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
                    if (block.determineCountryOnDate(flightInformation.getCampaign().getDate()).isNeutral())
                    {
                        continue;
                    }
    
                    if (block.determineCountryOnDate(flightInformation.getCampaign().getDate()).getSide() == flightInformation.getSquadron().determineEnemySide())
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
    
    private void addAvailableTarget(TargetType targetType, List<IFixedPosition> targets) throws PWCGException
    {
        for (IFixedPosition target : targets)
        {
            TargetDefinition targetDefinition = new TargetDefinition(targetType, target.getPosition(), target.getCountry(flightInformation.getCampaign().getDate()));
            availableTargets.add(targetDefinition);
        }
    }
}
