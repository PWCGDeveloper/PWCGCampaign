package pwcg.campaign.io.json;

import java.io.File;
import java.util.List;

import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class CampaignMissionIOJson
{
    public final static String MISSION_DATA_SUFFIX = ".MissionData.json";
	
    public static void writeJson(Campaign campaign, PwcgMissionData pwcgMissionData) throws PWCGException
	{
        String missionDataPath = buildMissionDataPath(campaign);
		JsonWriter<PwcgMissionData> jsonWriter = new JsonWriter<>();
		jsonWriter.writeAsJson(pwcgMissionData, missionDataPath, pwcgMissionData.getMissionHeader().getMissionFileName() + MISSION_DATA_SUFFIX);
	}

	public static PwcgMissionData readJson(Campaign campaign, String missionFileName) throws PWCGException
	{
        String missionDataPath = buildMissionDataPath(campaign);
	    List<File> campaignMissionDataFiles = FileUtils.getFilesWithFilter(missionDataPath, MISSION_DATA_SUFFIX);		
		for (File campaignMissionDataFile : campaignMissionDataFiles)
		{
			if (campaignMissionDataFile.getName().toLowerCase().startsWith(missionFileName.toLowerCase()))
			{
				JsonObjectReader<PwcgMissionData> jsoReader = new JsonObjectReader<>(PwcgMissionData.class);
				PwcgMissionData pwcgMissionData = jsoReader.readJsonFile(missionDataPath, campaignMissionDataFile.getName());
				return pwcgMissionData;
			}
		}
		
		return null;
	}
	

	public static String buildMissionDataPath(Campaign campaign)
    {
        String combatReportPath = PWCGContext.getInstance().getDirectoryManager().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\MissionData\\";
        return combatReportPath;
    }

}
