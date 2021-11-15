package pwcg.campaign.io.json;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.core.config.ConfigSet;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;
import pwcg.core.utils.PWCGLogger;

public class ConfigurationIOJson 
{
    public static void writeJsonConfigSet(String directory, ConfigSet configSet) throws PWCGException
	{
		PwcgJsonWriter<ConfigSet> jsonWriter = new PwcgJsonWriter<>();
		jsonWriter.writeAsJson(configSet, directory, configSet.getConfigSetName() + ".json");
	}


    public static Map<String, ConfigSet> readJson(String sourceDirectory) throws PWCGException
	{
	    Map<String, ConfigSet> configSets = new HashMap<String, ConfigSet>();
	    
		List<File> jsonFiles = FileUtils.getFilesWithFilter(sourceDirectory, ".json");
		File currentJsonFile = null;
		try
		{
			for (File jsonFile : jsonFiles)
			{
				currentJsonFile = jsonFile;
				
				JsonObjectReader<ConfigSet> jsoReader = new JsonObjectReader<>(ConfigSet.class);
				ConfigSet configSet = jsoReader.readJsonFile(sourceDirectory, jsonFile.getName()); 
				configSets.put(configSet.getConfigSetName(), configSet);
			}
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			throw new PWCGException("Malfomed JSON in file " + currentJsonFile.getAbsolutePath());
		}
		
		return configSets;
	}
}
