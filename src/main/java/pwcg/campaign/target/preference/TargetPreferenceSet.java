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

    public List<TargetPreference> getTargetPreferences(Date date, Side side) throws PWCGException 
    {
        List<TargetPreference> targetPreferences = new ArrayList<>();
        for (TargetPreference targetPreference : targetPreferences)
        {
            if ((!date.before(targetPreference.getStartDate())) && !(date.after(targetPreference.getEndDate())) && targetPreference.getTargetSide() == side)
            {
                targetPreferences.add(targetPreference);
            }
        }
        return targetPreferences;
    }
}
