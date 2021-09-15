package pwcg.campaign.resupply.depot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneSorter;
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;

public class EquipmentDepot
{
    public static final int NUM_POINTS_PER_PLANE = 10;
    
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
    
    public void addPlaneToDepot(EquippedPlane equippedPlane) throws PWCGException
    {
        equipment.addEPlaneToDepot(equippedPlane);
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

    public List<EquippedPlane> getDepotAircraftForRole(PwcgRoleCategory roleCategory) throws PWCGException
    {
        List<EquippedPlane> planesInDepotForRole = new ArrayList<>();
        for (EquippedPlane equippedPlane : equipment.getAvailableDepotPlanes().values())
        {
            if (equippedPlane.getRoleCategories().get(0) == roleCategory)
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

    public EquippedPlane getAnyPlaneInDepot(int planeSerialNumber) throws PWCGException
    {
        return equipment.getEquippedPlane(planeSerialNumber);
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

    public EquipmentUpgradeRecord getUpgrade(EquippedPlane equippedPlane) throws PWCGException
    {
        List<EquippedPlane> sortedPlanes = getPlanesForFromDepotBestToWorst(equipment.getAvailableDepotPlanes());
        for (EquippedPlane depotPlane : sortedPlanes)
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
    
    private List<EquippedPlane> getPlanesForFromDepotBestToWorst(Map<Integer, EquippedPlane> planesForSquadron) throws PWCGException
    {
        List<EquippedPlane> sortedPlanes = PlaneSorter.sortEquippedPlanesByGoodness(new ArrayList<EquippedPlane>(planesForSquadron.values()));
        return sortedPlanes;
    }
}
