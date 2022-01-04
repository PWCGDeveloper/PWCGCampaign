package pwcg.campaign.resupply.equipment;

import pwcg.campaign.tank.EquippedTank;

public class EquipmentResupplyRecord
{
    private EquippedTank equippedPlane;
    private int transferTo;

    public EquipmentResupplyRecord(EquippedTank equippedPlane, int transferTo)
    {
        this.equippedPlane  = equippedPlane;
        this.transferTo  = transferTo;
    }
    
    public EquippedTank getEquippedPlane()
    {
        return equippedPlane;
    }

    public int getTransferTo()
    {
        return transferTo;
    }
}
