package pwcg.campaign;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.plane.Equipment;

public class CampaignEquipmentManager
{
    private Map<Integer, Equipment> equipmentAllSquadrons = new HashMap<>();
    private Map<Integer, Equipment> equipmentReplacements = new HashMap<>();

    public Equipment getEquipmentForSquadron(Integer squadronId)
    {
        return equipmentAllSquadrons.get(squadronId);
    }

    public Equipment getEquipmentReplacementsForService(Integer serviceId)
    {
        return equipmentReplacements.get(serviceId);
    }

    public void addEquipmentForSquadron(Integer squadronId, Equipment equipmentForSquadron)
    {
        equipmentAllSquadrons.put(squadronId, equipmentForSquadron);
    }

    public void addEquipmentReplacementsForService(Integer serviceId, Equipment replacementEquipmentForService)
    {
        equipmentReplacements.put(serviceId, replacementEquipmentForService);
    }

    public Map<Integer, Equipment> getEquipmentAllSquadrons()
    {
        return equipmentAllSquadrons;
    }

    public Map<Integer, Equipment> getEquipmentReplacements()
    {
        return equipmentReplacements;
    }
}
