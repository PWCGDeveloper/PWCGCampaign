package pwcg.campaign.plane;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class Equipment
{
    private Map<Integer, EquippedPlane> equippedPlanes = new ConcurrentHashMap<>();

    public EquippedPlane getEquippedPlane(int planeSerialNumber)
    {
        return equippedPlanes.get(planeSerialNumber);
    }

    public Map<Integer, EquippedPlane> getActiveEquippedPlanes()
    {
        Map<Integer, EquippedPlane> activeEquippedPlanes = new HashMap<>();
        for (EquippedPlane equippedPlane : equippedPlanes.values())
        {
            if (equippedPlane.getPlaneStatus() == PlaneStatus.STATUS_DEPLOYED && equippedPlane.getDateRemovedFromService() == null)
            {
                activeEquippedPlanes.put(equippedPlane.getSerialNumber(), equippedPlane);
            }
        }
        return activeEquippedPlanes;
    }

    public Map<Integer, EquippedPlane> getRecentlyInactiveEquippedPlanes(Date date) throws PWCGException
    {
        Map<Integer, EquippedPlane> recentlyInactiveEquippedPlanes = new HashMap<>();
        for (EquippedPlane equippedPlane : equippedPlanes.values())
        {
            if (equippedPlane.getDateRemovedFromService() != null)
            {
                if (equippedPlane.getDateRemovedFromService().after(DateUtils.removeTimeDays(date, 7)))
                {
                    recentlyInactiveEquippedPlanes.put(equippedPlane.getSerialNumber(), equippedPlane);
                }
            }
        }
        return recentlyInactiveEquippedPlanes;
    }

    public boolean isSquadronEquipmentViable()
    {
        if (getActiveEquippedPlanes().size() > (Squadron.SQUADRON_STAFF_SIZE / 2))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void setEquippedPlanes(Map<Integer, EquippedPlane> equippedPlanes)
    {
        this.equippedPlanes = equippedPlanes;
    }
    
    public Map<Integer, EquippedPlane> getEquippedPlanes()
    {
        return equippedPlanes;
    }

    public void addEquippedPlane(EquippedPlane equippedPlane)
    {
        equippedPlanes.put(equippedPlane.getSerialNumber(), equippedPlane);
    }

    public EquippedPlane removeBestEquippedPlaneForArchType(List<String> activeArchTypes)
    {
        EquippedPlane selectedPlane = null;
        for (EquippedPlane equippedPlane : getPlanesForArchTypes(activeArchTypes))
        {
            if (selectedPlane == null || selectedPlane.getGoodness() < equippedPlane.getGoodness())
            {
                selectedPlane = equippedPlane;
            }
        }
        if (selectedPlane != null)
        {
            return removeEquippedPlane(selectedPlane.getSerialNumber());
        }
        else
        {
            return null;
        }
    }

    public List<EquippedPlane> getPlanesForArchTypes(List<String> activeArchTypes)
    {
        List<EquippedPlane> planesForArchType = new ArrayList<>();
        for (EquippedPlane equippedPlane : equippedPlanes.values())
        {
            for (String archTypeName : activeArchTypes)
            {
                if (equippedPlane.getArchType().equals(archTypeName))
                {
                    planesForArchType.add(equippedPlane);
                }
            }
        }
        return planesForArchType;
    }

    public EquippedPlane removeEquippedPlane(Integer planeSerialNumber)
    {
        return equippedPlanes.remove(planeSerialNumber);
    }
}
