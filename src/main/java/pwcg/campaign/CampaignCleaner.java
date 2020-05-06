package pwcg.campaign;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.Ace;
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
    
    private void removeDuplicatePilots()
    {
        System.out.println("Duplicate squadron members in campaign : " + campaign.getCampaignData().getName());

        CampaignPersonnelManager personnelManager = campaign.getPersonnelManager();
        Map<Integer, SquadronPersonnel> allPersonnel = personnelManager.getCampaignPersonnel();
        
        List<SquadronMember> allAiSquadronMembers = new ArrayList<>();
        for (SquadronPersonnel personnel : allPersonnel.values())
        {
            for (SquadronMember squadronMember : personnel.getSquadronMembers().getSquadronMemberList())
            {
                if (!squadronMember.isPlayer() && !(squadronMember instanceof Ace))
                {
                    allAiSquadronMembers.add(squadronMember);
                }
            }
        }
        
        HashSet<Integer> pilotSerialNumbers = new HashSet<>();
        for (SquadronMember aiSquadronMember : allAiSquadronMembers)
        {
            if (pilotSerialNumbers.contains(aiSquadronMember.getSerialNumber()))
            {
                System.out.println("Duplicate squadron member : " + aiSquadronMember.getName() + " flying for " + aiSquadronMember.getSquadronId());
                // aiSquadronMember.setSerialNumber(campaign.getSerialNumber().getNextPilotSerialNumber());
            }
            else
            {
                pilotSerialNumbers.add(aiSquadronMember.getSerialNumber());
            }
        }
        
    }
}
