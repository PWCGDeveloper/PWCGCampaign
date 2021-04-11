package pwcg.campaign.io.json;

import java.io.File;

import pwcg.campaign.battle.AmphibiousAssault;
import pwcg.campaign.battle.AmphibiousAssaults;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class AmphibiousAssaultIOJson 
{
	public static AmphibiousAssaults readJson(String mapName) throws PWCGException
	{
		JsonObjectReader<AmphibiousAssault> jsoReader = new JsonObjectReader<>(AmphibiousAssault.class);
		String directory = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\Amphibious\\";
		
		AmphibiousAssaults amphibiousAssaultsForMap = new AmphibiousAssaults();
        for (File file : FileUtils.getFilesInDirectory(directory))
        {
            AmphibiousAssault amphibiousAssault = jsoReader.readJsonFile(directory, file.getName()); 
            amphibiousAssaultsForMap.addAmphibiousAssault(amphibiousAssault);
        }
		return amphibiousAssaultsForMap;
	}
}
