package pwcg.aar.data;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.plane.EquippedPlane;

public class AAREquipmentLosses
{
    private Map<Integer, EquippedPlane> planesDestroyed = new HashMap<>();

    public void merge(AAREquipmentLosses equipmentEvents)
    {
        planesDestroyed.putAll(equipmentEvents.getPlanesDestroyed());
    }

    public void addPlaneDestroyed(EquippedPlane plane)
    {
        this.planesDestroyed.put(plane.getSerialNumber(), plane);
    }

    public Map<Integer, EquippedPlane> getPlanesDestroyed()
    {
        return planesDestroyed;
    }
    
    
}
