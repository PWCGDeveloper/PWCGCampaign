package pwcg.aar.campaigndate;

import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.Role;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.utils.MoonPhases;

public class NightDateAdjustment
{
    public Campaign campaign;

    public NightDateAdjustment(Campaign campaign)
    {
        this.campaign = campaign;
    }
        

    public Date adjustForNightOperations(Date date) throws PWCGException 
    {
        Role primaryRole = campaign.determineSquadron().determineSquadronPrimaryRole(campaign.getDate());
        if (primaryRole == Role.ROLE_STRAT_BOMB)
        {
            Date adjustedDate = DateUtils.getDateByTime(date.getTime());
    
            // Find out the previous and next new moons
            Date prevFullMoon = null;
            Date nextFullMoon = null;
            List<Date> fullMoonDates = MoonPhases.getMoonPhaseDates();
            for (Date fullMoonDate : fullMoonDates)
            {
                if (adjustedDate.after(fullMoonDate))
                {
                    prevFullMoon = fullMoonDate;
                }
                else
                {
                    nextFullMoon = fullMoonDate;
                    break;
                }
            }

            if (prevFullMoon != null && nextFullMoon != null)
            {
                Date prevFullMoonPlusThree = DateUtils.advanceTimeDays(prevFullMoon, 3);
                Date nextFullMoonMinusThree = DateUtils.removeTimeDays(prevFullMoon, 3);
    
                if (adjustedDate.after(prevFullMoonPlusThree) && adjustedDate.before(nextFullMoonMinusThree))
                {
                    adjustedDate = DateUtils.getDateByTime(nextFullMoon.getTime());
                }
            }

            return adjustedDate;
        }
        else
        {
            return date;
        }
        
    }

}
