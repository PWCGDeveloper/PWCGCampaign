package pwcg.campaign.resupply.depot;

import pwcg.campaign.plane.EquippedPlane;

public class EquipmentUpgradeRecord
{
    private EquippedPlane upgrade;
    private EquippedPlane replacedPlane;

    public EquipmentUpgradeRecord(EquippedPlane upgrade, EquippedPlane replacedPlane)
    {
        this.upgrade = upgrade;
        this.replacedPlane = replacedPlane;
    }
    
    public EquippedPlane getUpgrade()
    {
        return upgrade;
    }

    public EquippedPlane getReplacedPlane()
    {
        return replacedPlane;
    }

}
