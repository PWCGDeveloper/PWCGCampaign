package pwcg.aar.outofmission.phase2.resupply;

import java.util.List;

import pwcg.aar.outofmission.phase2.resupply.SquadronNeedFactory.SquadronNeedType;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.plane.EquipmentReplacement;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class EquipmentReplacementHandler
{
    private Campaign campaign;
    private ResupplyNeedBuilder transferNeedBuilder;

    private EquipmentResupplyData equipmentResupplyData = new EquipmentResupplyData();
    
    public EquipmentReplacementHandler(Campaign campaign, ResupplyNeedBuilder transferNeedBuilder)
    {
        this.campaign = campaign;
        this.transferNeedBuilder = transferNeedBuilder;
    }
    
    public EquipmentResupplyData determineEquipmentResupply() throws PWCGException
    {
        transferNeedBuilder.initialize(SquadronNeedType.EQUIPMENT);
        
        IArmedServiceManager serviceManager = ArmedServiceFactory.createServiceManager();
        for (ArmedService armedService : serviceManager.getAllArmedServices())
        {
            ServiceResupplyNeed serviceNeed = transferNeedBuilder.getServiceTransferNeed(armedService.getServiceId());
            EquipmentReplacement serviceReplacements =  campaign.getEquipmentManager().getEquipmentReplacementsForService(armedService.getServiceId());
            replaceForService(serviceNeed, serviceReplacements);
        }
        
        return equipmentResupplyData;
    }


    private void replaceForService(ServiceResupplyNeed serviceResupplyNeed, EquipmentReplacement serviceReplacements) throws PWCGException
    {
        while (serviceResupplyNeed.hasNeedySquadron())
        {
            int needySquadronId = serviceResupplyNeed.chooseNeedySquadron();
            Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(needySquadronId);
            List<String> activeArchTypes = squadron.getActiveArchTypes(campaign.getDate());
            
            EquippedPlane replacement = serviceReplacements.getEquipment().removeBestEquippedPlaneForArchType(activeArchTypes);        
            if (replacement != null)
            {
                EquipmentResupplyRecord equipmentResupplyRecord = new EquipmentResupplyRecord(replacement, needySquadronId);
                equipmentResupplyData.addEquipmentResupplyRecord(equipmentResupplyRecord);
            }
            else
            {
                serviceResupplyNeed.removeNeedySquadron(needySquadronId);
            }
        }        
    }
}
