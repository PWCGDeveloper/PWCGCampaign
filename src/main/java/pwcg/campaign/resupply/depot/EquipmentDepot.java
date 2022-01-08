package pwcg.campaign.resupply.depot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.campaign.tank.TankSorter;
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
        return equipment.getAvailableDepotTanks().size();
    }
    
    public void addPlaneToDepot(EquippedTank equippedPlane) throws PWCGException
    {
        equipment.addEPlaneToDepot(equippedPlane);
    }
    
    public EquippedTank removeBestPlaneFromDepot(List<String> activeArchTypes)
    {
        return equipment.removeBestEquippedFromDepot(activeArchTypes); 
    }

    public EquippedTank removeEquippedPlaneFromDepot(int planeSerialNumber)
    {
        return equipment.removeEquippedTank(planeSerialNumber);
    }

    public EquippedTank getPlaneFromDepot(int planeSerialNumber)
    {
        return equipment.getEquippedTank(planeSerialNumber);
    }

    public List<EquippedTank> getDepotAircraftForRole(PwcgRoleCategory roleCategory) throws PWCGException
    {
        List<EquippedTank> planesInDepotForRole = new ArrayList<>();
        for (EquippedTank equippedPlane : equipment.getAvailableDepotTanks().values())
        {
            if (equippedPlane.getRoleCategories().get(0) == roleCategory)
            {
                planesInDepotForRole.add(equippedPlane);
            }
        }
        List<EquippedTank> sortedDepotForRole = TankSorter.sortEquippedTanksByGoodness(planesInDepotForRole);
        return sortedDepotForRole;
    }

    public List<EquippedTank> getAllPlanesInDepot() throws PWCGException
    {
        List<EquippedTank> allPlanesInDepot = new ArrayList<>();
        for (EquippedTank equippedPlane : equipment.getAvailableDepotTanks().values())
        {
            allPlanesInDepot.add(equippedPlane);
        }
        return allPlanesInDepot;
    }

    public EquippedTank getAnyTankInDepot(int planeSerialNumber) throws PWCGException
    {
        return equipment.getEquippedTank(planeSerialNumber);
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

    public EquipmentUpgradeRecord getUpgrade(EquippedTank equippedPlane) throws PWCGException
    {
        List<EquippedTank> sortedPlanes = getPlanesForFromDepotBestToWorst(equipment.getAvailableDepotTanks());
        for (EquippedTank depotPlane : sortedPlanes)
        {
            if (isUpgradePlane(depotPlane, equippedPlane))
            {
                EquipmentUpgradeRecord upgradeRecord = new EquipmentUpgradeRecord(depotPlane, equippedPlane);
                return upgradeRecord;
            }
        }
        return null;
    }
    
    private boolean isUpgradePlane(EquippedTank depotPlane, EquippedTank equippedPlane)
    {
        if (!(depotPlane.getArchType().equals(equippedPlane.getArchType())))
        {
            return false;
        }
        
        if (!(depotPlane.getGoodness() > equippedPlane.getGoodness()))
        {
            return false;
        }
        
        if (equippedPlane.isEquipmentRequest())
        {
            return false;
        }

        return true;
    }

    private List<EquippedTank> getPlanesForFromDepotBestToWorst(Map<Integer, EquippedTank> planesForSquadron) throws PWCGException
    {
        List<EquippedTank> sortedPlanes = TankSorter.sortEquippedTanksByGoodness(new ArrayList<EquippedTank>(planesForSquadron.values()));
        return sortedPlanes;
    }
}
