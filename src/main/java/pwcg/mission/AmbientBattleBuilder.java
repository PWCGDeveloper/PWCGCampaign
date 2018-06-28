package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAssaultGenerator;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.AssaultGeneratorFactory;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.target.TacticalTarget;
import pwcg.campaign.target.TargetDefinition;
import pwcg.campaign.target.TargetDefinitionBuilder;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.AssaultGenerator.BattleSize;

public class AmbientBattleBuilder
{
    private Mission mission = null;
    private Campaign campaign = null;
    private ICountry attackingCountry = null;
    private ICountry defendingCountry = null;

    private List<MissionBattle> battles = new ArrayList<MissionBattle>();

    public AmbientBattleBuilder (Campaign campaign, Mission mission)
    {
        this.mission = mission;
        this.campaign = campaign;
    }

    public List<MissionBattle> generateAmbientBattles() throws PWCGException 
    {
        int maxBattles = getMaxAmbientBattles();
        int numBattles = RandomNumberGenerator.getRandom(maxBattles+1);

        for (int i = 0; i < 10; ++i)
        {
            chooseSides();
            Coordinate battleLocation = getBattleLocation();
            if (battleLocation != null)
            {
                TargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder();
                TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinitionForAmbient(campaign, attackingCountry, defendingCountry, TacticalTarget.TARGET_ASSAULT, battleLocation);
    
                if (targetDefinition != null)
                {
                    IAssaultGenerator assaultGenerator = AssaultGeneratorFactory.createAssaultGenerator(campaign, mission, campaign.getDate());
                    MissionBattle missionBattle = assaultGenerator.generateAssault(targetDefinition, BattleSize.BATTLE_SIZE_SKIRMISH);
                    
                    battles.add(missionBattle);
                }
            }

            if (battles.size() >= numBattles)
            {
                break;
            }
        }
        
        return battles;
    }
    
    private void chooseSides()
    {
        ICountry alliedCountry = CountryFactory.makeMapReferenceCountry(Side.ALLIED);
        ICountry axisCountry = CountryFactory.makeMapReferenceCountry(Side.AXIS);

        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < 50)
        {
            attackingCountry = alliedCountry;
            defendingCountry = axisCountry;
        }
        else
        {
            attackingCountry = axisCountry;
            defendingCountry = alliedCountry;
        }
    }


    private int getMaxAmbientBattles() throws PWCGException
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

    
    private Coordinate getBattleLocation() throws PWCGException
    {
        CoordinateBox missionBorders = mission.getMissionFlightBuilder().getMissionBorders(3000);
        
        FrontLinesForMap frontLinesForMap =  PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        List<FrontLinePoint> closestFrontLinePoints = new ArrayList<>();
        for (FrontLinePoint frontLinePoint : frontLinesForMap.getFrontLines(defendingCountry.getSide()))
        {
            if (missionBorders.isInBox(frontLinePoint.getPosition()))
            {
                closestFrontLinePoints.add(frontLinePoint);
            }
        }

        int selectedIndex = 0;
        if (closestFrontLinePoints.size() > 0)
        {
            selectedIndex = RandomNumberGenerator.getRandom(closestFrontLinePoints.size());
            return closestFrontLinePoints.get(selectedIndex).getPosition();
        }
        
        return null;
    }
}
