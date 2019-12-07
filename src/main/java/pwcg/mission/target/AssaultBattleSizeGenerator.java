package pwcg.mission.target;

import pwcg.campaign.Campaign;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.BattleSize;

public class AssaultBattleSizeGenerator
{
    public static BattleSize createAssaultBattleSize(Campaign campaign, TargetDefinition targetDefinition) throws PWCGException 
    {
        BattleSize battleSize = BattleSize.BATTLE_SIZE_TINY;      
        if (targetDefinition.isPlayerTarget())
        {
            ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
            String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
            if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
            {
                battleSize = BattleSize.BATTLE_SIZE_SKIRMISH;      
            }
            else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
            {
                battleSize = BattleSize.BATTLE_SIZE_ASSAULT;
            }
            else
            {
                int roll = RandomNumberGenerator.getRandom(100);
                if (roll < 80)
                {
                    battleSize = BattleSize.BATTLE_SIZE_ASSAULT;
                }
                else
                {
                    battleSize = BattleSize.BATTLE_SIZE_OFFENSIVE;
                }
            }
        }

        return battleSize;
    }
}
