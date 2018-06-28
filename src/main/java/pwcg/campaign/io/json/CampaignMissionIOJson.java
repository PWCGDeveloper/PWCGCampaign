package pwcg.campaign.io.json;

import java.io.File;
import java.util.List;

import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class CampaignMissionIOJson
{
	private final static String MISSION_DATA_SUFFIX = ".MissionData.json";
	
    public static void writeJson(Campaign campaign, PwcgMissionData pwcgMissionData) throws PWCGException
	{
		JsonWriter<PwcgMissionData> jsonWriter = new JsonWriter<>();
		jsonWriter.writeAsJson(pwcgMissionData, campaign.getCampaignPath(), pwcgMissionData.getMissionHeader().getMissionFileName() + MISSION_DATA_SUFFIX);
	}

	public static PwcgMissionData readJson(Campaign campaign, String missionFileName) throws PWCGException
	{
		FileUtils fileUtils = new FileUtils();
		String campaignPath = campaign.getCampaignPath();
	    List<File> campaignMissionDataFiles = fileUtils.getFilesWithFilter(campaignPath, MISSION_DATA_SUFFIX);		
		for (File campaignMissionDataFile : campaignMissionDataFiles)
		{
			if (campaignMissionDataFile.getName().toLowerCase().startsWith(missionFileName.toLowerCase()))
			{
				JsonObjectReader<PwcgMissionData> jsoReader = new JsonObjectReader<>(PwcgMissionData.class);
				PwcgMissionData pwcgMissionData = jsoReader.readJsonFile(campaignPath, campaignMissionDataFile.getName());
				return pwcgMissionData;
			}
		}
		
		return null;
	}
}
