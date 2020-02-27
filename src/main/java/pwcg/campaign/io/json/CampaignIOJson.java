package pwcg.campaign.io.json;

import java.io.File;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignAces;
import pwcg.campaign.CampaignData;
import pwcg.campaign.CampaignLogs;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;

public class CampaignIOJson 
{

    public static  void writeJson(Campaign campaign) throws PWCGException
    {
        String campaignDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\";
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
        String campaignDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\";

        JsonObjectReader<CampaignData> jsoReader1 = new JsonObjectReader<>(CampaignData.class);
        CampaignData campaignData = jsoReader1.readJsonFile(campaignDir, "Campaign.json"); 
        campaign.setCampaignData(campaignData);

        if (campaign.isValidCampaignForProduct())
        {
            readCampaignAces(campaign, campaignDir);
            readCampaignLogs(campaign, campaignDir);
    
            CampaignPersonnelIOJson.readJson(campaign);
            CampaignEquipmentOJson.readJson(campaign);
        }
    }

    private static void readCampaignAces(Campaign campaign, String campaignDir) throws PWCGException
    {
        JsonObjectReader<CampaignAces> jsoReader3 = new JsonObjectReader<>(CampaignAces.class);
        CampaignAces campaignAces = jsoReader3.readJsonFile(campaignDir, "CampaignAces.json"); 
        campaign.getPersonnelManager().setCampaignAces(campaignAces);
    }

    private static void readCampaignLogs(Campaign campaign, String campaignDir) throws PWCGException
    {
        try
        {
            JsonObjectReader<CampaignLogs> jsonWriter4 = new JsonObjectReader<>(CampaignLogs.class);
            CampaignLogs campaignLogs = jsonWriter4.readJsonFile(campaignDir, "CampaignLog.json"); 
            campaign.setCampaignLogs(campaignLogs);
        }
        catch (Exception exp)
        {
            File file = new File(campaignDir + "\\CampaignLog.json");
            if (file.exists())
            {
                boolean wasDeleted = file.delete();
                System.out.println("was deleted " + wasDeleted);
            }
        }
    }
}
