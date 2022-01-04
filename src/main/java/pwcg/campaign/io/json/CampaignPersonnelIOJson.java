package pwcg.campaign.io.json;

import java.io.File;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

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
        for (CompanyPersonnel squadronPersonnel : campaign.getPersonnelManager().getAllCompanyPersonnel())
        {
            writeSquadron(campaign, squadronPersonnel.getSquadron().getCompanyId());
        }
    }

    public static void writeSquadron(Campaign campaign, int squadronId) throws PWCGException
    {
        CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(squadronId);
        CrewMembers squadronMembersToWrite = squadronPersonnel.getCrewMembers();

        String campaignPersonnelDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Personnel\\";
        PwcgJsonWriter<CrewMembers> jsonWriterSquadrons = new PwcgJsonWriter<>();
        jsonWriterSquadrons.writeAsJson(squadronMembersToWrite, campaignPersonnelDir, squadronPersonnel.getSquadron().getCompanyId() + ".json");
    }

    private static void writeReplacements(Campaign campaign) throws PWCGException
    {
        String campaignReplacementDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Personnel\\Replacements\\";
        PwcgJsonWriter<PersonnelReplacementsService> jsonWriterReplacements = new PwcgJsonWriter<>();
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
        JsonObjectReader<CrewMembers> jsoReader = new JsonObjectReader<>(CrewMembers.class);
        CrewMembers squadronMembers = jsoReader.readJsonFile(campaignPersonnelDir, jsonFile.getName());
        
        int squadronId = Integer.valueOf(FileUtils.stripFileExtension(jsonFile.getName()));
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(squadronId);
        if (squadron != null)
        {
            CompanyPersonnel squadronPersonnel = new CompanyPersonnel(campaign, squadron);
            squadronPersonnel.setCrewMembers(squadronMembers);
            campaign.getPersonnelManager().addPersonnelForCompany(squadronPersonnel);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "No squadron found for squadron id " + squadronId + " in campaign " + campaign.getName());
        }
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
