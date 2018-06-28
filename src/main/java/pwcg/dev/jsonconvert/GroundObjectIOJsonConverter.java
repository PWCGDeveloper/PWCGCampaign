package pwcg.dev.jsonconvert;

import java.util.List;

import pwcg.campaign.context.PWCGDirectoryManager;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.GroundStructureGroup;
import pwcg.campaign.group.airfield.AirfieldBlock;
import pwcg.campaign.io.json.JsonObjectReader;
import pwcg.campaign.io.json.JsonWriter;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.dev.jsonconvert.orig.io.GroundObjectsFile;

public class GroundObjectIOJsonConverter {

	List<List<Block>> railroadsJson = null;
    

    public void convert(String mapName)
    {
    	try
    	{
            writeJson(mapName);
	        readJson(mapName);
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    }


	private void writeJson(String mapName) throws PWCGException
	{
		GroundObjectsFile groundObjectsFile = new GroundObjectsFile();
		groundObjectsFile.readGroundObjects(mapName);
		
		GroundStructureGroup groundStructureGroup = new GroundStructureGroup();
		groundStructureGroup.setRailroadStations(groundObjectsFile.getRailroadStations());
		groundStructureGroup.setStandaloneBlocks(groundObjectsFile.getStandaloneBlocks());
		groundStructureGroup.setBridges(groundObjectsFile.getBridges());
		groundStructureGroup.setAirfieldBlocks(groundObjectsFile.getAirfieldBlocks());

		JsonWriter<GroundStructureGroup> jsonWriter = new JsonWriter<>();			
		jsonWriter.writeAsJson(groundStructureGroup, PWCGDirectoryManager.getInstance().getPwcgInputDir() + mapName + "\\", "GroundStructures.json");
	}

	private void readJson(String mapName) throws PWCGException, PWCGIOException
	{
		JsonObjectReader<GroundStructureGroup> jsonReader = new JsonObjectReader<>(GroundStructureGroup.class);
		GroundStructureGroup groundStructureGroup = jsonReader.readJsonFile(PWCGDirectoryManager.getInstance().getPwcgInputDir() + mapName + "\\", "GroundStructures.json");
		for (AirfieldBlock airfieldBlock : groundStructureGroup.getAirfieldBlocks())
		{
			System.out.println(airfieldBlock.getModel());
		}
	}
}
