package pwcg.campaign.resupply.depot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneSorter;
import pwcg.campaign.plane.Role;
import pwcg.core.exception.PWCGException;

public class EquipmentDepot
{
    private Equipment equipment = new Equipment();
    private int equipmentPoints;
    private Date lastReplacementDate;

    public void setEquippment(Equipment equippedPlanes)
    {
        this.equipment = equippedPlanes;
    }

    public int getDepotSize()
    {
        return equipment.getAvailableDepotPlanes().size();
    }
    
    public void addPlaneToDepot(EquippedPlane equippedPlane)
    {
        equipment.addEquippedPlane(equippedPlane);
    }
    
    public EquippedPlane removeBestPlaneFromDepot(List<String> activeArchTypes)
    {
        return equipment.removeBestEquippedFromDepot(activeArchTypes); 
    }

    public EquippedPlane removeEquippedPlaneFromDepot(int planeSerialNumber)
    {
        return equipment.removeEquippedPlane(planeSerialNumber);
    }

    public EquippedPlane getPlaneFromDepot(int planeSerialNumber)
    {
        return equipment.getEquippedPlane(planeSerialNumber);
    }

    public List<EquippedPlane> getDepotAircraftForRole(Role role) throws PWCGException
    {
        List<EquippedPlane> planesInDepotForRole = new ArrayList<>();
        for (EquippedPlane equippedPlane : equipment.getAvailableDepotPlanes().values())
        {
            if (equippedPlane.getRoles().get(0) == role)
            {
                planesInDepotForRole.add(equippedPlane);
            }
        }
        List<EquippedPlane> sortedDepotForRole = PlaneSorter.sortEquippedPlanesByGoodness(planesInDepotForRole);
        return sortedDepotForRole;
    }

    public List<EquippedPlane> getAllPlanesInDepot() throws PWCGException
    {
        List<EquippedPlane> allPlanesInDepot = new ArrayList<>();
        for (EquippedPlane equippedPlane : equipment.getAvailableDepotPlanes().values())
        {
            allPlanesInDepot.add(equippedPlane);
        }
        return allPlanesInDepot;
    }

    public int getEquipmentPoints()
    {
        return equipmentPoints;
    }

    public void setEquipmentPoints(int equipmentPoints)
    {
        this.equipmentPoints = equipmentPoints;
    }

    public Date getLastReplacementDate()
    {
        return lastReplacementDate;
    }

    public void setLastReplacementDate(Date lastReplacementDate)
    {
        this.lastReplacementDate = lastReplacementDate;
    }

    public EquipmentUpgradeRecord getUpgrade(EquippedPlane equippedPlane)
    {
        for (EquippedPlane depotPlane : equipment.getAvailableDepotPlanes().values())
        {
            if (depotPlane.getArchType().equals(equippedPlane.getArchType()))
            {
                if (depotPlane.getGoodness() > equippedPlane.getGoodness())
                {
                    EquipmentUpgradeRecord upgradeRecord = new EquipmentUpgradeRecord(depotPlane, equippedPlane);
                    return upgradeRecord;
                }
            }
        }
        return null;
    }
}
