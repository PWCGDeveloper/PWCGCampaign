package pwcg.campaign.resupply.equipment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.IPlaneMarkingManager;
import pwcg.campaign.plane.PlaneSorter;
import pwcg.campaign.resupply.depot.EquipmentDepot;
import pwcg.campaign.resupply.depot.EquipmentUpgradeRecord;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronManager;
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
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        for (Squadron squadron : squadronManager.getActiveSquadronsForService(campaign.getDate(), armedService))
        {
            if (Squadron.isPlayerSquadron(campaign, squadron.getSquadronId()))
            {
                upgradeEquipment(squadron);
            }
        }
    }

    private void upgradeAiSquadrons(ArmedService armedService) throws PWCGException
    {
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        for (Squadron squadron : squadronManager.getActiveSquadronsForService(campaign.getDate(), armedService))
        {
            if (Squadron.isPlayerSquadron(campaign, squadron.getSquadronId()))
            {
                upgradeEquipment(squadron);
            }
        }
    }

    private void upgradeEquipment(Squadron squadron) throws PWCGException
    {
        Equipment equipmentForSquadron = campaign.getEquipmentManager().getEquipmentForSquadron(squadron.getSquadronId());
        EquipmentDepot equipmentDepot = campaign.getEquipmentManager().getEquipmentDepotForService(squadron.getService());
        IPlaneMarkingManager planeMarkingManager = PWCGContext.getInstance().getPlaneMarkingManager();

        List<EquippedPlane> sortedPlanes = getPlanesForSquadronWorstToBest(equipmentForSquadron);
        for (EquippedPlane equippedPlane : sortedPlanes)
        {
            EquipmentUpgradeRecord equipmentUpgrade = equipmentDepot.getUpgrade(equippedPlane);
            if (equipmentUpgrade != null)
            {
                EquippedPlane replacementPlane = equipmentDepot.removeEquippedPlaneFromDepot(equipmentUpgrade.getUpgrade().getSerialNumber());
                planeMarkingManager.allocatePlaneIdCode(campaign, squadron.getSquadronId(), equipmentForSquadron, equippedPlane);
                equipmentForSquadron.addEquippedPlane(replacementPlane);
                
                EquippedPlane replacedPlane = equipmentForSquadron.removeEquippedPlane(equipmentUpgrade.getReplacedPlane().getSerialNumber());
                equipmentDepot.addPlaneToDepot(replacedPlane);

                EquipmentResupplyRecord equipmentResupplyRecord = new EquipmentResupplyRecord(replacementPlane, squadron.getSquadronId());
                equipmentResupplyData.addEquipmentResupplyRecord(equipmentResupplyRecord);
            }
        }        
    }

    private List<EquippedPlane> getPlanesForSquadronWorstToBest(Equipment equipmentForSquadron) throws PWCGException
    {
        Map<Integer, EquippedPlane> planesForSquadron = equipmentForSquadron.getActiveEquippedPlanes();
        List<EquippedPlane> sortedPlanes = PlaneSorter.sortEquippedPlanesByGoodness(new ArrayList<EquippedPlane>(planesForSquadron.values()));
        Collections.reverse(sortedPlanes);
        return sortedPlanes;
    }
}
