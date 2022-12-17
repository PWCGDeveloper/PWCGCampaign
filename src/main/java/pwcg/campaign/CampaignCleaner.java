package pwcg.campaign;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.medals.MedalManager;
import pwcg.campaign.personnel.SquadronMemberNationalityConverter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.PilotNames;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronManager;
import pwcg.campaign.squadron.SquadronViability;
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
        removeDuplicatePilots();
        checkDuplicatePilots();
        checkPilotKeys();
        cleanMedals();
    }

    private void removeUnwantedSquadronFiles() throws PWCGException
    {
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> squadronsToStaff = squadronManager.getActiveSquadrons(campaign.getDate());
        for (Squadron squadron : squadronsToStaff)
        {
            if (!SquadronViability.isSquadronActive(squadron, campaign.getDate()))
            {
                if (campaign.getPersonnelManager().getSquadronPersonnel(squadron.getSquadronId()) == null)
                {
                    String campaignPersonnelDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Personnel\\";
                    File squadronPersonnelFile = new File(campaignPersonnelDir + squadron.getSquadronId() + ".json");
                    if (squadronPersonnelFile.exists())
                    {
                        squadronPersonnelFile.delete();
                    }
                    
                    String campaignEquipmentDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Equipment\\";
                    File campaignEquipmentFile = new File(campaignEquipmentDir + squadron.getSquadronId() + ".json");
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
    
    private void removeDuplicatePilots() throws PWCGException
    {
        CampaignPersonnelManager personnelManager = campaign.getPersonnelManager();
        Map<Integer, SquadronPersonnel> allPersonnel = personnelManager.getCampaignPersonnel();
        
        Map<Integer, List<Integer>> pilotSerialNumbers = new HashMap<>();
        for (SquadronPersonnel personnel : allPersonnel.values())
        {
            for (SquadronMember squadronMember : personnel.getSquadronMembers().getSquadronMemberList())
            {
                if (!squadronMember.isPlayer() && !(squadronMember instanceof Ace))
                {
                    if (!pilotSerialNumbers.containsKey(squadronMember.getSerialNumber()))
                    {
                        pilotSerialNumbers.put(squadronMember.getSerialNumber(), new ArrayList<Integer>());
                    }
                    List<Integer> squadronsForSerialNumber = pilotSerialNumbers.get(squadronMember.getSerialNumber());
                    squadronsForSerialNumber.add(personnel.getSquadron().getSquadronId());
                }
            }
        }
        
        boolean changesMade = false;
        for (Integer serialNumber : pilotSerialNumbers.keySet())
        {
            List<Integer> squadronsForSerialNumber = pilotSerialNumbers.get(serialNumber);
            if (squadronsForSerialNumber.size() > 1)
            {
                boolean firstTime = true;
                for (Integer squadronId : squadronsForSerialNumber)
                {
                    SquadronPersonnel campaignPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(squadronId);
                    SquadronMember  squadronMember = campaignPersonnel.getSquadronMember(serialNumber);
                    PWCGLogger.log(LogLevel.DEBUG, "Replace " + squadronMember.getName() + " squadron member : " + serialNumber + " flying for " + squadronId);
                    
                    if (!firstTime)
                    {
                        squadronMember.setSerialNumber(campaign.getCampaignData().getSerialNumber().getNextPilotSerialNumber());
                        
                        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronMember.getSquadronId());
                        String squaddieName = PilotNames.getInstance().getName(squadron.determineServiceForSquadron(campaign.getDate()), new HashMap<>());
                        squadronMember.setName(squaddieName);
                        SquadronMemberNationalityConverter.convertIfNeeded(campaign, squadron, squadronMember);
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
    
    private void checkDuplicatePilots() throws PWCGException
    {
        CampaignPersonnelManager personnelManager = campaign.getPersonnelManager();
        Map<Integer, SquadronPersonnel> allPersonnel = personnelManager.getCampaignPersonnel();
        
        Map<Integer, List<Integer>> pilotSerialNumbers = new HashMap<>();
        for (SquadronPersonnel personnel : allPersonnel.values())
        {
            for (SquadronMember squadronMember : personnel.getSquadronMembers().getSquadronMemberList())
            {
                if (!squadronMember.isPlayer() && !(squadronMember instanceof Ace))
                {
                    if (!pilotSerialNumbers.containsKey(squadronMember.getSerialNumber()))
                    {
                        pilotSerialNumbers.put(squadronMember.getSerialNumber(), new ArrayList<Integer>());
                    }
                    List<Integer> squadronsForSerialNumber = pilotSerialNumbers.get(squadronMember.getSerialNumber());
                    squadronsForSerialNumber.add(personnel.getSquadron().getSquadronId());
                }
            }
        }
        
        for (Integer serialNumber : pilotSerialNumbers.keySet())
        {
            List<Integer> squadronsForSerialNumber = pilotSerialNumbers.get(serialNumber);
            if (squadronsForSerialNumber.size() > 1)
            {
                for (Integer squadronId : squadronsForSerialNumber)
                {
                    PWCGLogger.log(LogLevel.DEBUG, "Duplicate squadron member : " + serialNumber + " flying for " + squadronId);
                }
            }
        }
    }
    
    
    private void checkPilotKeys() throws PWCGException
    {
        boolean changesMade = false;

        CampaignPersonnelManager personnelManager = campaign.getPersonnelManager();
        Map<Integer, SquadronPersonnel> allPersonnel = personnelManager.getCampaignPersonnel();
        
        for (SquadronPersonnel personnel : allPersonnel.values())
        {
            boolean fixesNeeded = true;
            while (fixesNeeded)
            {
                fixesNeeded = fixOneSquadronMember(personnel);
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

    private boolean fixOneSquadronMember(SquadronPersonnel personnel) throws PWCGException
    {
        boolean fixesNeeded = false;
        for (Integer key : personnel.getSquadronMembers().getSquadronMemberCollection().keySet())
        {
            SquadronMember squadronMember = personnel.getSquadronMembers().getSquadronMemberCollection().get(key);
            if (key != squadronMember.getSerialNumber())
            {
                personnel.getSquadronMembers().removeSquadronMember(key);
                personnel.getSquadronMembers().addToSquadronMemberCollection(squadronMember);
                fixesNeeded = true;
                break;
            }
        }
        return fixesNeeded;
    }

    
    private void cleanMedals() throws PWCGException
    {
        CampaignPersonnelManager personnelManager = campaign.getPersonnelManager();
        Map<Integer, SquadronPersonnel> allPersonnel = personnelManager.getCampaignPersonnel();
        
        for (SquadronPersonnel personnel : allPersonnel.values())
        {
            for (SquadronMember squadronMember : personnel.getSquadronMembers().getSquadronMemberList())
            {
                for (Medal medal : squadronMember.getMedals())
                {
                    if (medal.getMedalName().contains("Wound Badge ("))
                    {
                        int afterIndex = medal.getMedalName().indexOf("(");
                        String medalName = "Wound Badge" + medal.getMedalName().substring(afterIndex);
                        medal.setMedalName(medalName);
                    }
                    convertMedal(squadronMember, squadronMember.determineCountry(medal.getMedalDate()), medal);
                }
            }
        }
        
        for (Ace ace : campaign.getPersonnelManager().getCampaignAces().getAllCampaignAces().values())
        {
            for (Medal medal : ace.getMedals())
            {
                convertMedal(ace, CountryFactory.makeCountryByCountry(ace.getCountry()), medal);
            }
        }
    }

    private void convertMedal(SquadronMember squadronMember, ICountry country, Medal medal) throws PWCGException
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
