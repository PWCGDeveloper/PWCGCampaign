package pwcg.campaign.squadron;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NightMissionSet
{
    private List<NightMissionPeriod> nightMissionPeriods = new ArrayList<>();

    public int determineNighMissionOdds(Date date)
    {
        NightMissionPeriod selectedNightMissionForPeriod = null;
        for (NightMissionPeriod squadronRole : nightMissionPeriods)
        {
            Date startDate = squadronRole.getStartDate();
            Date endDate = squadronRole.getEndDate();

            if (date.before(startDate))
            {
            }
            else if (date.after(endDate))
            {
            }
            else
            {
                selectedNightMissionForPeriod = squadronRole;
            }
        }
        
        int nightMissionOdds = 0;
        if (selectedNightMissionForPeriod != null)
        {
            nightMissionOdds = selectedNightMissionForPeriod.getNightMissionOdds();
        }

        return nightMissionOdds;
    }
}
