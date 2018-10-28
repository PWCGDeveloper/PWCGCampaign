package pwcg.campaign.resupply.equipment;

import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.resupply.ISquadronNeed;
import pwcg.campaign.resupply.ResupplyNeedBuilder;
import pwcg.campaign.resupply.ServiceResupplyNeed;
import pwcg.campaign.resupply.SquadronNeedFactory.SquadronNeedType;
import pwcg.campaign.resupply.depo.EquipmentDepo;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class EquipmentReplacementHandler
{
    private Campaign campaign;
    private ResupplyNeedBuilder equipmentNeedBuilder;

    private EquipmentResupplyData equipmentResupplyData = new EquipmentResupplyData();
    
    public EquipmentReplacementHandler(Campaign campaign, ResupplyNeedBuilder equipmentNeedBuilder)
    {
        this.campaign = campaign;
        this.equipmentNeedBuilder = equipmentNeedBuilder;
    }
    
    public EquipmentResupplyData determineEquipmentResupply(ArmedService armedService) throws PWCGException
    {
        ServiceResupplyNeed serviceResupplyNeed = equipmentNeedBuilder.determineNeedForService(SquadronNeedType.EQUIPMENT);
        EquipmentDepo serviceAvailableReplacements =  campaign.getEquipmentManager().getEquipmentReplacementsForService(armedService.getServiceId());
        replaceForService(serviceResupplyNeed, serviceAvailableReplacements);
        
        return equipmentResupplyData;
    }


    private void replaceForService(ServiceResupplyNeed serviceResupplyNeed, EquipmentDepo serviceAvailableReplacements) throws PWCGException
    {
        while (serviceResupplyNeed.hasNeedySquadron())
        {
            ISquadronNeed selectedSquadronNeed = serviceResupplyNeed.chooseNeedySquadron();
            if (selectedSquadronNeed == null)
            {
                break;
            }

            Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(selectedSquadronNeed.getSquadronId());
            List<String> activeArchTypes = squadron.getActiveArchTypes(campaign.getDate());
            
            EquippedPlane replacement = serviceAvailableReplacements.getEquipment().removeBestEquippedPlaneForArchType(activeArchTypes);        
            if (replacement != null)
            {
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
