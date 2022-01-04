package pwcg.campaign.resupply.depot;

import pwcg.campaign.tank.EquippedTank;

public class EquipmentUpgradeRecord
{
    private EquippedTank upgrade;
    private EquippedTank replacedPlane;

    public EquipmentUpgradeRecord(EquippedTank upgrade, EquippedTank replacedPlane)
    {
        this.upgrade = upgrade;
        this.replacedPlane = replacedPlane;
    }
    
    public EquippedTank getUpgrade()
    {
        return upgrade;
    }

    public EquippedTank getReplacedPlane()
    {
        return replacedPlane;
    }

}
