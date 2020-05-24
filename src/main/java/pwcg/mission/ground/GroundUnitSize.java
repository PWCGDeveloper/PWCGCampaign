package pwcg.mission.ground;

import pwcg.campaign.Campaign;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;

public enum GroundUnitSize
{
    GROUND_UNIT_SIZE_TINY,
    GROUND_UNIT_SIZE_LOW,
    GROUND_UNIT_SIZE_MEDIUM,
    GROUND_UNIT_SIZE_HIGH;
    
    public static GroundUnitSize calcNumUnitsByConfig(Campaign campaign) throws PWCGException 
    {
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_HIGH))
        {
            return GroundUnitSize.GROUND_UNIT_SIZE_HIGH;
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            return GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM;
        }
        else
        {
            return GroundUnitSize.GROUND_UNIT_SIZE_LOW;
        }
    }
}
