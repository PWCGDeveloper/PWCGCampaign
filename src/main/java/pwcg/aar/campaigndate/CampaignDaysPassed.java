package pwcg.aar.campaigndate;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class CampaignDaysPassed
{
    private Campaign campaign = null;
    
    public CampaignDaysPassed (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public int calcDaysForMission() throws PWCGException 
    {
        int daysForMission = RandomNumberGenerator.getRandom(getDaysBetweenMission()) + 1;
        daysForMission = atLeastOneDay(daysForMission);
        return daysForMission;
    }

    private int atLeastOneDay(int daysForMission)
    {
        if (daysForMission < 1)
        {
            daysForMission = 1;
        }
        return daysForMission;
    }
    
    public int getDaysBetweenMission() throws PWCGException 
    {
        PWCGMap map = PWCGContext.getInstance().getCurrentMap();
        return map.getDaysBetweenMissionForDate(campaign.getDate());
    }
}
