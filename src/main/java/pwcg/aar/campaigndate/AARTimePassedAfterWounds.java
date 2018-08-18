package pwcg.aar.campaigndate;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class AARTimePassedAfterWounds
{
    protected Campaign campaign;

    public AARTimePassedAfterWounds(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public Date calcDateOfRecovery(int pilotStatus) throws PWCGException 
    {
        int daysForWounds = calculateDaysForWounds(pilotStatus);        
        Date newDateAfterWounds = DateUtils.advanceTimeDays(campaign.getDate(), daysForWounds);
        return newDateAfterWounds;
    }

    private int calculateDaysForWounds(int pilotStatus) throws PWCGException
    {
        CampaignDaysPassed campaignDaysPassed = new CampaignDaysPassed(campaign);
        int daysForWounds = campaignDaysPassed.calcDaysForWound(pilotStatus);
        return daysForWounds;
    }
}