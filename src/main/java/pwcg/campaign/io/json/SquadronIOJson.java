package pwcg.campaign.io.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class SquadronIOJson 
{

    public static void writeJson(Squadron squadron) throws PWCGException
    {
        JsonWriter<Squadron> jsonWriter = new JsonWriter<>();
        String squadronDir = PWCGContext.getInstance().getDirectoryManager().getPwcgSquadronDir();
        jsonWriter.writeAsJson(squadron, squadronDir, squadron.getFileName());
    }

	public static List<Squadron> readJson() throws PWCGException
	{
	    List<Squadron> squadrons = new ArrayList<>();
	    
		FileUtils fileUtils = new FileUtils();
		List<File> jsonFiles = fileUtils.getFilesWithFilter(PWCGContext.getInstance().getDirectoryManager().getPwcgSquadronDir(), ".json");

		for (File jsonFile : jsonFiles)
		{
			JsonObjectReader<Squadron> jsonReader = new JsonObjectReader<>(Squadron.class);
			Squadron squadron = jsonReader.readJsonFile(PWCGContext.getInstance().getDirectoryManager().getPwcgSquadronDir(), jsonFile.getName()); 
			squadron.setFileName(jsonFile.getName());			
			squadrons.add(squadron);
		}

		return squadrons;
	}
}
