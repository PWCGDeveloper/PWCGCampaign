package pwcg.campaign;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.PilotNames;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

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
    }
    
    public void removeUnwantedSquadronFiles() throws PWCGException
    {
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> squadronsToStaff = squadronManager.getActiveSquadrons(campaign.getDate());
        for (Squadron squadron : squadronsToStaff)
        {
            if (!squadron.isCanFly(campaign.getDate()))
            {
                if (campaign.getPersonnelManager().getSquadronPersonnel(squadron.getSquadronId()) == null)
                {
                    String campaignPersonnelDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Personnel\\";
                    File squadronPersonnelFile = new File(campaignPersonnelDir + squadron.getSquadronId() + ".json");
                    if (squadronPersonnelFile.exists())
                    {
                        squadronPersonnelFile.delete();
                    }
                    
                    String campaignEquipmentDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Equipment\\";
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
                    System.out.println("Replace " + squadronMember.getName() + " squadron member : " + serialNumber + " flying for " + squadronId);
                    
                    if (!firstTime)
                    {
                        squadronMember.setSerialNumber(campaign.getCampaignData().getSerialNumber().getNextPilotSerialNumber());
                        
                        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronMember.getSquadronId());
                        String squaddieName = PilotNames.getInstance().getName(squadron.determineServiceForSquadron(campaign.getDate()), new HashMap<>());
                        squadronMember.setName(squaddieName);
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
                    System.out.println("Duplicate squadron member : " + serialNumber + " flying for " + squadronId);
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

}
