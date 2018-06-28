package pwcg.campaign.io.json;

import pwcg.campaign.group.FixedPositions;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;

public class FixedPositionIOJson {

	public static void writeJson(FixedPositions fixedPositions, String directory, String filename) throws PWCGException
	{
		JsonWriter<FixedPositions> jsonWriter = new JsonWriter<>();			
		jsonWriter.writeAsJson(fixedPositions, directory, filename + ".json");
	}

	public static FixedPositions readJson(String directory, String filename) throws PWCGException, PWCGIOException
	{
		JsonObjectReader<FixedPositions> jsonReader = new JsonObjectReader<>(FixedPositions.class);
		FixedPositions rofAirfields = jsonReader.readJsonFile(directory, filename + ".json");
		return rofAirfields;
	}
}
