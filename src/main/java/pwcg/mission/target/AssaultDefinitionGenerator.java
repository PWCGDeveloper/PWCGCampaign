package pwcg.mission.target;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.BattleSize;

public class AssaultDefinitionGenerator
{    
    public static final int DISTANCE_BETWEEN_COMBATANTS = 200;

    protected Campaign campaign;
    protected List<AssaultDefinition> assaultInformationElements = new ArrayList<>();

    public AssaultDefinitionGenerator(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public List<AssaultDefinition> generateAssaultDefinition(TargetDefinition targetDefinition) throws PWCGException 
    {
        generateMiniAssaultOnEachIndex(targetDefinition);        
        return assaultInformationElements;
    }
    
    private void generateMiniAssaultOnEachIndex(TargetDefinition targetDefinition) throws PWCGException
    {
        BattleSize battleSize = AssaultBattleSizeGenerator.createAssaultBattleSize(campaign, targetDefinition);
        List<Integer> battleFrontIndeces = geFrontBattleIndeces(targetDefinition, battleSize);
        
        for (int battleIndex : battleFrontIndeces)
        {
            FrontLinesForMap frontLineMarker =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
            Coordinate defenseCoordinate = frontLineMarker.getCoordinates(battleIndex, targetDefinition.getTargetCountry().getSide());
            Coordinate assaultCoordinate = getAssaultPositionAcrossFromAssaultingUnit(targetDefinition, defenseCoordinate);

            AssaultDefinition assaultDefinition = new AssaultDefinition();
            assaultDefinition.setAssaultPosition(assaultCoordinate);
            assaultDefinition.setDefensePosition(defenseCoordinate);
            assaultDefinition.setBattleSize(battleSize);
            assaultDefinition.setTargetDefinition(targetDefinition);
            
            assaultInformationElements.add (assaultDefinition);
        }
    }
    
    private List<Integer> geFrontBattleIndeces(TargetDefinition targetDefinition, BattleSize battleSize) throws PWCGException
    {
        List<Integer> battleFrontIndeces = new ArrayList<>();
        int centerFrontIndex = determineCenterOfBattle(targetDefinition);
        int numAssaults = determineNumberOfAssaultSegments(battleSize);    
        
        int startFrontIndex = centerFrontIndex;
        int finishFrontIndex = centerFrontIndex;
        if (numAssaults > 1)
        {
            startFrontIndex = centerFrontIndex - (numAssaults / 2);
            finishFrontIndex = startFrontIndex + numAssaults;
        }
        
        for (int battleIndex = startFrontIndex; battleIndex <= finishFrontIndex; ++battleIndex)
        {
            battleFrontIndeces.add(battleIndex);
        }
        
        return battleFrontIndeces;
    }

    private int determineNumberOfAssaultSegments(BattleSize battleSize)
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int numAssaults = productSpecific.getNumAssaultSegments(battleSize);
        return numAssaults;
    }
    
    private int determineCenterOfBattle(TargetDefinition targetDefinition) throws PWCGException
    {
        FrontLinesForMap frontLineMarker =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
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
    
    
    private Coordinate getAssaultPositionAcrossFromAssaultingUnit(TargetDefinition targetDefinition, Coordinate defensePosition) throws PWCGException
    {
        FrontLinesForMap frontLineMarker =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        Coordinate assaultPosition = frontLineMarker.findClosestFrontCoordinateForSide(defensePosition, targetDefinition.getAttackingCountry().getSide());

        double angleFromDefensePosition = MathUtils.calcAngle(defensePosition, assaultPosition);
        Coordinate assaultStartPosition = MathUtils.calcNextCoord(defensePosition, angleFromDefensePosition, DISTANCE_BETWEEN_COMBATANTS);
        return assaultStartPosition;
    }
}
