package pwcg.campaign.skin;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.flight.plane.PlaneMcu;

public class TacticalCodeBuilder
{
    public static TacticalCode buildTacticalCode (Campaign campaign, Squadron squadron, PlaneMcu plane) throws PWCGException
    {
        try {
            TacticalCode tacticalCode = TacticalCodeFactory.getTacticalCode(squadron.getCountry().getCountry());
            tacticalCode.buildTacticalCode(campaign, plane, squadron);
            return tacticalCode;
        }
        catch (Exception e)
        {
            PWCGLogger.log(LogLevel.ERROR, "Invalid tactical code lookup for plane " + plane.getName() + " Squadron: " + squadron.getFileName());
            return null;
        }
    }
}
