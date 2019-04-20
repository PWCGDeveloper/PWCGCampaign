package pwcg.aar.campaigndate;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class AARTimePassedAfterMission
{
    protected Campaign campaign;

    public AARTimePassedAfterMission(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public Date calcNewDate() throws PWCGException 
    {
        int daysForMission = calculateDaysForMission();        
        Date newDate = DateUtils.advanceTimeDays(campaign.getDate(), daysForMission);
        return newDate;
    }
    

    private int calculateDaysForMission() throws PWCGException 
    {
        CampaignDaysPassed campaignDaysPassed = new CampaignDaysPassed(campaign);
        int daysAfterMission = campaignDaysPassed.calcDaysForMission();
        return daysAfterMission;
    }
}