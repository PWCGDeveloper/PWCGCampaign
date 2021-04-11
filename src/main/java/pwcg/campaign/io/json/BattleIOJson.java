package pwcg.campaign.io.json;

import pwcg.campaign.battle.Battles;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;

public class BattleIOJson 
{
	public static Battles readJson(String mapName) throws PWCGException
	{
		JsonObjectReader<Battles> jsoReader = new JsonObjectReader<>(Battles.class);
		String directory = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\";
		Battles battles = jsoReader.readJsonFile(directory, "Battles.json"); 
		return battles;
	}
}
