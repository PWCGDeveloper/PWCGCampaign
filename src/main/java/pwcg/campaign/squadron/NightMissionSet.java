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
        for (NightMissionPeriod nightPeriod : nightMissionPeriods)
        {
            Date startDate = nightPeriod.getStartDate();
            Date endDate = nightPeriod.getEndDate();

            if (date.before(startDate))
            {
            }
            else if (date.after(endDate))
            {
            }
            else
            {
                selectedNightMissionForPeriod = nightPeriod;
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
