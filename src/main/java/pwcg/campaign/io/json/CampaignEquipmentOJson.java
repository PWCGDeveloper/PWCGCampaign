package pwcg.campaign.io.json;

import java.io.File;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.resupply.depot.EquipmentDepot;
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
        String campaignEquipmentDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Equipment\\";
        FileUtils.createDirIfNeeded(campaignEquipmentDir);
        
        String campaignReplacementsDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Equipment\\Replacements\\";
        FileUtils.createDirIfNeeded(campaignReplacementsDir);
    }

    private static void writeSquadrons(Campaign campaign) throws PWCGException
    {
        String campaignEquipmentReplacementDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Equipment\\";
        JsonWriter<Equipment> jsonWriterEquipment = new JsonWriter<>();
        for (Integer squadronId : campaign.getEquipmentManager().getEquipmentAllSquadrons().keySet())
        {
            Equipment squadronEquipment = campaign.getEquipmentManager().getEquipmentForSquadron(squadronId);
            jsonWriterEquipment.writeAsJson(squadronEquipment, campaignEquipmentReplacementDir, squadronId + ".json");
        }
    }

    private static void writeReplacements(Campaign campaign) throws PWCGException
    {
        String campaignEquipmentReplacementDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Equipment\\Replacements\\";
        JsonWriter<EquipmentDepot> jsonWriterReplacements = new JsonWriter<>();
        for (Integer serviceId : campaign.getEquipmentManager().getServiceIdsForDepots())
        {
            EquipmentDepot replacementEquipment = campaign.getEquipmentManager().getEquipmentDepotForService(serviceId);
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
        String campaignEquipmentDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Equipment\\";
        List<File> jsonFiles = FileUtils.getFilesWithFilter(campaignEquipmentDir, ".json");
        for (File jsonFile : jsonFiles)
        {
            JsonObjectReader<Equipment> jsoReader = new JsonObjectReader<>(Equipment.class);
            Equipment squadronEquipment = jsoReader.readJsonFile(campaignEquipmentDir, jsonFile.getName());
            int squadronId = Integer.valueOf(FileUtils.stripFileExtension(jsonFile.getName()));
            campaign.getEquipmentManager().addEquipmentForSquadron(squadronId, squadronEquipment);
            // Allocate ID codes in case none were present
            // Can be removed after the next campaign compatibility break
            for (EquippedPlane equippedPlane : squadronEquipment.getActiveEquippedPlanes().values())
            {
                if (equippedPlane.getAircraftIdCode() == null)
                    PWCGContext.getInstance().getPlaneMarkingManager().allocatePlaneIdCode(campaign, squadronId, squadronEquipment, equippedPlane);
            }
        }
    }

    private static void readReplacements(Campaign campaign) throws PWCGException
    {
        String campaignEquipmentReplacementDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Equipment\\Replacements\\";
        List<File> jsonFiles = FileUtils.getFilesWithFilter(campaignEquipmentReplacementDir, ".json");
        for (File jsonFile : jsonFiles)
        {
            JsonObjectReader<EquipmentDepot> jsoReader = new JsonObjectReader<>(EquipmentDepot.class);
            EquipmentDepot replacementEquipemnt = jsoReader.readJsonFile(campaignEquipmentReplacementDir, jsonFile.getName());
            int serviceId = Integer.valueOf(FileUtils.stripFileExtension(jsonFile.getName()));
            campaign.getEquipmentManager().addEquipmentDepotForService(serviceId, replacementEquipemnt);
        }
    }
}
