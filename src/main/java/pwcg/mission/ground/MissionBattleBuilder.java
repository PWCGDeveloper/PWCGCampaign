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
import pwcg.mission.ground.org.GroundUnitCollection;

public class MissionBattleBuilder
{
    private Mission mission = null;
    private Campaign campaign = null;

    private List<GroundUnitCollection> battles = new ArrayList<>();

    public MissionBattleBuilder (Campaign campaign, Mission mission)
    {
        this.mission = mission;
        this.campaign = campaign;
    }

    public List<GroundUnitCollection> generateBattles() throws PWCGException 
    {
        int maxBattles = getMaxBattles();
        int numBattles = RandomNumberGenerator.getRandom(maxBattles+1);

        for (int i = 0; i < 10; ++i)
        {
            Coordinate battleLocation = getBattleLocation();
            if (battleLocation != null)
            {
                GroundUnitCollection battleUnitCollection = AssaultBuilder.generateAssault(mission, battleLocation);
                battles.add(battleUnitCollection);
            }

            if (battles.size() >= numBattles)
            {
                break;
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

    private Coordinate getBattleLocation() throws PWCGException
    {
        CoordinateBox missionBorders = mission.getMissionBorders().expandBox(3000);
        
        FrontLinesForMap frontLinesForMap =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        List<FrontLinePoint> closestFrontLinePoints = new ArrayList<>();
        for (FrontLinePoint frontLinePoint : frontLinesForMap.getFrontLines(Side.ALLIED))
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
