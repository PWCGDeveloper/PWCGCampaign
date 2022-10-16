package pwcg.campaign.skin;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.plane.PlaneMcu;

public class TacticalCodeGermany extends TacticalCode
{
    public TacticalCode buildTacticalCode(Campaign campaign, PlaneMcu plane, Squadron squadron) throws PWCGException
    {
        if (plane.getSkin().isUseTacticalCodes() == false)
        {
            return null;
        }
        
        if (squadron.determineDisplayName(campaign.getDate()).contains("JG") || 
                squadron.determineDisplayName(campaign.getDate()).contains("JV") || 
                squadron.determineDisplayName(campaign.getDate()).contains("Sch.G") || 
                squadron.determineDisplayName(campaign.getDate()).contains("SG"))
        {
            TacticalCodeGermanFighters tacticalCodeGermanFighters = new TacticalCodeGermanFighters();
            tacticalCodeGermanFighters.buildTacticalCode(campaign, squadron, plane);
            super.createExplicitCodes(tacticalCodeGermanFighters.getCodes(), tacticalCodeGermanFighters.getColors());
        }
        else
        {
            TacticalCodeGermanBombers tacticalCodeGermanBombers = new TacticalCodeGermanBombers();
            tacticalCodeGermanBombers.buildTacticalCode(campaign, squadron, plane);
            super.createExplicitCodes(tacticalCodeGermanBombers.getCodes(), tacticalCodeGermanBombers.getColors());
        }
        
        return this;
    }
}
