package pwcg.campaign.io.json;

import pwcg.campaign.group.NonScriptedBlockPositions;
import pwcg.core.exception.PWCGException;

public class NonScriptedBlockPositionIOJson {

	public static void writeJson(NonScriptedBlockPositions nonScriptedGroundPositions, String directory, String filename) throws PWCGException
	{
		PwcgJsonWriter<NonScriptedBlockPositions> jsonWriter = new PwcgJsonWriter<>();			
		jsonWriter.writeAsJson(nonScriptedGroundPositions, directory, filename + ".json");
	}

	public static NonScriptedBlockPositions readJson(String directory, String filename) throws PWCGException, PWCGException
	{
		JsonObjectReader<NonScriptedBlockPositions> jsonReader = new JsonObjectReader<>(NonScriptedBlockPositions.class);
		NonScriptedBlockPositions nonScriptedGroundPositions = jsonReader.readJsonFile(directory, filename);
		return nonScriptedGroundPositions;
	}
}
