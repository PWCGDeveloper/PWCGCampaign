package pwcg.campaign.squadron;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.SkirmishProfile;
import pwcg.campaign.SkirmishProfileType;
import pwcg.campaign.io.json.SkirmishProfileIOJson;
import pwcg.core.exception.PWCGException;

public class SkirmishProfileManager 
{
    private Map<SkirmishProfileType, SkirmishProfile> skirmishProfiles = new HashMap<>();

	public SkirmishProfileManager ()
	{
	}

	public void initialize() throws PWCGException 
	{
        skirmishProfiles.clear();
        skirmishProfiles = SkirmishProfileIOJson.readJson(); 
	}

	public SkirmishProfile getSkirmishProfile(SkirmishProfileType profileType) throws PWCGException
	{
		if(skirmishProfiles.containsKey(profileType))
		{
		    SkirmishProfile skirmishProfile = skirmishProfiles.get(profileType);
			return skirmishProfile;
		}
		
    	throw new PWCGException("Skirmish profile not found for " + profileType);
	}
}
