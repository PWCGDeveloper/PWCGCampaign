package pwcg.campaign.io.json;

import java.io.File;

import pwcg.campaign.Skirmishes;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class SkirmishIOJson 
{
	public static Skirmishes readJson(String mapName) throws PWCGException
	{
		JsonObjectReader<Skirmishes> jsoReader = new JsonObjectReader<>(Skirmishes.class);
		String directory = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\Skirmishes\\";
		
		Skirmishes skirmishesForMap = new Skirmishes();
        for (File file : FileUtils.getFilesInDirectory(directory))
        {
            Skirmishes skirmishesForBattle = jsoReader.readJsonFile(directory, file.getName()); 
            skirmishesForMap.addSkirmish(skirmishesForBattle);
        }
		return skirmishesForMap;
	}
}
