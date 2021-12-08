package pwcg.aar.inmission.phase1.parse;

import pwcg.campaign.Campaign;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.AARLogEventData;
import pwcg.core.logfiles.AARLogLineParser;
import pwcg.core.logfiles.AARMissionLogFileSet;

public class AARLogParser 
{
    public static String UNKNOWN_MISSION_LOG_ENTITY = "-1";

    private AARMissionLogFileSet aarLogFileMissionFile;

    public AARLogParser(AARMissionLogFileSet aarLogFileMissionFile)
    {
        this.aarLogFileMissionFile = aarLogFileMissionFile;
    }

    public AARLogEventData parseLogFilesForMission(Campaign campaign) throws PWCGException 
    {
        AARLogReader logReader = new AARLogReader(aarLogFileMissionFile);
        AARLogLineParser logLineParser = new AARLogLineParser();
        AARLogEventData logEventData = logLineParser.parseLogLinesForMission(logReader.readLogFilesForMission(campaign));
        
        debugLogData(logEventData);
        
        return logEventData;
    }

    private void debugLogData(AARLogEventData logEventData)
    {
        if (TestDriver.getInstance().isDebugAARLogs())
        {
            AAREventAnalyzer analyzer = new AAREventAnalyzer(logEventData);
            analyzer.analyze();
        }
    }
}

