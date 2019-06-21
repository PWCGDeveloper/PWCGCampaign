package pwcg.campaign.target.locator.targettype;

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

    public TargetPreferenceSet getTargetPreferenceSet()
    {
        return targetPreferenceSet;
    }
}
