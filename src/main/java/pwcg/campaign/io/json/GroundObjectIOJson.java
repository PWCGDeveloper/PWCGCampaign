package pwcg.campaign.io.json;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.GroundStructureGroup;
import pwcg.core.exception.PWCGException;

public class GroundObjectIOJson 
{
    public static void writeJson(GroundStructureGroup groundStructureGroup, String mapName, String filename) throws PWCGException
    {
        PwcgJsonWriter<GroundStructureGroup> jsonWriter = new PwcgJsonWriter<>();         
        String mapDir = formPath(mapName);
        jsonWriter.writeAsJson(groundStructureGroup, mapDir, filename + ".json");
    }

	public static GroundStructureGroup readJson(String mapName) throws PWCGException, PWCGException
	{
		JsonObjectReader<GroundStructureGroup> jsonReader = new JsonObjectReader<>(GroundStructureGroup.class);
		String mapDir = formPath(mapName);
		GroundStructureGroup groundStructureGroup = jsonReader.readJsonFile(mapDir, "GroundStructures.json");
		return groundStructureGroup;
	}
	
	private static String formPath(String mapName) {
	    String inputDir = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir();
	    return inputDir + mapName + "\\";
	}
}
