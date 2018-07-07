package pwcg.campaign.io.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.HistoricalAce;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class HistoricalAceIOJson
{
    public static void writeJson(HistoricalAce historicalAce) throws PWCGException
    {
        JsonWriter<HistoricalAce> jsonWriter = new JsonWriter<>();
        jsonWriter.writeAsJson(historicalAce, PWCGContextManager.getInstance().getDirectoryManager().getPwcgAcesDir(), historicalAce.getName() + ".json");
    }

	public static List<HistoricalAce> readJson() throws PWCGException
	{
		List<HistoricalAce> historicalAces = new ArrayList<>();
		
		FileUtils fileUtils = new FileUtils();
		List<File> jsonFiles = fileUtils.getFilesWithFilter(PWCGContextManager.getInstance().getDirectoryManager().getPwcgAcesDir(), ".json");

		for (File jsonFile : jsonFiles)
		{
			JsonObjectReader<HistoricalAce> jsoReader = new JsonObjectReader<>(HistoricalAce.class);
			HistoricalAce historicalAce = jsoReader.readJsonFile(PWCGContextManager.getInstance().getDirectoryManager().getPwcgAcesDir(), jsonFile.getName()); 
			historicalAces.add(historicalAce);
		}
		
		return historicalAces;
	}
}
