package pwcg.campaign;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.campaign.resupply.depot.EquipmentDepot;
import pwcg.campaign.resupply.depot.EquipmentDepotInitializer;
import pwcg.core.exception.PWCGException;

public class CampaignEquipmentManager
{
    private Campaign campaign;
    private Map<Integer, Equipment> equipmentAllSquadrons = new HashMap<>();
    private Map<Integer, EquipmentDepot> equipmentDepotsForServices = new HashMap<>();

    public CampaignEquipmentManager(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public Equipment getEquipmentForSquadron(Integer squadronId)
    {
        return equipmentAllSquadrons.get(squadronId);
    }
    
    public boolean hasEquipmentDepo (int serviceId)
    {
        return equipmentDepotsForServices.containsKey(serviceId);
    }

    public EquipmentDepot getEquipmentDepotForService(Integer serviceId) throws PWCGException
    {
        if (!equipmentDepotsForServices.containsKey(serviceId))
        {
            ArmedService armedService = ArmedServiceFactory.createServiceManager().getArmedServiceById(serviceId, campaign.getDate());
            createEquipmentDepot(armedService);
        }
        return equipmentDepotsForServices.get(serviceId);
    }

    public void addEquipmentForSquadron(Integer squadronId, Equipment equipmentForSquadron)
    {
        equipmentAllSquadrons.put(squadronId, equipmentForSquadron);
    }

    public void addEquipmentDepotForService(Integer serviceId, EquipmentDepot replacementEquipmentForService)
    {
        equipmentDepotsForServices.put(serviceId, replacementEquipmentForService);
    }

    public Map<Integer, Equipment> getEquipmentAllSquadrons()
    {
        return equipmentAllSquadrons;
    }
    
    public List<Integer> getServiceIdsForDepots()
    {
        return new ArrayList<Integer>(equipmentDepotsForServices.keySet());
    }

    public EquippedPlane getPlaneFromAnySquadron(Integer serialNumber) throws PWCGException
    {
        for (Equipment equipment : equipmentAllSquadrons.values())
        {
            EquippedPlane equippedPlane = equipment.getEquippedPlane(serialNumber);
            if (equippedPlane != null)
            {
                return equippedPlane;
            }        
        }
         throw new PWCGException ("Unable to locate equipped plane for serial number " + serialNumber);
    }

    public EquippedPlane getAnyActivePlaneFromSquadron(Integer squadronId) throws PWCGException
    {
        Equipment equipment = equipmentAllSquadrons.get(squadronId);
        for (EquippedPlane equippedPlane : equipment.getActiveEquippedPlanes().values())
        {
            return equippedPlane;
        }

        throw new PWCGException ("Unable to locate active equipped plane for squadron " + squadronId);
    }

    public EquippedPlane destroyPlaneFromSquadron(int squadronId, Date date) throws PWCGException
    {
        EquippedPlane destroyedPlane = getAnyActivePlaneFromSquadron(squadronId);
        destroyedPlane.setPlaneStatus(PlaneStatus.STATUS_DESTROYED);
        destroyedPlane.setDateRemovedFromService(date);
        return destroyedPlane;
    }

    public EquippedPlane destroyPlane(int serialNumber, Date date) throws PWCGException
    {
        EquippedPlane destroyedPlane = getPlaneFromAnySquadron(serialNumber);
        destroyedPlane.setPlaneStatus(PlaneStatus.STATUS_DESTROYED);
        destroyedPlane.setDateRemovedFromService(date);
        return destroyedPlane;
    }
    

    public int getReplacementCount() throws PWCGException
    {
        int replacementCount = 0;
        for (EquipmentDepot replacementService : equipmentDepotsForServices.values())
        {
            replacementCount += replacementService.getDepotSize();
        }
        
        return replacementCount;
    }
    
    
    public void createEquipmentDepot(ArmedService armedService) throws PWCGException
    {
        EquipmentDepotInitializer depotInitializer = new EquipmentDepotInitializer(campaign, armedService);
        Equipment equipment = depotInitializer.createReplacementPoolForService();
        EquipmentDepot depot = new EquipmentDepot();
        depot.setEquipmentPoints(armedService.getDailyEquipmentReplacementRate() * 2);
        depot.setLastReplacementDate(campaign.getDate());
        depot.setEquippment(equipment);
        campaign.getEquipmentManager().addEquipmentDepotForService(armedService.getServiceId(), depot);
    }

}
