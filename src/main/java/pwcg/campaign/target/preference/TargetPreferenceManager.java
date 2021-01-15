package pwcg.campaign.target.preference;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.io.json.TargetPreferenceIOJson;
import pwcg.core.exception.PWCGException;

public class TargetPreferenceManager
{
    private TargetPreferenceSet targetPreferenceSet = new TargetPreferenceSet();
    
    public void configure(String mapName) throws PWCGException
    {
        targetPreferenceSet = TargetPreferenceIOJson.readJson(mapName);
        if (targetPreferenceSet == null)
        {
            targetPreferenceSet = new TargetPreferenceSet();
        }        
    }

    public List<TargetPreference> getTargetPreference(Campaign campaign, Side side) throws PWCGException
    {
        List<TargetPreference> targetPreferences = targetPreferenceSet.getTargetPreferences(campaign.getDate(), side);
        return targetPreferences;
    }
}
