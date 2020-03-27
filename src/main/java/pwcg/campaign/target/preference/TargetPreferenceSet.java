package pwcg.campaign.target.preference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;

public class TargetPreferenceSet
{
    private List<TargetPreference> targetPreferences = new ArrayList<>();

    public List<TargetPreference> getTargetPreferences()
    {
        return targetPreferences;
    }

    public void setTargetPreferences(List<TargetPreference> targetPreferences)
    {
        this.targetPreferences = targetPreferences;
    }

    public TargetPreference getTargetPreferenceToUse(Date date, Side side) throws PWCGException 
    {
        return getTargetPreferenceForDate(date, side);
    }

    private TargetPreference getTargetPreferenceForDate(Date date, Side side)
    {
        TargetPreference selectedTargetPreference = null;
        for (TargetPreference targetPreference : targetPreferences)
        {
            if ((!date.before(targetPreference.getStartDate())) && !(date.after(targetPreference.getEndDate())) && targetPreference.getTargetSide() == side)
            {
                selectedTargetPreference = targetPreference;
            }
        }
        return selectedTargetPreference;
    }
}
