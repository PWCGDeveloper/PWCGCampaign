package pwcg.mission.flight.intercept;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroupManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.StrategicTargetBuilder;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;

public class InterceptPlayerCoordinateGenerator
{
    public static double CLOSE_ENOUGH_FOR_INTERCEPT = 80000.0;

    private Campaign campaign;
    private Mission mission;
    private FlightTypes flightType;
    private Squadron squadron;
    private Coordinate targetCoordinates = null;

    public InterceptPlayerCoordinateGenerator(Campaign campaign, Mission mission, FlightTypes flightType, Squadron squadron)
    {
        this.campaign = campaign;
        this.mission = mission;
        this.squadron = squadron;
        this.flightType = flightType;
    }
    
    public void createTargetCoordinates() throws PWCGException
    {
        if (flightType == FlightTypes.HOME_DEFENSE)
        {
            getStrategicTargetCoordinates();
        }
        else if (flightType == FlightTypes.LOW_ALT_CAP)
        {
            getFrontTargetCoordinates();
        }
        else
        {
            createPlayerInterceptLocation();
        }
    }

    private void getStrategicTargetCoordinates() throws PWCGException 
    {
        StrategicTargetBuilder targetManager = new StrategicTargetBuilder();        
        TargetDefinition targetDefinition = targetManager.getStrategicTarget(campaign, mission, squadron);
        targetCoordinates = targetDefinition.getTargetLocation();
    }
    
    private void getFrontTargetCoordinates() throws PWCGException 
    {
        FrontLinesForMap frontLinesForMap = PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        List<FrontLinePoint> frontPointsPatrol = frontLinesForMap.findClosestFrontPositionsForSide(
                squadron.determineCurrentPosition(campaign.getDate()), 
                CLOSE_ENOUGH_FOR_INTERCEPT, 
                squadron.determineEnemySide());
        
        if (frontPointsPatrol.size() == 0)
        {
            frontPointsPatrol = frontLinesForMap.findClosestFrontPositionsForSide(
                    squadron.determineCurrentPosition(campaign.getDate()), 
                    CLOSE_ENOUGH_FOR_INTERCEPT * 1.5, 
                    squadron.determineEnemySide());        
        }
   
        int selectedIndex = RandomNumberGenerator.getRandom(frontPointsPatrol.size());
        targetCoordinates = frontPointsPatrol.get(selectedIndex).getPosition().copy();
    }
    
    private void createPlayerInterceptLocation() throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        GroupManager groupManager = PWCGContextManager.getInstance().getCurrentMap().getGroupManager();
        Block block = groupManager.getBlockFinder().getBlockWithinRadius(squadron.determineCurrentPosition(campaign.getDate()), productSpecific.getInterceptRadius());
        targetCoordinates = block.getPosition();
    }

    public Coordinate getTargetCoordinates()
    {
        return targetCoordinates;
    }
}
