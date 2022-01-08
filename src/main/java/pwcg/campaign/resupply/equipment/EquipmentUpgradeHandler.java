package pwcg.campaign.resupply.equipment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.resupply.depot.EquipmentDepot;
import pwcg.campaign.resupply.depot.EquipmentUpgradeRecord;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankSorter;
import pwcg.core.exception.PWCGException;

public class EquipmentUpgradeHandler
{
    private Campaign campaign;
    private EquipmentResupplyData equipmentResupplyData = new EquipmentResupplyData();
    
    public EquipmentUpgradeHandler(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public EquipmentResupplyData upgradeEquipment(ArmedService armedService) throws PWCGException
    {
        upgradePlayerSquadrons(armedService);
        upgradeAiSquadrons(armedService);
        return equipmentResupplyData;
    }

    private void upgradePlayerSquadrons(ArmedService armedService) throws PWCGException
    {
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        for (Company squadron : squadronManager.getActiveCompaniesForService(campaign.getDate(), armedService))
        {
            if (Company.isPlayerCompany(campaign, squadron.getCompanyId()))
            {
                upgradeEquipment(squadron);
            }
        }
    }

    private void upgradeAiSquadrons(ArmedService armedService) throws PWCGException
    {
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        for (Company squadron : squadronManager.getActiveCompaniesForService(campaign.getDate(), armedService))
        {
            upgradeEquipment(squadron);
        }
    }

    private void upgradeEquipment(Company squadron) throws PWCGException
    {
        Equipment equipmentForSquadron = campaign.getEquipmentManager().getEquipmentForCompany(squadron.getCompanyId());
        EquipmentDepot equipmentDepot = campaign.getEquipmentManager().getEquipmentDepotForService(squadron.getService());

        List<EquippedTank> sortedPlanes = getPlanesForSquadronWorstToBest(equipmentForSquadron);
        for (EquippedTank equippedPlane : sortedPlanes)
        {
            EquipmentUpgradeRecord equipmentUpgrade = equipmentDepot.getUpgrade(equippedPlane);
            if (equipmentUpgrade != null)
            {
                EquippedTank replacementPlane = equipmentDepot.removeEquippedPlaneFromDepot(equipmentUpgrade.getUpgrade().getSerialNumber());
                equipmentForSquadron.addEquippedTankToCompany(campaign, squadron.getCompanyId(), replacementPlane);
                
                EquippedTank replacedPlane = equipmentForSquadron.removeEquippedTank(equipmentUpgrade.getReplacedPlane().getSerialNumber());
                equipmentDepot.addPlaneToDepot(replacedPlane);

                EquipmentResupplyRecord equipmentResupplyRecord = new EquipmentResupplyRecord(replacementPlane, squadron.getCompanyId());
                equipmentResupplyData.addEquipmentResupplyRecord(equipmentResupplyRecord);
            }
        }        
    }

    private List<EquippedTank> getPlanesForSquadronWorstToBest(Equipment equipmentForSquadron) throws PWCGException
    {
        Map<Integer, EquippedTank> planesForSquadron = equipmentForSquadron.getActiveEquippedTanks();
        List<EquippedTank> sortedPlanes = TankSorter.sortEquippedTanksByGoodness(new ArrayList<EquippedTank>(planesForSquadron.values()));
        Collections.reverse(sortedPlanes);
        return sortedPlanes;
    }
}
