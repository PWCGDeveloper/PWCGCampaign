package pwcg.aar.inmission.phase1.parse;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.LogFileHeaderParser;
import pwcg.core.logfiles.LogFileSet;
import pwcg.core.logfiles.LogParser;

public class AARMissionFileLogResultMatcher
{
    private Campaign campaign;
    private LogFileHeaderParser aarHeaderParser;        
    private LogParser logParser;

    public AARMissionFileLogResultMatcher(Campaign campaign, LogFileHeaderParser aarHeaderParser, LogParser logParser)
    {
        this.campaign = campaign;
        this.aarHeaderParser = aarHeaderParser;
        this.logParser = logParser;
    }

    public LogFileSet matchMissionFileAndLogFile(PwcgMissionData pwcgMissionData, List<String> sortedLogSets) throws PWCGException 
    {
        List<String> validLogSets = findValidLogSets(sortedLogSets);

    	for (String logFileName : validLogSets)
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

    private List<String> findValidLogSets(List<String> sortedLogSets) throws PWCGException
    {
        List<String> validLogSets = new ArrayList<>();
        for (String logFileSetName : sortedLogSets)
        {
            LogEventData logEventData = logParser.parseLogFilesForMission(campaign, logFileSetName);
            if (logEventData.isValid())
            {
                validLogSets.add(logFileSetName);
            }
        }
        return validLogSets;
    }

    private LogFileSet makeMissionLogFileSet(String logFileName, PwcgMissionData pwcgMissionData) throws PWCGException
    {
        LogFileSet logFileMissionFile = new LogFileSet();
        logFileMissionFile.setLogFileName(logFileName);                    
        return logFileMissionFile;
    }
}
