package pwcg.dev.jsonconvert;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.group.NonScriptedBlockPositions;
import pwcg.campaign.group.NonScriptedBlock;
import pwcg.campaign.io.json.NonScriptedBlockPositionIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DirectoryReader;
import pwcg.dev.jsonconvert.orig.io.GroundObjectsFileReader;

public class GameGroundObjectToJsonConverter
{
    private static final String inputDirectory = "D:\\PWCG\\MapObjects\\";
    private static final String outputDirectory = "D:\\PWCG\\workspacePWCG\\PWCGCampaign\\BoSData\\Input\\";

    private Map<String, GroundObjectsFileReader> readers = new HashMap<>();
    
    public static void main(String args[]) 
    {
        try
        {
            GameGroundObjectToJsonConverter converter = new GameGroundObjectToJsonConverter();
            converter.readAllMaps();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void readAllMaps() throws PWCGException
    {
        readMap("Moscow");
        readMap("Stalingrad");
        readMap("Kuban");
        readMap("Rheinland");
    }
    
    private void readMap(String mapName) throws PWCGException 
    {
        GroundObjectsFileReader reader = new GroundObjectsFileReader();
        readers.put(mapName, reader);
     
        DirectoryReader directoryReader = new DirectoryReader();
        directoryReader.sortFilesInDir(inputDirectory); 
        
        for (String groupFile : directoryReader.getSortedFilesWithFilter(mapName))
        {
            String groupFilePath = inputDirectory + groupFile;
            readGameInputFile(reader, groupFilePath);
        }
        
        NonScriptedBlockPositions groundObjectsForMap = new NonScriptedBlockPositions();
        for (NonScriptedBlock ground : reader.getGroundObjects())
        {
            groundObjectsForMap.addNonScriptedGround(ground);
        }
        
        String mapOutputDirectory = outputDirectory + mapName + "\\";
        NonScriptedBlockPositionIOJson.writeJson(groundObjectsForMap, mapOutputDirectory, "StaticGroundStructures");
    }
    
    private void readGameInputFile(GroundObjectsFileReader reader, String groupFilePath) throws PWCGException
    {
        reader.readGroundObjectsFromFile(groupFilePath);
    }
}
