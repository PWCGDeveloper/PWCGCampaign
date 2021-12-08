package pwcg.aar.inmission.phase1.parse;

import java.util.List;

import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogFileSet;
import pwcg.core.logfiles.LogFileHeaderParser;

public class AARMissionFileLogResultMatcher
{
    private Campaign campaign;
    private LogFileHeaderParser aarHeaderParser;        

    public AARMissionFileLogResultMatcher(Campaign campaign, LogFileHeaderParser aarHeaderParser)
    {
        this.campaign = campaign;
        this.aarHeaderParser = aarHeaderParser;
    }

    public LogFileSet matchMissionFileAndLogFile(PwcgMissionData pwcgMissionData, List<String> sortedLogSets) throws PWCGException 
    {
    	for (String logFileName : sortedLogSets)
    	{
            String missionFileNameFromLogs = aarHeaderParser.parseHeaderOnly(campaign.getCampaignData().getName(), logFileName);
            String missionFileNameFromPwcg = pwcgMissionData.getMissionHeader().getMissionFileName();
            if (missionFileNameFromPwcg.toLowerCase().equalsIgnoreCase(missionFileNameFromLogs.toLowerCase()))
            {
                LogFileSet logFileMissionFileSet = makeMissionLogFileSet(logFileName, pwcgMissionData);
                return logFileMissionFileSet;
            }
    	}
        
        throw new PWCGException("Unable to find matching logs set for " + pwcgMissionData.getMissionHeader().getMissionFileName());
    }

    private LogFileSet makeMissionLogFileSet(String logFileName, PwcgMissionData pwcgMissionData) throws PWCGException
    {
        LogFileSet logFileMissionFile = new LogFileSet();
        logFileMissionFile.setLogFileName(logFileName);                    
        return logFileMissionFile;
    }
}
