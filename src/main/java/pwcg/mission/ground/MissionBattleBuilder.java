package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.ground.builder.AssaultBuilder;
import pwcg.mission.ground.builder.IBattleBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.target.AssaultDefinitionRange;

public class MissionBattleBuilder implements IBattleBuilder
{
    private static final int BATTLE_BOX_EXPANSION_FROM_MISSION = 5000;
    private Mission mission = null;
    private Campaign campaign = null;

    private List<GroundUnitCollection> battles = new ArrayList<>();

    public MissionBattleBuilder (Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
    }


    @Override
    public List<GroundUnitCollection> generateBattle() throws PWCGException
    {
        return generateLandBattles();
    }

    private List<GroundUnitCollection> generateLandBattles() throws PWCGException 
    {
        int maxBattles = getMaxBattles();
        if (maxBattles > 0)
        {
            int numBattles = getNumBattles(maxBattles);
            if (numBattles > 0)
            {
                List<Coordinate> assaultPositions = getBattleLocations(numBattles);
                generateAssaultsAtLocations(assaultPositions);
            }
        }
        
        return battles;
    }

    private int getNumBattles(int maxBattles)
    {
        int numBattles = RandomNumberGenerator.getRandom(maxBattles+1);
        if (numBattles == 0)
        {
            numBattles = 1;
        }
        return numBattles;
    }

    private void generateAssaultsAtLocations(List<Coordinate> assaultPositions) throws PWCGException
    {
        for (Coordinate assaultPosition : assaultPositions)
        {
            GroundUnitCollection assaultUnitCollection = AssaultBuilder.generateAssault(mission, assaultPosition);
            battles.add(assaultUnitCollection);
        }
    }

    private int getMaxBattles() throws PWCGException
    {
        int maxBattles = 1;
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_ULTRA_LOW))
        {
            maxBattles = 1;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            maxBattles = 1;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            maxBattles = 2;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            maxBattles = 3;
        }
        
        maxBattles = reduceNumberOfbattlesForSmallFront(maxBattles);

        return maxBattles;
    }

    private int reduceNumberOfbattlesForSmallFront(int maxBattles) throws PWCGException
    {
        List<FrontLinePoint> battleBoxFrontLinePoints = getFrontLineIndecesInBox();
        int maxBattlesForFrontPoints = AssaultDefinitionRange.calculateMaximumAssultsForFrontPoints(battleBoxFrontLinePoints.size(), maxBattles);
        if (maxBattlesForFrontPoints < maxBattles)
        {
            maxBattles = maxBattlesForFrontPoints;
        }
        return maxBattles;
    }

    private List<Coordinate> getBattleLocations(int numBattles) throws PWCGException
    {
        List<FrontLinePoint> battleBoxFrontLinePoints = getFrontLineIndecesInBox();
        if (battleBoxFrontLinePoints.isEmpty())
        {
            return new ArrayList<>();
        }

        List<Coordinate> battlePositionIndeces = new ArrayList<>();
        List<Integer> indecesToUse = AssaultDefinitionRange.calculateFirstBattlePoints(battleBoxFrontLinePoints.size(), numBattles);
        for (int indexToUse : indecesToUse)
        {
            if (indexToUse > 0 && indexToUse < battleBoxFrontLinePoints.size())
            {
                battlePositionIndeces.add(battleBoxFrontLinePoints.get(indexToUse).getPosition());
            }
        }

        return battlePositionIndeces;
    }

    private List<FrontLinePoint> getFrontLineIndecesInBox() throws PWCGException
    {
        List<FrontLinePoint> battleBoxFrontLinePoints = new ArrayList<>();
        CoordinateBox battleLocationBorders = CoordinateBox.copy(mission.getMissionBorders());
        battleLocationBorders.expandBox(BATTLE_BOX_EXPANSION_FROM_MISSION);

        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getFrontLinesForMap(campaign.getDate());
        for (FrontLinePoint frontLinePoint : frontLinesForMap.getFrontLines(Side.ALLIED))
        {
            if (battleLocationBorders.isInBox(frontLinePoint.getPosition()))
            {
                battleBoxFrontLinePoints.add(frontLinePoint);
            }
        }

        return battleBoxFrontLinePoints;
    }
}
