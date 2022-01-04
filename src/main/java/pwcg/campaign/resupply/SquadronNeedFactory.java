package pwcg.campaign.resupply;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.resupply.equipment.SquadronEquipmentNeed;
import pwcg.campaign.resupply.personnel.SquadronPersonnelNeed;

public class SquadronNeedFactory
{
    public enum SquadronNeedType
    {
        PERSONNEL,
        EQUIPMENT
    };
    
    SquadronNeedType squadronNeedType = SquadronNeedType.PERSONNEL;
    
    public SquadronNeedFactory(SquadronNeedType squadronNeedType)
    {
        this.squadronNeedType = squadronNeedType;
    }
    
    public ISquadronNeed buildSquadronNeed(Campaign campaign, Company squadron)
    {
        if (squadronNeedType == SquadronNeedType.PERSONNEL)
        {
            return new SquadronPersonnelNeed(campaign, squadron);
        }
        else
        {
            return new SquadronEquipmentNeed(campaign, squadron);
        }
    }
}
