package pwcg.aar.outofmission.phase2.resupply;

import java.util.List;

import pwcg.aar.outofmission.phase2.resupply.SquadronNeedFactory.SquadronNeedType;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.EquipmentReplacement;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class EquipmentReplacementHandler
{
    private Campaign campaign;
    private ResupplyNeedBuilder equipmentNeedBuilder;

    private EquipmentResupplyData equipmentResupplyData = new EquipmentResupplyData();
    
    public EquipmentReplacementHandler(Campaign campaign, ResupplyNeedBuilder transferNeedBuilder)
    {
        this.campaign = campaign;
        this.equipmentNeedBuilder = transferNeedBuilder;
    }
    
    public EquipmentResupplyData determineEquipmentResupply(ArmedService armedService) throws PWCGException
    {
        ServiceResupplyNeed serviceResupplyNeed = equipmentNeedBuilder.determineNeedForService(SquadronNeedType.EQUIPMENT);
        EquipmentReplacement serviceAvailableReplacements =  campaign.getEquipmentManager().getEquipmentReplacementsForService(armedService.getServiceId());
        replaceForService(serviceResupplyNeed, serviceAvailableReplacements);
        
        return equipmentResupplyData;
    }


    private void replaceForService(ServiceResupplyNeed serviceResupplyNeed, EquipmentReplacement serviceAvailableReplacements) throws PWCGException
    {
        while (serviceResupplyNeed.hasNeedySquadron())
        {
            ISquadronNeed selectedSquadronNeed = serviceResupplyNeed.chooseNeedySquadron();
            if (selectedSquadronNeed == null)
            {
                break;
            }
            
            System.out.println("Squadron chosen for need: " + selectedSquadronNeed.getSquadronId());
            
            Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(selectedSquadronNeed.getSquadronId());
            List<String> activeArchTypes = squadron.getActiveArchTypes(campaign.getDate());

            for (String archType: activeArchTypes)
            {
                System.out.println("Archtypes for squadron: " + archType);
            }
            
            EquippedPlane replacement = serviceAvailableReplacements.getEquipment().removeBestEquippedPlaneForArchType(activeArchTypes);        
            if (replacement != null)
            {
                System.out.println("Removed from depo: " + replacement.getDisplayName() + " " + replacement.getSerialNumber() + " for " + selectedSquadronNeed.getSquadronId());
                EquipmentResupplyRecord equipmentResupplyRecord = new EquipmentResupplyRecord(replacement, selectedSquadronNeed.getSquadronId());
                equipmentResupplyData.addEquipmentResupplyRecord(equipmentResupplyRecord);
                serviceResupplyNeed.noteResupply(selectedSquadronNeed);
            }
            else
            {
                serviceResupplyNeed.removeNeedySquadron(selectedSquadronNeed);
            }
        }        
    }
}
