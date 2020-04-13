package pwcg.campaign.io.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.coop.model.CoopPersonaOld;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class CoopPersonaOldIOJson 
{	
	public static List<CoopPersonaOld> readCoopPersonas() throws PWCGException
	{
	    List<CoopPersonaOld> coopPersonas = new ArrayList<>();
		
		String coopPersonaDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCoopDir() + "Pilots\\";
		List<File> jsonFiles = FileUtils.getFilesWithFilter(coopPersonaDir, ".json");
		for (File jsonFile : jsonFiles)
		{
			JsonObjectReader<CoopPersonaOld> jsonReader = new JsonObjectReader<>(CoopPersonaOld.class);
			CoopPersonaOld coopPersona = jsonReader.readJsonFile(coopPersonaDir, jsonFile.getName()); 			
			coopPersonas.add(coopPersona);
		}
		
		return coopPersonas;
	}
}
