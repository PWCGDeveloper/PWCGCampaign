package pwcg.aar.campaigndate;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARAdjustDateForNightMissions
{
    static public Date adjustForNightMissions(Campaign campaign, Date newDate) throws PWCGException
    {
        NightDateAdjustment nightDateAdjustment = new NightDateAdjustment(campaign);
        Date finalNewDate = nightDateAdjustment.adjustForNightOperations(newDate);
        return finalNewDate;
    }

}
