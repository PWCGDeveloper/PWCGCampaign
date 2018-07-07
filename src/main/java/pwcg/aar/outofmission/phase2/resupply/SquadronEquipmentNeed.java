package pwcg.aar.outofmission.phase2.resupply;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class SquadronEquipmentNeed implements ISquadronNeed
{
    private Campaign campaign;
    private Squadron squadron;
    private int planesNeeded = 0;

    public SquadronEquipmentNeed(Campaign campaign, Squadron squadron)
    {
        this.campaign = campaign;
        this.squadron = squadron;
    }

    @Override
    public void determineResupplyNeeded() throws PWCGException
    {
        Equipment equipment = campaign.getEquipmentManager().getEquipmentForSquadron(squadron.getSquadronId());
        int activeSquadronSize = equipment.getActiveEquippedPlanes().size();
        int recentlyInactive = equipment.getRecentlyInactiveEquippedPlanes(campaign.getDate()).size();
      
        planesNeeded = Squadron.SQUADRON_EQUIPMENT_SIZE - activeSquadronSize - recentlyInactive;

    }

    @Override
    public int getSquadronId()
    {
        return squadron.getSquadronId();
    }

    @Override
    public boolean needsResupply()
    {
        return (planesNeeded > 0);
    }

    @Override
    public void noteResupply()
    {
        --planesNeeded;
    }

}
