package pwcg.campaign.io.json;

import java.io.File;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class CampaignPersonnelIOJson 
{
    public static  void writeJson(Campaign campaign) throws PWCGException
    {
        makePersonnelDir(campaign);
        writeSquadrons(campaign);
        writeReplacements(campaign);
    }
    
    private static void makePersonnelDir(Campaign campaign)
    {
        String campaignPersonnelDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Personnel\\";
        FileUtils.createDirIfNeeded(campaignPersonnelDir);
        
        String campaignReplacementsDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Personnel\\Replacements\\";
        FileUtils.createDirIfNeeded(campaignReplacementsDir);
    }

    private static void writeSquadrons(Campaign campaign) throws PWCGException
    {
        String campaignPersonnelDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Personnel\\";
        JsonWriter<SquadronMembers> jsonWriterSquadrons = new JsonWriter<>();
        for (SquadronPersonnel squadronPersonnel : campaign.getPersonnelManager().getAllSquadronPersonnel())
        {
            SquadronMembers squadronMembersToWrite = squadronPersonnel.getSquadronMembers();
            jsonWriterSquadrons.writeAsJson(squadronMembersToWrite, campaignPersonnelDir, squadronPersonnel.getSquadron().getSquadronId() + ".json");
        }
    }

    private static void writeReplacements(Campaign campaign) throws PWCGException
    {
        String campaignReplacementDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Personnel\\Replacements\\";
        JsonWriter<PersonnelReplacementsService> jsonWriterReplacements = new JsonWriter<>();
        for (PersonnelReplacementsService replacements : campaign.getPersonnelManager().getAllPersonnelReplacements())
        {
            jsonWriterReplacements.writeAsJson(replacements, campaignReplacementDir, replacements.getServiceId() + ".json");
        }
    }

    public static void readJson(Campaign campaign) throws PWCGException
    {
        readSquadrons(campaign);
        readReplacements(campaign);
    }

    private static void readSquadrons(Campaign campaign) throws PWCGException
    {
        String campaignPersonnelDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Personnel\\";
        List<File> jsonFiles = FileUtils.getFilesWithFilter(campaignPersonnelDir, ".json");
        for (File jsonFile : jsonFiles)
        {
            readSquadron(campaign, campaignPersonnelDir, jsonFile);
        }
    }

    private static void readSquadron(Campaign campaign, String campaignPersonnelDir, File jsonFile) throws PWCGException
    {
        JsonObjectReader<SquadronMembers> jsoReader = new JsonObjectReader<>(SquadronMembers.class);
        SquadronMembers squadronMembers = jsoReader.readJsonFile(campaignPersonnelDir, jsonFile.getName());
        
        int squadronId = Integer.valueOf(FileUtils.stripFileExtension(jsonFile.getName()));
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
        SquadronPersonnel squadronPersonnel = new SquadronPersonnel(campaign, squadron);
        squadronPersonnel.setSquadronMembers(squadronMembers);
        
        campaign.getPersonnelManager().addPersonnelForSquadron(squadronPersonnel);
    }

    private static void readReplacements(Campaign campaign) throws PWCGException
    {
        String campaignReplacementDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Personnel\\Replacements\\";
        List<File> jsonFiles = FileUtils.getFilesWithFilter(campaignReplacementDir, ".json");
        for (File jsonFile : jsonFiles)
        {
            JsonObjectReader<PersonnelReplacementsService> jsoReader = new JsonObjectReader<>(PersonnelReplacementsService.class);
            PersonnelReplacementsService replacements = jsoReader.readJsonFile(campaignReplacementDir, jsonFile.getName());
            
            int serviceId = Integer.valueOf(FileUtils.stripFileExtension(jsonFile.getName()));
            campaign.getPersonnelManager().addPersonnelReplacementsService(serviceId, replacements);
        }
    }
}
