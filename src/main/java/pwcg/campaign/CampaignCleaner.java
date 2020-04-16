package pwcg.campaign;

import java.io.File;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.SquadronManager;
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
}
