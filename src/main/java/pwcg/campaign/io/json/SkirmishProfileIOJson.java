package pwcg.campaign.io.json;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.SkirmishProfile;
import pwcg.campaign.SkirmishProfileType;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class SkirmishProfileIOJson 
{
	public static Map<SkirmishProfileType, SkirmishProfile> readJson() throws PWCGException
	{
		JsonObjectReader<SkirmishProfile> jsoReader = new JsonObjectReader<>(SkirmishProfile.class);
		String directory = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + "\\SkirmishProfiles\\";
		
		Map<SkirmishProfileType, SkirmishProfile> skirmishesProfiles = new HashMap<>();
        for (File file : FileUtils.getFilesInDirectory(directory))
        {
            SkirmishProfile skirmishProfile = jsoReader.readJsonFile(directory, file.getName()); 
            skirmishesProfiles.put(skirmishProfile.getProfileType(), skirmishProfile);
        }
		return skirmishesProfiles;
	}
}
