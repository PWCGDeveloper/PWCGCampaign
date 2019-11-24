package pwcg.campaign.io.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.coop.model.CoopPersona;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class CoopPersonaIOJson 
{	
    public static void writeJson(List<CoopPersona> coopPersonas) throws PWCGException
	{
		for (CoopPersona coopPersona : coopPersonas)
		{
			writeJson(coopPersona);
		}
	}

    public static void writeJson(CoopPersona coopPersona) throws PWCGException
	{
		JsonWriter<CoopPersona> jsonWriter = new JsonWriter<>();
        String coopPersonaDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCoopDir() + "Pilots\\";
		jsonWriter.writeAsJson(coopPersona, coopPersonaDir, coopPersona.getPilotName() + ".json");
	}

	public static List<CoopPersona> readCoopPersonas() throws PWCGException
	{
	    List<CoopPersona> coopPersonas = new ArrayList<>();
		
		String coopPersonaDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCoopDir() + "Pilots\\";
		FileUtils fileUtils = new FileUtils();
		List<File> jsonFiles = fileUtils.getFilesWithFilter(coopPersonaDir, ".json");
		for (File jsonFile : jsonFiles)
		{
			JsonObjectReader<CoopPersona> jsonReader = new JsonObjectReader<>(CoopPersona.class);
			CoopPersona coopPersona = jsonReader.readJsonFile(coopPersonaDir, jsonFile.getName()); 			
			coopPersonas.add(coopPersona);
		}
		
		return coopPersonas;
	}
}
