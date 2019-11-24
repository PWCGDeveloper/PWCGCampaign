package pwcg.campaign;

import pwcg.core.utils.FileUtils;

public class CampaignDirectoryBuilder
{
    public static void initializeCampaignDirectories(Campaign campaign) 
    {
        FileUtils fileUtils = new FileUtils();
        
        String campaignCombatReportsDir = campaign.getCampaignPath() + "CombatReports\\";
        fileUtils.createConfigDirIfNeeded(campaignCombatReportsDir);

        String campaignConfigDir = campaign.getCampaignPath() + "config\\";
        fileUtils.createConfigDirIfNeeded(campaignConfigDir);
        
        String campaignEquipmentDir = campaign.getCampaignPath() + "Equipment\\";
        fileUtils.createConfigDirIfNeeded(campaignEquipmentDir);
        
        String campaignMissionDataDir = campaign.getCampaignPath() + "MissionData\\";
        fileUtils.createConfigDirIfNeeded(campaignMissionDataDir);
        
        String campaignPersonnelDir = campaign.getCampaignPath() + "Personnel\\";
        fileUtils.createConfigDirIfNeeded(campaignPersonnelDir);
    }
}
