package pwcg.campaign.io.json;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.LocationSet;

public class LocationIOJson
{
	public static void writeJson(String directoryName, String filename, LocationSet locationSet) throws PWCGException
	{
		PwcgJsonWriter<LocationSet> jsonWriter = new PwcgJsonWriter<>();
		jsonWriter.writeAsJson(locationSet, directoryName, filename + ".json");
	}

	public static LocationSet readJson(String directoryName, String filename) throws PWCGException
	{		
		JsonObjectReader<LocationSet> jsoReader = new JsonObjectReader<>(LocationSet.class);
		LocationSet locationSet = jsoReader.readJsonFile(directoryName, filename + ".json");
		return locationSet;
	}
}
