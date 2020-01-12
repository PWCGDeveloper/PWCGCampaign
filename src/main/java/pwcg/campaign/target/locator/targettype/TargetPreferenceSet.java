package pwcg.campaign.target.locator.targettype;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.target.TargetType;

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

    public TargetType getTargetPreferenceToUse(Date date, Side side) throws PWCGException 
    {
        TargetPreference selectedTargetPreference = getTargetPreferenceForDate(date, side);
        TargetType targetType = getPreferredTargetType(selectedTargetPreference);
        return targetType;
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

    private TargetType getPreferredTargetType(TargetPreference selectedTargetPreference)
    {
        TargetType targetType = TargetType.TARGET_NONE;        
        if (selectedTargetPreference != null)
        {
            int roll = RandomNumberGenerator.getRandom(100);
            if (selectedTargetPreference.getOddsOfUse() <= roll)
            {
                targetType = selectedTargetPreference.getTargetType();
            }
        }
        return targetType;
    }
}
