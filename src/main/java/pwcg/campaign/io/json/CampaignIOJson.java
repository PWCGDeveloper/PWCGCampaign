package pwcg.campaign.io.json;

import java.io.File;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignAces;
import pwcg.campaign.CampaignData;
import pwcg.campaign.CampaignLogs;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;

public class CampaignIOJson 
{

    public static  void writeJson(Campaign campaign) throws PWCGException
    {
        String campaignDir = PWCGContextManager.getInstance().getDirectoryManager().getPwcgCampaignsDir() + campaign.getName() + "\\";
        File campaignDirFile = new File(campaignDir);
        if (!campaignDirFile.exists())
        {
            campaignDirFile.mkdir();
        }
        
        JsonWriter<CampaignData> jsonCampaignDataWriter = new JsonWriter<>();
        jsonCampaignDataWriter.writeAsJson(campaign.getCampaignData(), campaignDir, "Campaign.json");
        
        JsonWriter<CampaignAces> jsonAcesWriter = new JsonWriter<>();
        jsonAcesWriter.writeAsJson(campaign.getPersonnelManager().getCampaignAces(), campaignDir, "CampaignAces.json");
        
        JsonWriter<CampaignLogs> jsonCampaignLogsWriter = new JsonWriter<>();
        jsonCampaignLogsWriter.writeAsJson(campaign.getCampaignLogs(), campaignDir, "CampaignLog.json");

        CampaignPersonnelIOJson.writeJson(campaign);
        CampaignEquipmentOJson.writeJson(campaign);
    }

    public static void readJson(Campaign campaign) throws PWCGException
    {
        String campaignDir = PWCGContextManager.getInstance().getDirectoryManager().getPwcgCampaignsDir() + campaign.getName() + "\\";

        JsonObjectReader<CampaignData> jsoReader1 = new JsonObjectReader<>(CampaignData.class);
        CampaignData campaignData = jsoReader1.readJsonFile(campaignDir, "Campaign.json"); 
        campaign.setCampaignData(campaignData);

        JsonObjectReader<CampaignAces> jsoReader3 = new JsonObjectReader<>(CampaignAces.class);
        CampaignAces campaignAces = jsoReader3.readJsonFile(campaignDir, "CampaignAces.json"); 
        campaign.getPersonnelManager().setCampaignAces(campaignAces);

        JsonObjectReader<CampaignLogs> jsonWriter4 = new JsonObjectReader<>(CampaignLogs.class);
        CampaignLogs campaignLogs = jsonWriter4.readJsonFile(campaignDir, "CampaignLog.json"); 
        campaign.setCampaignLogs(campaignLogs);

        CampaignPersonnelIOJson.readJson(campaign);
        CampaignEquipmentOJson.readJson(campaign);
    }
}
