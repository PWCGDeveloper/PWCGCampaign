package pwcg.campaign.io.json;

import java.io.File;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignAces;
import pwcg.campaign.CampaignData;
import pwcg.campaign.CampaignLogs;
import pwcg.campaign.context.PWCGDirectoryManager;
import pwcg.core.exception.PWCGException;

public class CampaignIOJson 
{

    public static  void writeJson(Campaign campaign) throws PWCGException
    {
        String campaignDir = PWCGDirectoryManager.getInstance().getPwcgCampaignsDir() + campaign.getName() + "\\";
        File campaignDirFile = new File(campaignDir);
        if (!campaignDirFile.exists())
        {
            campaignDirFile.mkdir();
        }
        
        JsonWriter<CampaignData> jsonWriter1 = new JsonWriter<>();
        jsonWriter1.writeAsJson(campaign.getCampaignData(), campaignDir, "Campaign.json");
        
        CampaignPersonnelIOJson.writeJson(campaign);
        
        JsonWriter<CampaignAces> jsonWriter3 = new JsonWriter<>();
        jsonWriter3.writeAsJson(campaign.getPersonnelManager().getCampaignAces(), campaignDir, "CampaignAces.json");
        
        JsonWriter<CampaignLogs> jsonWriter4 = new JsonWriter<>();
        jsonWriter4.writeAsJson(campaign.getCampaignLogs(), campaignDir, "CampaignLog.json");
    }

    public static void readJson(Campaign campaign) throws PWCGException
    {
        String campaignDir = PWCGDirectoryManager.getInstance().getPwcgCampaignsDir() + campaign.getName() + "\\";

        JsonObjectReader<CampaignData> jsoReader1 = new JsonObjectReader<>(CampaignData.class);
        CampaignData campaignData = jsoReader1.readJsonFile(campaignDir, "Campaign.json"); 
        campaign.setCampaignData(campaignData);

        CampaignPersonnelIOJson.readJson(campaign);

        JsonObjectReader<CampaignAces> jsoReader3 = new JsonObjectReader<>(CampaignAces.class);
        CampaignAces campaignAces = jsoReader3.readJsonFile(campaignDir, "CampaignAces.json"); 
        campaign.getPersonnelManager().setCampaignAces(campaignAces);

        JsonObjectReader<CampaignLogs> jsonWriter4 = new JsonObjectReader<>(CampaignLogs.class);
        CampaignLogs campaignLogs = jsonWriter4.readJsonFile(campaignDir, "CampaignLog.json"); 
        campaign.setCampaignLogs(campaignLogs);
    }
}
