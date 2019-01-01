package pwcg.campaign.io.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.coop.model.CoopPilot;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class CoopPilotIOJson 
{	
    public static void writeJson(List<CoopPilot> coopPilots) throws PWCGException
	{
		for (CoopPilot coopPilot : coopPilots)
		{
			writeJson(coopPilot);
		}
	}

    public static void writeJson(CoopPilot coopPilot) throws PWCGException
	{
		JsonWriter<CoopPilot> jsonWriter = new JsonWriter<>();
        String coopPilotDir = PWCGContextManager.getInstance().getDirectoryManager().getPwcgCoopDir() + "Pilots\\";
		jsonWriter.writeAsJson(coopPilot, coopPilotDir, coopPilot.getPilotName() + ".json");
	}

	public static List<CoopPilot> readCoopPilots() throws PWCGException
	{
	    List<CoopPilot> coopPilots = new ArrayList<>();
		
		String coopPilotDir = PWCGContextManager.getInstance().getDirectoryManager().getPwcgCoopDir() + "Pilots\\";
		FileUtils fileUtils = new FileUtils();
		List<File> jsonFiles = fileUtils.getFilesWithFilter(coopPilotDir, ".json");
		for (File jsonFile : jsonFiles)
		{
			JsonObjectReader<CoopPilot> jsonReader = new JsonObjectReader<>(CoopPilot.class);
			CoopPilot coopPilot = jsonReader.readJsonFile(coopPilotDir, jsonFile.getName()); 			
			coopPilots.add(coopPilot);
		}
		
		return coopPilots;
	}
}
