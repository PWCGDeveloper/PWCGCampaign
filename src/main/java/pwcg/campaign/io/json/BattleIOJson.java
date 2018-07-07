package pwcg.campaign.io.json;

import pwcg.campaign.Battles;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;

public class BattleIOJson 
{
	public static Battles readJson() throws PWCGException
	{
		JsonObjectReader<Battles> jsoReader = new JsonObjectReader<>(Battles.class);
		Battles battles = jsoReader.readJsonFile(PWCGContextManager.getInstance().getDirectoryManager().getPwcgInputDir(), "Battles.json"); 
		return battles;
	}
}
