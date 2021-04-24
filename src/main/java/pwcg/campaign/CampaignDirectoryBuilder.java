package pwcg.campaign;

import pwcg.core.utils.FileUtils;

public class CampaignDirectoryBuilder
{
    public static void initializeCampaignDirectories(Campaign campaign) 
    {
        String campaignCombatReportsDir = campaign.getCampaignPathAutoCreateDirectory() + "CombatReports\\";
        FileUtils.createDirIfNeeded(campaignCombatReportsDir);

        String campaignConfigDir = campaign.getCampaignPathAutoCreateDirectory() + "config\\";
        FileUtils.createDirIfNeeded(campaignConfigDir);
        
        String campaignEquipmentDir = campaign.getCampaignPathAutoCreateDirectory() + "Equipment\\";
        FileUtils.createDirIfNeeded(campaignEquipmentDir);
        
        String campaignMissionDataDir = campaign.getCampaignPathAutoCreateDirectory() + "MissionData\\";
        FileUtils.createDirIfNeeded(campaignMissionDataDir);
        
        String campaignPersonnelDir = campaign.getCampaignPathAutoCreateDirectory() + "Personnel\\";
        FileUtils.createDirIfNeeded(campaignPersonnelDir);
    }
}
