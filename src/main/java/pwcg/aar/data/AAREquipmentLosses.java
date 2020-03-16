package pwcg.aar.data;

import java.util.HashMap;
import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;

public class AAREquipmentLosses
{
    private Map<Integer, LogPlane> planesDestroyed = new HashMap<>();

    public void merge(AAREquipmentLosses equipmentEvents)
    {
        planesDestroyed.putAll(equipmentEvents.getPlanesDestroyed());
    }

    public void addPlaneDestroyed(LogPlane shotDownPlane)
    {
        this.planesDestroyed.put(shotDownPlane.getPlaneSerialNumber(), shotDownPlane);
    }

    public Map<Integer, LogPlane> getPlanesDestroyed()
    {
        return planesDestroyed;
    }
    
    
}
