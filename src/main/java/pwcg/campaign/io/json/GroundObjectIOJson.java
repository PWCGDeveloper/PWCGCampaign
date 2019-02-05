package pwcg.campaign.io.json;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.GroundStructureGroup;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;

public class GroundObjectIOJson 
{
    public static void writeJson(GroundStructureGroup groundStructureGroup, String mapName, String filename) throws PWCGException
    {
        JsonWriter<GroundStructureGroup> jsonWriter = new JsonWriter<>();         
        String mapDir = formPath(mapName);
        jsonWriter.writeAsJson(groundStructureGroup, mapDir, filename + ".json");
    }

	public static GroundStructureGroup readJson(String mapName) throws PWCGException, PWCGIOException
	{
		JsonObjectReader<GroundStructureGroup> jsonReader = new JsonObjectReader<>(GroundStructureGroup.class);
		String mapDir = formPath(mapName);
		GroundStructureGroup groundStructureGroup = jsonReader.readJsonFile(mapDir, "GroundStructures.json");
		return groundStructureGroup;
	}
	
	private static String formPath(String mapName) {
	    return PWCGContextManager.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\";
	}
}
