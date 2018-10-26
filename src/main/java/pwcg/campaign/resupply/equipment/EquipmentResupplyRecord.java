package pwcg.campaign.resupply.equipment;

import pwcg.campaign.plane.EquippedPlane;

public class EquipmentResupplyRecord
{
    private EquippedPlane equippedPlane;
    private int transferTo;

    public EquipmentResupplyRecord(EquippedPlane equippedPlane, int transferTo)
    {
        this.equippedPlane  = equippedPlane;
        this.transferTo  = transferTo;
    }
    
    public EquippedPlane getEquippedPlane()
    {
        return equippedPlane;
    }

    public int getTransferTo()
    {
        return transferTo;
    }
}
