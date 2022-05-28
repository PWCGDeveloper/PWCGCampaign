package pwcg.campaign.io.json;

import pwcg.campaign.group.ScriptedFixedPositions;
import pwcg.core.exception.PWCGException;

public class ScriptedFixedPositionIOJson {

	public static void writeJson(ScriptedFixedPositions fixedPositions, String directory, String filename) throws PWCGException
	{
		PwcgJsonWriter<ScriptedFixedPositions> jsonWriter = new PwcgJsonWriter<>();			
		jsonWriter.writeAsJson(fixedPositions, directory, filename + ".json");
	}

	public static ScriptedFixedPositions readJson(String directory, String filename) throws PWCGException, PWCGException
	{
		JsonObjectReader<ScriptedFixedPositions> jsonReader = new JsonObjectReader<>(ScriptedFixedPositions.class);
		ScriptedFixedPositions rofAirfields = jsonReader.readJsonFile(directory, filename + ".json");
		return rofAirfields;
	}
}
