package pwcg.campaign.io.json;

import java.io.File;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquipmentReplacement;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class CampaignEquipmentOJson 
{
    public static  void writeJson(Campaign campaign) throws PWCGException
    {
        makeEquipmentDir(campaign);
        writeSquadrons(campaign);
        writeReplacements(campaign);
    }
    
    private static void makeEquipmentDir(Campaign campaign)
    {
        String campaignEquipmentDir = PWCGContextManager.getInstance().getDirectoryManager().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Equipment\\";
        File equipmentDirFile = new File(campaignEquipmentDir);
        if (!equipmentDirFile.exists())
        {
            equipmentDirFile.mkdir();
        }
        
        String campaignReplacementsDir = PWCGContextManager.getInstance().getDirectoryManager().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Equipment\\Replacements\\";
        File replacementDirFile = new File(campaignReplacementsDir);
        if (!replacementDirFile.exists())
        {
            replacementDirFile.mkdir();
        }
    }

    private static void writeSquadrons(Campaign campaign) throws PWCGException
    {
        String campaignEquipmentReplacementDir = PWCGContextManager.getInstance().getDirectoryManager().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Equipment\\";
        JsonWriter<Equipment> jsonWriterEquipment = new JsonWriter<>();
        for (Integer squadronId : campaign.getEquipmentManager().getEquipmentAllSquadrons().keySet())
        {
            Equipment squadronEquipment = campaign.getEquipmentManager().getEquipmentForSquadron(squadronId);
            jsonWriterEquipment.writeAsJson(squadronEquipment, campaignEquipmentReplacementDir, squadronId + ".json");
        }
    }

    private static void writeReplacements(Campaign campaign) throws PWCGException
    {
        String campaignEquipmentReplacementDir = PWCGContextManager.getInstance().getDirectoryManager().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Equipment\\Replacements\\";
        JsonWriter<EquipmentReplacement> jsonWriterReplacements = new JsonWriter<>();
        for (Integer serviceId : campaign.getEquipmentManager().getEquipmentReplacements().keySet())
        {
            EquipmentReplacement replacementEquipment = campaign.getEquipmentManager().getEquipmentReplacements().get(serviceId);
            jsonWriterReplacements.writeAsJson(replacementEquipment, campaignEquipmentReplacementDir, serviceId + ".json");
        }
    }

    public static void readJson(Campaign campaign) throws PWCGException
    {
        readSquadrons(campaign);
        readReplacements(campaign);
    }

    private static void readSquadrons(Campaign campaign) throws PWCGException
    {
        String campaignEquipmentDir = PWCGContextManager.getInstance().getDirectoryManager().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Equipment\\";
        FileUtils fileUtils = new FileUtils();
        List<File> jsonFiles = fileUtils.getFilesWithFilter(campaignEquipmentDir, ".json");
        for (File jsonFile : jsonFiles)
        {
            JsonObjectReader<Equipment> jsoReader = new JsonObjectReader<>(Equipment.class);
            Equipment squadronEquipment = jsoReader.readJsonFile(campaignEquipmentDir, jsonFile.getName());
            int squadronId = new Integer(FileUtils.stripFileExtension(jsonFile.getName()));
            campaign.getEquipmentManager().addEquipmentForSquadron(squadronId, squadronEquipment);
        }
    }

    private static void readReplacements(Campaign campaign) throws PWCGException
    {
        String campaignEquipmentReplacementDir = PWCGContextManager.getInstance().getDirectoryManager().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Equipment\\Replacements\\";
        FileUtils fileUtils = new FileUtils();
        List<File> jsonFiles = fileUtils.getFilesWithFilter(campaignEquipmentReplacementDir, ".json");
        for (File jsonFile : jsonFiles)
        {
            JsonObjectReader<EquipmentReplacement> jsoReader = new JsonObjectReader<>(EquipmentReplacement.class);
            EquipmentReplacement replacementEquipemnt = jsoReader.readJsonFile(campaignEquipmentReplacementDir, jsonFile.getName());
            int serviceId = new Integer(FileUtils.stripFileExtension(jsonFile.getName()));
            campaign.getEquipmentManager().addEquipmentReplacementsForService(serviceId, replacementEquipemnt);
        }
    }
}
