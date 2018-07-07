package pwcg.campaign;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquipmentReplacement;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.core.exception.PWCGException;

public class CampaignEquipmentManager
{
    private Map<Integer, Equipment> equipmentAllSquadrons = new HashMap<>();
    private Map<Integer, EquipmentReplacement> equipmentReplacements = new HashMap<>();

    public Equipment getEquipmentForSquadron(Integer squadronId)
    {
        return equipmentAllSquadrons.get(squadronId);
    }

    public EquipmentReplacement getEquipmentReplacementsForService(Integer serviceId)
    {
        return equipmentReplacements.get(serviceId);
    }

    public void addEquipmentForSquadron(Integer squadronId, Equipment equipmentForSquadron)
    {
        equipmentAllSquadrons.put(squadronId, equipmentForSquadron);
    }

    public void addEquipmentReplacementsForService(Integer serviceId, EquipmentReplacement replacementEquipmentForService)
    {
        equipmentReplacements.put(serviceId, replacementEquipmentForService);
    }

    public Map<Integer, Equipment> getEquipmentAllSquadrons()
    {
        return equipmentAllSquadrons;
    }

    public Map<Integer, EquipmentReplacement> getEquipmentReplacements()
    {
        return equipmentReplacements;
    }

    public EquippedPlane getPlaneFromAnySquadron(Integer serialNumber) throws PWCGException
    {
        for (Equipment equipment : equipmentAllSquadrons.values())
        {
            EquippedPlane equippedPlane = equipment.getEquippedPlanes().get(serialNumber);
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
        for (EquipmentReplacement replacementService : equipmentReplacements.values())
        {
            replacementCount += replacementService.getEquipment().getEquippedPlanes().size();
        }
        
        return replacementCount;
    }
}