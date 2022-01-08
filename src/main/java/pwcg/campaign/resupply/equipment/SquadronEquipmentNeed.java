package pwcg.campaign.resupply.equipment;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.resupply.ISquadronNeed;
import pwcg.campaign.tank.Equipment;
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
        Equipment equipment = campaign.getEquipmentManager().getEquipmentForCompany(squadron.getCompanyId());
        int activeSquadronSize = equipment.getActiveEquippedTanks().size();
        int recentlyInactive = equipment.getRecentlyInactiveEquippedTanks(campaign.getDate()).size();
      
        planesNeeded = Company.COMPANY_EQUIPMENT_SIZE - activeSquadronSize - recentlyInactive;

    }

    @Override
    public int getSquadronId()
    {
        return squadron.getCompanyId();
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
