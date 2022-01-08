package pwcg.aar.outofmission.phase3.resupply;

import pwcg.aar.data.AARContext;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.resupply.ResupplyNeedBuilder;
import pwcg.campaign.resupply.equipment.EquipmentReplacementHandler;
import pwcg.campaign.resupply.equipment.EquipmentResupplyData;
import pwcg.campaign.resupply.equipment.EquipmentUpgradeHandler;
import pwcg.campaign.resupply.equipment.WithdrawnEquipmentReplacer;
import pwcg.campaign.resupply.personnel.SquadronTransferData;
import pwcg.campaign.resupply.personnel.TransferHandler;
import pwcg.campaign.tank.Equipment;
import pwcg.core.exception.PWCGException;

public class AARResupplyCoordinator
{
    private Campaign campaign;
    private AARContext aarContext;
    private AARResupplyData resupplyData = new AARResupplyData();

    public AARResupplyCoordinator(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
    
    
    public AARResupplyData handleResupply() throws PWCGException 
    {
        handleAceTransfers();
        squadronTransfers();
        equipmentResupply();
        return resupplyData;
    }

    private void handleAceTransfers() throws PWCGException
    {
        HistoricalAceTransferHandler aceTransferHandler = new HistoricalAceTransferHandler(campaign, aarContext.getNewDate());
        SquadronTransferData acesTransferred =  aceTransferHandler.determineAceTransfers();
        resupplyData.setAcesTransferred(acesTransferred);
    }
    
    private void squadronTransfers() throws PWCGException
    {
        IArmedServiceManager serviceManager = ArmedServiceFactory.createServiceManager();
        for (ArmedService armedService : serviceManager.getAllActiveArmedServices(campaign.getDate()))
        {
            ResupplyNeedBuilder transferNeedBuilder = new ResupplyNeedBuilder(campaign, armedService);
            TransferHandler squadronTransferHandler = new TransferHandler(campaign, transferNeedBuilder);
            SquadronTransferData squadronTransferData = squadronTransferHandler.determineCrewMemberTransfers(armedService);
            resupplyData.getSquadronTransferData().merge(squadronTransferData);
        }
    }

    private void equipmentResupply() throws PWCGException
    {
        IArmedServiceManager serviceManager = ArmedServiceFactory.createServiceManager();
        for (ArmedService armedService : serviceManager.getAllActiveArmedServices(campaign.getDate()))
        {
            replaceWithdrawnPlanes(armedService);
            replaceLostPlanes(armedService);
            upgradePlanes(armedService);
        }
    }

    private void replaceWithdrawnPlanes(ArmedService armedService) throws PWCGException
    {
        for (CompanyPersonnel squadronPersonnel : campaign.getPersonnelManager().getAllCompanyPersonnel())
        {
            int serviceIdForSquadron = squadronPersonnel.getSquadron().determineServiceForSquadron(campaign.getDate()).getServiceId();
            if (armedService.getServiceId() == serviceIdForSquadron)
            {
                Equipment equipment = campaign.getEquipmentManager().getEquipmentForCompany(squadronPersonnel.getSquadron().getCompanyId());
                WithdrawnEquipmentReplacer withdrawnEquipmentReplacer = new WithdrawnEquipmentReplacer(campaign, equipment, squadronPersonnel.getSquadron());
                withdrawnEquipmentReplacer.replaceWithdrawnEquipment();
            }
        }        
    }

    private void replaceLostPlanes(ArmedService armedService) throws PWCGException
    {
        ResupplyNeedBuilder equipmentNeedBuilder = new ResupplyNeedBuilder(campaign, armedService);
        EquipmentReplacementHandler equipmentResupplyHandler = new EquipmentReplacementHandler(campaign, equipmentNeedBuilder);
        EquipmentResupplyData equipmentResupplyData = equipmentResupplyHandler.resupplyForLosses(armedService);
        resupplyData.getEquipmentResupplyData().merge(equipmentResupplyData);
    }
    
    private void upgradePlanes(ArmedService armedService) throws PWCGException
    {
        EquipmentUpgradeHandler equipmentUpgradeHandler = new EquipmentUpgradeHandler(campaign);
        EquipmentResupplyData equipmentResupplyData = equipmentUpgradeHandler.upgradeEquipment(armedService);
        resupplyData.getEquipmentResupplyData().merge(equipmentResupplyData);
    }
}
