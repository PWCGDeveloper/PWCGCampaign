package pwcg.campaign.io.json;

import pwcg.campaign.group.FixedPositions;
import pwcg.core.exception.PWCGException;

public class FixedPositionIOJson {

	public static void writeJson(FixedPositions fixedPositions, String directory, String filename) throws PWCGException
	{
		PwcgJsonWriter<FixedPositions> jsonWriter = new PwcgJsonWriter<>();			
		jsonWriter.writeAsJson(fixedPositions, directory, filename + ".json");
	}

	public static FixedPositions readJson(String directory, String filename) throws PWCGException, PWCGException
	{
		JsonObjectReader<FixedPositions> jsonReader = new JsonObjectReader<>(FixedPositions.class);
		FixedPositions rofAirfields = jsonReader.readJsonFile(directory, filename + ".json");
		return rofAirfields;
	}
}
