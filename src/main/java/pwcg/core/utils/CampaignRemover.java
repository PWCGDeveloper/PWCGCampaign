package pwcg.core.utils;

import pwcg.campaign.context.PWCGContextManager;

public class CampaignRemover
{
    private FileUtils fileUtils = new FileUtils();

    public void deleteCampaign(String campaignName)
    {
        String campaignDirPath = PWCGContextManager.getInstance().getDirectoryManager().getPwcgCampaignsDir() + "\\" + campaignName;

        fileUtils.deleteRecursive(campaignDirPath);
    }

}
