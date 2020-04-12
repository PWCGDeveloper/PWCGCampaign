package pwcg.campaign;

import pwcg.core.utils.FileUtils;

public class CampaignDirectoryBuilder
{
    public static void initializeCampaignDirectories(Campaign campaign) 
    {
        String campaignCombatReportsDir = campaign.getCampaignPath() + "CombatReports\\";
        FileUtils.createDirIfNeeded(campaignCombatReportsDir);

        String campaignConfigDir = campaign.getCampaignPath() + "config\\";
        FileUtils.createDirIfNeeded(campaignConfigDir);
        
        String campaignEquipmentDir = campaign.getCampaignPath() + "Equipment\\";
        FileUtils.createDirIfNeeded(campaignEquipmentDir);
        
        String campaignMissionDataDir = campaign.getCampaignPath() + "MissionData\\";
        FileUtils.createDirIfNeeded(campaignMissionDataDir);
        
        String campaignPersonnelDir = campaign.getCampaignPath() + "Personnel\\";
        FileUtils.createDirIfNeeded(campaignPersonnelDir);
    }
}
