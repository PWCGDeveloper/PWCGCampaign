package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAssaultGenerator;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.AssaultInformation;
import pwcg.mission.Mission;

public abstract class AssaultGenerator  implements IAssaultGenerator
{    
    protected Campaign campaign;
    protected Mission mission;
    protected Date date;
    protected TargetDefinition targetDefinition;
    protected BattleSize battleSize;
    protected List<AssaultInformation> assaultInformationElements = new ArrayList<>();
    protected static int DISTANCE_BETWEEN_COMBATANTS = 500;

    protected abstract void generateAssaultComponent(Coordinate groundPosition) throws PWCGException;

    public AssaultGenerator(Campaign campaign, Mission mission, Date date)
    {
        this.campaign = campaign;
        this.mission = mission;
        this.date = date;
    }
    
    @Override
    public AssaultInformation generateAssault(TargetDefinition targetDefinition, BattleSize battleSize) throws PWCGException 
    {
        this.targetDefinition = targetDefinition;
        this.battleSize = battleSize;

        int numAssaults = determineNumberOfAssaultSegments(battleSize);        
        int centerFrontIndex = determineCenterOfBattle();
        generateMiniAssaultOnEachIndex(targetDefinition, numAssaults, centerFrontIndex);
        
        int index = RandomNumberGenerator.getRandom(assaultInformationElements.size());
        mission.registerAssault(assaultInformationElements.get(index));
        return assaultInformationElements.get(index);
    }

    private int determineNumberOfAssaultSegments(BattleSize battleSize)
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int numAssaults = productSpecific.getNumAssaultSegments(battleSize);
        return numAssaults;
    }
    
    protected int determineCenterOfBattle() throws PWCGException
    {
        FrontLinesForMap frontLineMarker =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(date);
        List<FrontLinePoint> frontLines = frontLineMarker.getFrontLines(targetDefinition.getTargetCountry().getSide());
        int centerFrontIndex = frontLineMarker.findIndexForClosestPosition(targetDefinition.getTargetPosition(), targetDefinition.getTargetCountry().getSide());
        if (centerFrontIndex < 10)
        {
            centerFrontIndex = 10;
        }

        if (centerFrontIndex > (frontLines.size() - 10))
        {
            centerFrontIndex = frontLines.size() - 10;
        }
        
        return centerFrontIndex;
    }

    private void generateMiniAssaultOnEachIndex(TargetDefinition targetDefinition, int numAssaults, int centerFrontIndex) throws PWCGException
    {
        int startFrontIndex = centerFrontIndex;
        int finishFrontIndex = centerFrontIndex;
        if (numAssaults > 1)
        {
            startFrontIndex = centerFrontIndex - (numAssaults / 2);
            finishFrontIndex = startFrontIndex + numAssaults;
        }
        
        FrontLinesForMap frontLineMarker =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(date);
        for (int thisIndex = startFrontIndex; thisIndex <= finishFrontIndex; ++thisIndex)
        {
            Coordinate battleCoordinate = frontLineMarker.getCoordinates(thisIndex, targetDefinition.getTargetCountry().getSide());
            generateAssaultComponent (battleCoordinate);
        }
    }
    
    protected Coordinate getDefensePositionAcrossFromAssaultingUnit(AssaultInformation assaultInformation, Coordinate unitPosition) throws PWCGException
    {
        double angleToDefensePosition = MathUtils.calcAngle(assaultInformation.getAssaultPosition(), assaultInformation.getDefensePosition());
        double distanceToDefensePosition = MathUtils.calcDist(assaultInformation.getAssaultPosition(), assaultInformation.getDefensePosition()) + DISTANCE_BETWEEN_COMBATANTS;
        Coordinate destinationPositionForThisUnit = MathUtils.calcNextCoord(unitPosition, angleToDefensePosition, distanceToDefensePosition);
        return destinationPositionForThisUnit;
    }
    
    protected void registerAssaultSegment(AssaultInformation assaultInformation)
    {
        assaultInformationElements.add(assaultInformation);
    }
    
    protected boolean determineIsPlayer()
    {
        if (battleSize == BattleSize.BATTLE_SIZE_TINY)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
