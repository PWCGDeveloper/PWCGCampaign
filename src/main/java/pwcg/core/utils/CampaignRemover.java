package pwcg.core.utils;

import pwcg.campaign.context.PWCGDirectoryUserManager;

public class CampaignRemover
{
    public static void deleteCampaign(String campaignName)
    {
        String campaignDirPath = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + "\\" + campaignName;
        FileUtils.deleteRecursive(campaignDirPath);
    }

}
