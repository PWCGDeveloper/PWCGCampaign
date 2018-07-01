package pwcg.campaign.plane;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.squadron.Squadron;

public class Equipment
{
    private Map<Integer, EquippedPlane> equippedPlanes = new HashMap<>();

    public Map<Integer, EquippedPlane> getEquippedPlanes()
    {
        return equippedPlanes;
    }

    public void setEquippedPlanes(Map<Integer, EquippedPlane> equippedPlanes)
    {
        this.equippedPlanes = equippedPlanes;
    }

    public void addEquippedPlane(EquippedPlane equippedPlane)
    {
        equippedPlanes.put(equippedPlane.getSerialNumber(), equippedPlane);
    }

    public void removeEquippedPlanes(Integer planeSerialNumber)
    {
        equippedPlanes.remove(planeSerialNumber);
    }

    public boolean isSquadronEquipmentViable()
    {
        if (equippedPlanes.size() > (Squadron.SQUADRON_STAFF_SIZE / 2))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
