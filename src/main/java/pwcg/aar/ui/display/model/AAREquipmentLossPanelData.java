package pwcg.aar.ui.display.model;

import java.util.HashMap;
import java.util.Map;

import pwcg.aar.ui.events.model.PlaneStatusEvent;

public class AAREquipmentLossPanelData
{
    private Map<Integer, PlaneStatusEvent> equipmentLost = new HashMap<>();

    public Map<Integer, PlaneStatusEvent> getEquipmentLost()
    {
        return equipmentLost;
    }

    public void setEquipmentLost(Map<Integer, PlaneStatusEvent> equipmentLost)
    {
        this.equipmentLost = equipmentLost;
    }

    public void merge(AAREquipmentLossPanelData equipmentLossPanelData)
    {
        equipmentLost.putAll(equipmentLossPanelData.getEquipmentLost());        
    }
}
