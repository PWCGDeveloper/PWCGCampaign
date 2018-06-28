package pwcg.core.utils;

import pwcg.campaign.context.PWCGDirectoryManager;

public class CampaignRemover
{
    private FileUtils fileUtils = new FileUtils();

    public void deleteCampaign(String campaignName)
    {
        String campaignDirPath = PWCGDirectoryManager.getInstance().getPwcgCampaignsDir() + "\\" + campaignName;

        fileUtils.deleteRecursive(campaignDirPath);
    }

}
