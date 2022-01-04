package pwcg.campaign.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TargetPreferenceSet
{
    private List<TargetPreferencePeriod> targetPreferencePeriods = new ArrayList<>();

    public TargetPreferencePeriod determineTargetPreference(Date date)
    {
        List<TargetPreferencePeriod> selectedTargetPreferenceForPeriods = new ArrayList<>();
        for (TargetPreferencePeriod targetPreferencePeriod : targetPreferencePeriods)
        {
            Date startDate = targetPreferencePeriod.getStartDate();
            Date endDate = targetPreferencePeriod.getEndDate();

            if (date.before(startDate))
            {
            }
            else if (date.after(endDate))
            {
            }
            else
            {
                selectedTargetPreferenceForPeriods.add(targetPreferencePeriod);
            }
        }

        if (selectedTargetPreferenceForPeriods.size() > 0)
        {
            Collections.shuffle(selectedTargetPreferenceForPeriods);
            return selectedTargetPreferenceForPeriods.get(0);
        }
        
        return null;
    }

    public List<TargetPreferencePeriod> getTargetPreferencePeriods()
    {
        return targetPreferencePeriods;
    }
    
    
}
