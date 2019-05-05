package pwcg.campaign.io.json;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.CombatReport;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.FileUtils;

public class CombatReportIOJson 
{
	public static void writeJson(Campaign campaign, CombatReport combatReport) throws PWCGException
	{
        String combatReportPath = buildCombatReportPath(campaign, combatReport.getPilotSerialNumber());
        File combatReportDir = new File(combatReportPath);
        if (!combatReportDir.exists())
        {
            combatReportDir.mkdirs();
        }
        JsonWriter<CombatReport> jsonWriter = new JsonWriter<>();
        jsonWriter.writeAsJson(combatReport, combatReportPath, DateUtils.getDateStringYYYYMMDD(combatReport.getDate()) + ".CombatReport.json");
	}

	public static Map<String, CombatReport> readJson(Campaign campaign, Integer pilotSerialNumber) throws PWCGException
	{
	    Map<String, CombatReport> combatReportsForCampaign = new TreeMap<>();
		FileUtils fileUtils = new FileUtils();
        String combatReportPath = buildCombatReportPath(campaign, pilotSerialNumber);
	    List<File> combatReportFiles = fileUtils.getFilesWithFilter(combatReportPath, ".CombatReport.json");
		for (File combatReportFile : combatReportFiles)
		{
			JsonObjectReader<CombatReport> jsoReader = new JsonObjectReader<>(CombatReport.class);
			CombatReport combatReport = jsoReader.readJsonFile(combatReportPath, combatReportFile.getName()); 
			combatReportsForCampaign.put(DateUtils.getDateStringYYYYMMDD(combatReport.getDate()), combatReport);
		}
		
		return combatReportsForCampaign;
	}

    private static String buildCombatReportPath(Campaign campaign, Integer pilotSerialNumber)
    {
        String combatReportPath = PWCGContextManager.getInstance().getDirectoryManager().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\CombatReports\\" + pilotSerialNumber + "\\";
        return combatReportPath;
    }
}
