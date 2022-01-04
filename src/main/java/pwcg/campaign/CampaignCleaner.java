package pwcg.campaign;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.company.CompanyViability;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberNames;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.medals.MedalManager;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class CampaignCleaner
{
    private Campaign campaign;
    
    public CampaignCleaner(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public void cleanDataFiles() throws PWCGException
    {
        generateMissingDepos();
        removeUnwantedSquadronFiles();
        removeDuplicateCrewMembers();
        checkDuplicateCrewMembers();
        checkCrewMemberKeys();
        cleanMedals();
    }

    private void removeUnwantedSquadronFiles() throws PWCGException
    {
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> squadronsToStaff = squadronManager.getActiveCompanies(campaign.getDate());
        for (Company squadron : squadronsToStaff)
        {
            if (!CompanyViability.isCompanyActive(squadron, campaign.getDate()))
            {
                if (campaign.getPersonnelManager().getCompanyPersonnel(squadron.getCompanyId()) == null)
                {
                    String campaignPersonnelDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Personnel\\";
                    File squadronPersonnelFile = new File(campaignPersonnelDir + squadron.getCompanyId() + ".json");
                    if (squadronPersonnelFile.exists())
                    {
                        squadronPersonnelFile.delete();
                    }
                    
                    String campaignEquipmentDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Equipment\\";
                    File campaignEquipmentFile = new File(campaignEquipmentDir + squadron.getCompanyId() + ".json");
                    if (campaignEquipmentFile.exists())
                    {
                        campaignEquipmentFile.delete();
                    }
                }
            }
       }
    }

    private void generateMissingDepos() throws PWCGException
    {
        generatePersonnelDepos();
        generateEquipmentDepos();
    }

    private void generatePersonnelDepos() throws PWCGException
    {
        for (ArmedService armedService : ArmedServiceFinder.getArmedServicesForDate(campaign))
        {
            CampaignPersonnelManager personnelManager = campaign.getPersonnelManager();
            if (!personnelManager.hasPersonnelReplacements(armedService.getServiceId()))
            {
                personnelManager.createPersonnelReplacements(armedService);
            }
        }
    }
    
    private void generateEquipmentDepos() throws PWCGException
    {
        for (ArmedService armedService : ArmedServiceFinder.getArmedServicesForDate(campaign))
        {
            CampaignEquipmentManager equipmentManager = campaign.getEquipmentManager();
            if (!equipmentManager.hasEquipmentDepo(armedService.getServiceId()))
            {
                equipmentManager.createEquipmentDepot(armedService);
            }
        }
    }
    
    private void removeDuplicateCrewMembers() throws PWCGException
    {
        CampaignPersonnelManager personnelManager = campaign.getPersonnelManager();
        Map<Integer, CompanyPersonnel> allPersonnel = personnelManager.getCampaignPersonnel();
        
        Map<Integer, List<Integer>> crewMemberSerialNumbers = new HashMap<>();
        for (CompanyPersonnel personnel : allPersonnel.values())
        {
            for (CrewMember crewMember : personnel.getCrewMembers().getCrewMemberList())
            {
                if (!crewMember.isPlayer() && !(crewMember instanceof TankAce))
                {
                    if (!crewMemberSerialNumbers.containsKey(crewMember.getSerialNumber()))
                    {
                        crewMemberSerialNumbers.put(crewMember.getSerialNumber(), new ArrayList<Integer>());
                    }
                    List<Integer> squadronsForSerialNumber = crewMemberSerialNumbers.get(crewMember.getSerialNumber());
                    squadronsForSerialNumber.add(personnel.getSquadron().getCompanyId());
                }
            }
        }
        
        boolean changesMade = false;
        for (Integer serialNumber : crewMemberSerialNumbers.keySet())
        {
            List<Integer> squadronsForSerialNumber = crewMemberSerialNumbers.get(serialNumber);
            if (squadronsForSerialNumber.size() > 1)
            {
                boolean firstTime = true;
                for (Integer squadronId : squadronsForSerialNumber)
                {
                    CompanyPersonnel campaignPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(squadronId);
                    CrewMember  crewMember = campaignPersonnel.getCrewMember(serialNumber);
                    PWCGLogger.log(LogLevel.DEBUG, "Replace " + crewMember.getName() + " squadron member : " + serialNumber + " flying for " + squadronId);
                    
                    if (!firstTime)
                    {
                        crewMember.setSerialNumber(campaign.getCampaignData().getSerialNumber().getNextCrewMemberSerialNumber());
                        
                        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(crewMember.getCompanyId());
                        String squaddieName = CrewMemberNames.getInstance().getName(squadron.determineServiceForSquadron(campaign.getDate()), new HashMap<>());
                        crewMember.setName(squaddieName);
                    }
                    
                    firstTime = false;
                    changesMade = true;
                }
            }
        }
        
        if (changesMade)
        {
            campaign.write();
        }
    }
    
    private void checkDuplicateCrewMembers() throws PWCGException
    {
        CampaignPersonnelManager personnelManager = campaign.getPersonnelManager();
        Map<Integer, CompanyPersonnel> allPersonnel = personnelManager.getCampaignPersonnel();
        
        Map<Integer, List<Integer>> crewMemberSerialNumbers = new HashMap<>();
        for (CompanyPersonnel personnel : allPersonnel.values())
        {
            for (CrewMember crewMember : personnel.getCrewMembers().getCrewMemberList())
            {
                if (!crewMember.isPlayer() && !(crewMember instanceof TankAce))
                {
                    if (!crewMemberSerialNumbers.containsKey(crewMember.getSerialNumber()))
                    {
                        crewMemberSerialNumbers.put(crewMember.getSerialNumber(), new ArrayList<Integer>());
                    }
                    List<Integer> squadronsForSerialNumber = crewMemberSerialNumbers.get(crewMember.getSerialNumber());
                    squadronsForSerialNumber.add(personnel.getSquadron().getCompanyId());
                }
            }
        }
        
        for (Integer serialNumber : crewMemberSerialNumbers.keySet())
        {
            List<Integer> squadronsForSerialNumber = crewMemberSerialNumbers.get(serialNumber);
            if (squadronsForSerialNumber.size() > 1)
            {
                for (Integer squadronId : squadronsForSerialNumber)
                {
                    PWCGLogger.log(LogLevel.DEBUG, "Duplicate squadron member : " + serialNumber + " flying for " + squadronId);
                }
            }
        }
    }
    
    
    private void checkCrewMemberKeys() throws PWCGException
    {
        boolean changesMade = false;

        CampaignPersonnelManager personnelManager = campaign.getPersonnelManager();
        Map<Integer, CompanyPersonnel> allPersonnel = personnelManager.getCampaignPersonnel();
        
        for (CompanyPersonnel personnel : allPersonnel.values())
        {
            boolean fixesNeeded = true;
            while (fixesNeeded)
            {
                fixesNeeded = fixOneCrewMember(personnel);
                if (fixesNeeded)
                {
                    changesMade = true;
                }
            }
        }
        
        if (changesMade)
        {
            campaign.write();
        }
    }

    private boolean fixOneCrewMember(CompanyPersonnel personnel) throws PWCGException
    {
        boolean fixesNeeded = false;
        for (Integer key : personnel.getCrewMembers().getCrewMemberCollection().keySet())
        {
            CrewMember crewMember = personnel.getCrewMembers().getCrewMemberCollection().get(key);
            if (key != crewMember.getSerialNumber())
            {
                personnel.getCrewMembers().removeCrewMember(key);
                personnel.getCrewMembers().addToCrewMemberCollection(crewMember);
                fixesNeeded = true;
                break;
            }
        }
        return fixesNeeded;
    }

    
    private void cleanMedals() throws PWCGException
    {
        CampaignPersonnelManager personnelManager = campaign.getPersonnelManager();
        Map<Integer, CompanyPersonnel> allPersonnel = personnelManager.getCampaignPersonnel();
        
        for (CompanyPersonnel personnel : allPersonnel.values())
        {
            for (CrewMember crewMember : personnel.getCrewMembers().getCrewMemberList())
            {
                for (Medal medal : crewMember.getMedals())
                {
                    if (medal.getMedalName().contains("Wound Badge ("))
                    {
                        int afterIndex = medal.getMedalName().indexOf("(");
                        String medalName = "Wound Badge" + medal.getMedalName().substring(afterIndex);
                        medal.setMedalName(medalName);
                    }
                    convertMedal(crewMember, crewMember.determineCountry(medal.getMedalDate()), medal);
                }
            }
        }
        
        for (TankAce ace : campaign.getPersonnelManager().getCampaignAces().getAllCampaignAces().values())
        {
            for (Medal medal : ace.getMedals())
            {
                convertMedal(ace, CountryFactory.makeCountryByCountry(ace.getCountry()), medal);
            }
        }
    }

    private void convertMedal(CrewMember crewMember, ICountry country, Medal medal) throws PWCGException
    {
        Medal newMedal = MedalManager.getMedalFromAnyManager(country, campaign, medal.getMedalName());
        if (newMedal != null)
        {
            medal.setMedalImage(newMedal.getMedalImage());
        }
        else
        {
            PWCGLogger.log(LogLevel.DEBUG, "Could not find medal for " + medal.getMedalName());
        }
    }
}
