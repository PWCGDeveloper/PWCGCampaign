package pwcg.campaign.io.json;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.config.InternationalizationRecords;
import pwcg.core.exception.PWCGException;

public class InternationalizationRecordsIO 
{
	public static InternationalizationRecords readJson(String internationalizationFile) throws PWCGException
	{
	    try
	    {
	        return readJsonFromFile(internationalizationFile);
	    }
	    catch (Exception e)
	    {
	        return readJsonFromFile("International.En.json");
	    }
	}

	private static InternationalizationRecords readJsonFromFile(String internationalizationFile) throws PWCGException
    {
        JsonObjectReader<InternationalizationRecords> jsoReader = new JsonObjectReader<>(InternationalizationRecords.class);
        String directory = PWCGContext.getInstance().getDirectoryManager().getPwcgInternationalizationDir();
        InternationalizationRecords internationalizationRecords = jsoReader.readJsonFile(directory, internationalizationFile); 
        return internationalizationRecords;
    }

    public static void writeJson(InternationalizationRecords internationalizationRecords, String filename) throws PWCGException
    {
        PwcgJsonWriter<InternationalizationRecords> jsonWriter = new PwcgJsonWriter<>();
        String directory = PWCGContext.getInstance().getDirectoryManager().getPwcgInternationalizationDir();
        jsonWriter.writeAsJson(internationalizationRecords, directory, filename);
    }
}
