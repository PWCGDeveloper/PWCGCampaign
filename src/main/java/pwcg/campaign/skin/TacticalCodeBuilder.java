package pwcg.campaign.skin;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.plane.PlaneMcu;

public class TacticalCodeBuilder
{
    public static TacticalCode buildTacticalCode (Campaign campaign, Squadron squadron, PlaneMcu plane) throws PWCGException
    {
        TacticalCode tacticalCode = TacticalCodeFactory.getTacticalCode(squadron.getCountry().getCountry());
        tacticalCode.buildTacticalCode(campaign, plane, squadron);
        return tacticalCode;
    }
}
