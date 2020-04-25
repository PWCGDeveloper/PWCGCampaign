package pwcg.campaign.group;

import java.io.File;
import java.util.Map;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.JsonObjectReader;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;

public class BlockDefinitionManager
{
    private static BlockDefinitionManager instance = null;

    private Map<String, BlockDefinition> blockDefinitions;

    public static BlockDefinitionManager getInstance()
    {
        if (instance == null)
        {
            configureBlockDefinitions();
        }

        return instance;
    }

    private static void configureBlockDefinitions()
    {
        try
        {
            readJson();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
    }


    private static void readJson() throws PWCGException, PWCGIOException
    {
        JsonObjectReader<BlockDefinitionManager> jsonReader = new JsonObjectReader<>(BlockDefinitionManager.class);
        instance = jsonReader.readJsonFile(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir(), "Blocks.json");
    }


    public BlockDefinition getBlockDefinition(String name) 
    {
        return blockDefinitions.get(name.toLowerCase());
    }

    public BlockDefinition getBlockDefinition(FixedPosition fixedPosition)
    {
        File script = new File(fixedPosition.getScript());
        String name = script.getName().split("\\.")[0];
        return blockDefinitions.get(name.toLowerCase());
    }
}
