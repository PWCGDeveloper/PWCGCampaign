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
        int numBattles = RandomNumberGenerator.getRandom(maxBattles+1);

        if (numBattles > 0)
        {
            List<Coordinate> battleLocations = getBattleLocations(numBattles);
            for (Coordinate battleLocation : battleLocations)
            {
                GroundUnitCollection assaultUnitCollection = AssaultBuilder.generateAssault(mission, battleLocation);
                battles.add(assaultUnitCollection);
            }
        }
        
        return battles;
    }

    private int getMaxBattles() throws PWCGException
    {
        int maxBattles = 1;
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
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
        return maxBattles;
    }

    private List<Coordinate> getBattleLocations(int numBattles) throws PWCGException
    {
        List<FrontLinePoint> battleBoxFrontLinePoints = getFrontLineIndecesInBox();
        if (battleBoxFrontLinePoints.isEmpty())
        {
            return new ArrayList<>();
        }

        List<Integer> battlePositionIndeces = new ArrayList<>();
        for (int i = 0; i < numBattles; ++i)
        {
            int selectedIndex = getFrontIndexForBattle(battleBoxFrontLinePoints, battlePositionIndeces);
            if (selectedIndex != 0)
            {
                battlePositionIndeces.add(selectedIndex);
            }
        }
        
        List<Coordinate> battlePositions = new ArrayList<>();
        for (int battlePositionIndex : battlePositionIndeces)
        {
            battlePositions.add(battleBoxFrontLinePoints.get(battlePositionIndex).getPosition());
        }

        return battlePositions;
    }

    private List<FrontLinePoint> getFrontLineIndecesInBox() throws PWCGException
    {
        CoordinateBox battleLocationBorders = CoordinateBox.copy(mission.getMissionBorders());
        battleLocationBorders.expandBox(BATTLE_BOX_EXPANSION_FROM_MISSION);
        
        List<FrontLinePoint> battleBoxFrontLinePoints = new ArrayList<>();
        FrontLinesForMap frontLinesForMap =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        for (FrontLinePoint frontLinePoint : frontLinesForMap.getFrontLines(Side.ALLIED))
        {
            if (battleLocationBorders.isInBox(frontLinePoint.getPosition()))
            {
                battleBoxFrontLinePoints.add(frontLinePoint);
            }
        }
        return battleBoxFrontLinePoints;
    }

    private int getFrontIndexForBattle(List<FrontLinePoint> battleBoxFrontLinePoints, List<Integer> battlePositionIndeces)
    {
        List<FrontLinePoint> remainingBattleBoxFrontLinePoints = reduceBattleBoxFrontLinePoints(battleBoxFrontLinePoints, battlePositionIndeces);
        int selectedIndex = 0;
        if (remainingBattleBoxFrontLinePoints.size() > 0)
        {
            selectedIndex = RandomNumberGenerator.getRandom(remainingBattleBoxFrontLinePoints.size());
        }
        return selectedIndex;
    }

    private List<FrontLinePoint> reduceBattleBoxFrontLinePoints(List<FrontLinePoint> battleBoxFrontLinePoints, List<Integer> battlePositionIndeces)
    {
        List<FrontLinePoint> remainingBattleBoxFrontLinePoints = new ArrayList<>();
        for (int index = 0; index < battleBoxFrontLinePoints.size(); ++index)
        {
            if (!AssaultDefinitionRange.isInUse(index, battlePositionIndeces))
            {
                remainingBattleBoxFrontLinePoints.add(battleBoxFrontLinePoints.get(index));
            }
        }
        return remainingBattleBoxFrontLinePoints;
    }
}
