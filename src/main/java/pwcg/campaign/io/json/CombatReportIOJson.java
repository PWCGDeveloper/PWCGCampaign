package pwcg.campaign.io.json;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.CombatReport;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.FileUtils;

public class CombatReportIOJson 
{
	public static void writeJson(Campaign campaign, CombatReport combatReport) throws PWCGException
	{
		JsonWriter<CombatReport> jsonWriter = new JsonWriter<>();
		jsonWriter.writeAsJson(combatReport, campaign.getCampaignPath(), DateUtils.getDateStringYYYYMMDD(combatReport.getDate()) + ".CombatReport.json");
	}

	public static Map<String, CombatReport> readJson(Campaign campaign) throws PWCGException
	{
	    Map<String, CombatReport> combatReportsForCampaign = new TreeMap<>();
		FileUtils fileUtils = new FileUtils();
	    List<File> combatReportFiles = fileUtils.getFilesWithFilter(campaign.getCampaignPath(), ".CombatReport.json");
		for (File combatReportFile : combatReportFiles)
		{
			JsonObjectReader<CombatReport> jsoReader = new JsonObjectReader<>(CombatReport.class);
			CombatReport combatReport = jsoReader.readJsonFile(campaign.getCampaignPath(), combatReportFile.getName()); 
			combatReportsForCampaign.put(DateUtils.getDateStringYYYYMMDD(combatReport.getDate()), combatReport);
		}
		
		return combatReportsForCampaign;
	}
}
