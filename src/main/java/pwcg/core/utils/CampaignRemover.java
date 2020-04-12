package pwcg.core.utils;

import pwcg.campaign.context.PWCGContext;

public class CampaignRemover
{
    public static void deleteCampaign(String campaignName)
    {
        String campaignDirPath = PWCGContext.getInstance().getDirectoryManager().getPwcgCampaignsDir() + "\\" + campaignName;
        FileUtils.deleteRecursive(campaignDirPath);
    }

}
