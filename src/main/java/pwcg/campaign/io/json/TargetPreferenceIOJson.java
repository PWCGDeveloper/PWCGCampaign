package pwcg.campaign.io.json;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.target.TargetPreferenceSet;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;

public class TargetPreferenceIOJson 
{
	public static TargetPreferenceSet readJson(String mapName) throws PWCGException, PWCGIOException
	{
		JsonObjectReader<TargetPreferenceSet> jsonReader = new JsonObjectReader<>(TargetPreferenceSet.class);
		TargetPreferenceSet targetPreferenceSet = jsonReader.readJsonFile(PWCGContextManager.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\", "TargetPreferences.json");
		return targetPreferenceSet;
	}
}
