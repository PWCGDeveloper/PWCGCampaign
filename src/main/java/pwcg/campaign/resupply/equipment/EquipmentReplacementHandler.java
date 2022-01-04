package pwcg.campaign.resupply.equipment;

import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.resupply.ISquadronNeed;
import pwcg.campaign.resupply.ResupplyNeedBuilder;
import pwcg.campaign.resupply.ServiceResupplyNeed;
import pwcg.campaign.resupply.SquadronNeedFactory.SquadronNeedType;
import pwcg.campaign.resupply.depot.EquipmentDepot;
import pwcg.campaign.tank.EquippedTank;
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
    
    public EquipmentResupplyData resupplyForLosses(ArmedService armedService) throws PWCGException
    {
        ServiceResupplyNeed serviceResupplyNeed = equipmentNeedBuilder.determineNeedForService(SquadronNeedType.EQUIPMENT);
        EquipmentDepot equipmentDepo =  campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());
        replaceForService(serviceResupplyNeed, equipmentDepo);
        return equipmentResupplyData;
    }


    private void replaceForService(ServiceResupplyNeed serviceResupplyNeed, EquipmentDepot equipmentDepo) throws PWCGException
    {
        while (serviceResupplyNeed.hasNeedySquadron())
        {
            ISquadronNeed selectedSquadronNeed = serviceResupplyNeed.chooseNeedySquadron();
            if (selectedSquadronNeed == null)
            {
                break;
            }

            Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(selectedSquadronNeed.getSquadronId());
            List<String> activeArchTypes = squadron.getActiveArchTypes(campaign.getDate());
            
            EquippedTank replacement = equipmentDepo.removeBestPlaneFromDepot(activeArchTypes);        
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
