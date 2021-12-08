package pwcg.core.logfiles;

import java.util.List;

import pwcg.aar.inmission.phase1.parse.AAREventAnalyzer;
import pwcg.campaign.Campaign;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;

public class LogParser 
{
    public static String UNKNOWN_MISSION_LOG_ENTITY = "-1";

    public LogEventData parseLogFilesForMission(Campaign campaign, String logFileName) throws PWCGException 
    {
        LogReader logReader = new LogReader();
        LogLineParser logLineParser = new LogLineParser();
        List<String> logFileLines = logReader.readLogFilesForMission(campaign, logFileName);
        LogEventData logEventData = logLineParser.parseLogLinesForMission(logFileLines);
        
        debugLogData(logEventData);
        
        return logEventData;
    }

    private void debugLogData(LogEventData logEventData)
    {
        if (TestDriver.getInstance().isDebugAARLogs())
        {
            AAREventAnalyzer analyzer = new AAREventAnalyzer(logEventData);
            analyzer.analyze();
        }
    }
}

