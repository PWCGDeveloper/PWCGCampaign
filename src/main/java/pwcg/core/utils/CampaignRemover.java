package pwcg.core.utils;

import pwcg.campaign.context.PWCGContext;

public class CampaignRemover
{
    private FileUtils fileUtils = new FileUtils();

    public void deleteCampaign(String campaignName)
    {
        String campaignDirPath = PWCGContext.getInstance().getDirectoryManager().getPwcgCampaignsDir() + "\\" + campaignName;

        fileUtils.deleteRecursive(campaignDirPath);
    }

}
