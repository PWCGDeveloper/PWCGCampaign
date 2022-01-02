package pwcg.campaign.io.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.HistoricalAce;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class HistoricalAceIOJson
{
    public static void writeJson(HistoricalAce historicalAce) throws PWCGException
    {
        PwcgJsonWriter<HistoricalAce> jsonWriter = new PwcgJsonWriter<>();
        jsonWriter.writeAsJson(historicalAce, PWCGContext.getInstance().getDirectoryManager().getPwcgAcesDir(), historicalAce.getName() + ".json");
    }

	public static List<HistoricalAce> readJson() throws PWCGException
	{
		List<HistoricalAce> historicalAces = new ArrayList<>();
		
		List<File> jsonFiles = FileUtils.getFilesWithFilter(PWCGContext.getInstance().getDirectoryManager().getPwcgAcesDir(), ".json");

		for (File jsonFile : jsonFiles)
		{
			JsonObjectReader<HistoricalAce> jsoReader = new JsonObjectReader<>(HistoricalAce.class);
			HistoricalAce historicalAce = jsoReader.readJsonFile(PWCGContext.getInstance().getDirectoryManager().getPwcgAcesDir(), jsonFile.getName()); 
			historicalAces.add(historicalAce);
		}
		
		return historicalAces;
	}
}
