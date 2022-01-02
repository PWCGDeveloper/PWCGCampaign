package pwcg.campaign.io.json;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.CombatReport;
import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.FileUtils;

public class CombatReportIOJson 
{
    public final static String COMBAT_REPORT_SUFFIX = ".CombatReport.json";

	public static void writeJson(Campaign campaign, CombatReport combatReport) throws PWCGException
	{
        String combatReportPath = buildCombatReportPath(campaign, combatReport.getCrewMemberSerialNumber());
        FileUtils.createDirIfNeeded(combatReportPath);
        
        PwcgJsonWriter<CombatReport> jsonWriter = new PwcgJsonWriter<>();
        jsonWriter.writeAsJson(combatReport, combatReportPath, DateUtils.getDateStringYYYYMMDD(combatReport.getDate()) + COMBAT_REPORT_SUFFIX);
	}

	public static Map<String, CombatReport> readJson(Campaign campaign, Integer crewMemberSerialNumber) throws PWCGException
	{
	    Map<String, CombatReport> combatReportsForCampaign = new TreeMap<>();
        String combatReportPath = buildCombatReportPath(campaign, crewMemberSerialNumber);
	    List<File> combatReportFiles = FileUtils.getFilesWithFilter(combatReportPath, COMBAT_REPORT_SUFFIX);
		for (File combatReportFile : combatReportFiles)
		{
			JsonObjectReader<CombatReport> jsoReader = new JsonObjectReader<>(CombatReport.class);
			CombatReport combatReport = jsoReader.readJsonFile(combatReportPath, combatReportFile.getName()); 
			convertV5ToV6(crewMemberSerialNumber, combatReport);			
			combatReportsForCampaign.put(DateUtils.getDateStringYYYYMMDD(combatReport.getDate()), combatReport);
		}
		
		return combatReportsForCampaign;
	}

    private static void convertV5ToV6(Integer crewMemberSerialNumber, CombatReport combatReport)
    {
        if (combatReport.getCrewMemberSerialNumber() <= 0)
        {
            combatReport.setCrewMemberSerialNumber(crewMemberSerialNumber);
        }
    }

    public static String buildCombatReportPath(Campaign campaign, Integer crewMemberSerialNumber)
    {
        String combatReportPath = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\CombatReports\\" + crewMemberSerialNumber + "\\";
        return combatReportPath;
    }
}
