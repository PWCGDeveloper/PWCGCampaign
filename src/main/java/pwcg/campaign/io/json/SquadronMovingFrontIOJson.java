package pwcg.campaign.io.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadron.SquadronMovingFrontOverlay;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class SquadronMovingFrontIOJson
{
	public static List<SquadronMovingFrontOverlay> readJson() throws PWCGException
	{
		List<SquadronMovingFrontOverlay> overlayList = new ArrayList<>();
		
		FileUtils fileUtils = new FileUtils();
		List<File> jsonFiles = fileUtils.getFilesWithFilter(PWCGContextManager.getInstance().getDirectoryManager().getPwcgSquadronMovingFrontDir(), ".json");

		for (File jsonFile : jsonFiles)
		{
			JsonObjectReader<SquadronMovingFrontOverlay> jsoReader = new JsonObjectReader<>(SquadronMovingFrontOverlay.class);
			SquadronMovingFrontOverlay overlay = jsoReader.readJsonFile(PWCGContextManager.getInstance().getDirectoryManager().getPwcgSquadronMovingFrontDir(), jsonFile.getName()); 
			overlayList.add(overlay);
		}
		
		return overlayList;
	}

}
