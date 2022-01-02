package pwcg.campaign.resupply.equipment;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.resupply.ISquadronNeed;
import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;

public class SquadronEquipmentNeed implements ISquadronNeed
{
    private Campaign campaign;
    private Company squadron;
    private int planesNeeded = 0;

    public SquadronEquipmentNeed(Campaign campaign, Company squadron)
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
      
        planesNeeded = Company.SQUADRON_EQUIPMENT_SIZE - activeSquadronSize - recentlyInactive;

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

    @Override
    public int getNumNeeded()
    {
        return planesNeeded;
    }

    public void setPlanesNeeded(int planesNeeded)
    {
        this.planesNeeded = planesNeeded;
    }
}
