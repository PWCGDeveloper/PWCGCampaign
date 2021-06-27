package pwcg.campaign.io.json;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.personnel.Ranks;
import pwcg.core.exception.PWCGException;

public class RankIOJson 
{
	public static Ranks readJson() throws PWCGException, PWCGException
	{
		JsonObjectReader<Ranks> jsonReader = new JsonObjectReader<>(Ranks.class);
		Ranks ranks = jsonReader.readJsonFile(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + "Ranks\\", "Ranks.json");
		return ranks;
	}
}
