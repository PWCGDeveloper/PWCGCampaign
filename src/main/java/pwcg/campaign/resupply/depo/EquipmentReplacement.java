package pwcg.campaign.resupply.depo;

import java.util.Date;

import pwcg.campaign.plane.Equipment;

public class EquipmentReplacement
{
    private Equipment equipment = new Equipment();
    private int equipmentPoints;
    private Date lastReplacementDate;

    public Equipment getEquipment()
    {
        return equipment;
    }

    public void setEquippment(Equipment equippedPlanes)
    {
        this.equipment = equippedPlanes;
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
}
