package pwcg.campaign.io.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.skirmish.IconicSingleMission;
import pwcg.campaign.skirmish.IconicMissions;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class IconicMissionsIOJson 
{
	public static List<IconicSingleMission> readJson() throws PWCGException
	{
		JsonObjectReader<IconicMissions> jsoReader = new JsonObjectReader<>(IconicMissions.class);
		String directory = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + "\\IconicBattles\\";
		
		List<IconicSingleMission> iconicMissionList = new ArrayList<>();
        for (File file : FileUtils.getFilesInDirectory(directory))
        {
            IconicMissions iconicMissions = jsoReader.readJsonFile(directory, file.getName()); 
            iconicMissionList.addAll(iconicMissions.getIconicMissiones());
        }
		return iconicMissionList;
	}  
}
