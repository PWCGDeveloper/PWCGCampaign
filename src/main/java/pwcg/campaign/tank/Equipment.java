package pwcg.campaign.tank;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class Equipment
{
    private Map<Integer, EquippedTank> equippedTanks = new HashMap<>();

    public EquippedTank getEquippedTank(int tankSerialNumber)
    {
        return equippedTanks.get(tankSerialNumber);
    }

    public Map<Integer, EquippedTank> getAvailableDepotTanks()
    {
        Map<Integer, EquippedTank> availableDepotPlanes = new HashMap<>();
        for (EquippedTank equippedTank : equippedTanks.values())
        {
            if (equippedTank.getPlaneStatus() == TankStatus.STATUS_DEPOT)
            {
                availableDepotPlanes.put(equippedTank.getSerialNumber(), equippedTank);
            }
        }
        return availableDepotPlanes;
    }

    public Map<Integer, EquippedTank> getActiveEquippedTanks()
    {
        Map<Integer, EquippedTank> activeEquippedPlanes = new TreeMap<>();
        for (EquippedTank equippedTank : equippedTanks.values())
        {
            if (equippedTank.getPlaneStatus() == TankStatus.STATUS_DEPLOYED && equippedTank.getDateRemovedFromService() == null)
            {
                activeEquippedPlanes.put(equippedTank.getSerialNumber(), equippedTank);
            }
        }
        return activeEquippedPlanes;
    }

    public Map<Integer, EquippedTank> getRecentlyInactiveEquippedTanks(Date date) throws PWCGException
    {
        Map<Integer, EquippedTank> recentlyInactiveEquippedPlanes = new HashMap<>();
        for (EquippedTank equippedTank : equippedTanks.values())
        {
            if (equippedTank.getDateRemovedFromService() != null)
            {
                if (equippedTank.getDateRemovedFromService().after(DateUtils.removeTimeDays(date, 7)))
                {
                    recentlyInactiveEquippedPlanes.put(equippedTank.getSerialNumber(), equippedTank);
                }
            }
        }
        return recentlyInactiveEquippedPlanes;
    }

    public boolean isCompanyEquipmentViable()
    {
        if (getActiveEquippedTanks().size() > (Company.COMPANY_STAFF_SIZE / 2))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void addEquippedTankToCompany(Campaign campaign, int squadronId, EquippedTank equippedTank) throws PWCGException
    {
        equippedTanks.put(equippedTank.getSerialNumber(), equippedTank);
    }

    public void addEPlaneToDepot(EquippedTank equippedTank) throws PWCGException
    {
        equippedTanks.put(equippedTank.getSerialNumber(), equippedTank);
    }

    public EquippedTank removeBestEquippedFromDepot(List<String> activeArchTypes)
    {
        EquippedTank selectedTank = null;
        for (EquippedTank equippedTank : getTanksForArchTypes(activeArchTypes))
        {
            if (selectedTank == null || selectedTank.getGoodness() < equippedTank.getGoodness())
            {
                selectedTank = equippedTank;
            }
        }
        if (selectedTank != null)
        {
            return removeEquippedTank(selectedTank.getSerialNumber());
        }
        else
        {
            return null;
        }
    }

    public List<EquippedTank> getTanksForArchTypes(List<String> activeArchTypes)
    {
        List<EquippedTank> planesForArchType = new ArrayList<>();
        for (EquippedTank equippedTank : equippedTanks.values())
        {
            for (String archTypeName : activeArchTypes)
            {
                if (equippedTank.getArchType().equals(archTypeName))
                {
                    planesForArchType.add(equippedTank);
                }
            }
        }
        return planesForArchType;
    }

    public List<String> getArchTypes()
    {
        Map<String, String> archTypeMap = new HashMap<>();
        for (EquippedTank equippedTank : equippedTanks.values())
        {
            archTypeMap.put(equippedTank.getArchType(), equippedTank.getArchType());
        }
        return new ArrayList<String>(archTypeMap.values());
    }

    public Map<Integer, EquippedTank> getEquippedTanks()
    {
        return equippedTanks;
    }

    public EquippedTank deactivateEquippedTankFromCompany(Integer tankSerialNumber, Date date)
    {
        EquippedTank equippedTank = equippedTanks.get(tankSerialNumber);
        if (equippedTank != null)
        {
            equippedTank.setPlaneStatus(TankStatus.STATUS_REMOVED_FROM_SERVICE);
            equippedTank.setDateRemovedFromService(date);
        }
        return equippedTank;
    }

    public EquippedTank removeEquippedTank(Integer tankSerialNumber)
    {
        return equippedTanks.remove(tankSerialNumber);
    }
}
