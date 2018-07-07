package pwcg.campaign.io.json;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class AircraftIOJson 
{
	public static Map<String, PlaneType> readJson() throws PWCGException
	{
	    Map<String, PlaneType> planeMap = new TreeMap<>();

		FileUtils fileUtils = new FileUtils();
		List<File> jsonFiles = fileUtils.getFilesWithFilter(PWCGContextManager.getInstance().getDirectoryManager().getPwcgAircraftInfoDir(), ".json");

		for (File jsonFile : jsonFiles)
		{
			JsonObjectReader<PlaneType> jsoReader = new JsonObjectReader<>(PlaneType.class);
			PlaneType planeType = jsoReader.readJsonFile(PWCGContextManager.getInstance().getDirectoryManager().getPwcgAircraftInfoDir(), jsonFile.getName());
			planeMap.put(planeType.getType(), planeType);
		}
		
		return planeMap;
	}
}
