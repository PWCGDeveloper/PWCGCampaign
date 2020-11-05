package pwcg.aar.inmission.phase1.parse;

import pwcg.campaign.Campaign;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;

public class AARLogParser implements IAARLogParser 
{
    public static String UNKNOWN_MISSION_LOG_ENTITY = "-1";

    private AARMissionLogFileSet aarLogFileMissionFile;

    public AARLogParser(AARMissionLogFileSet aarLogFileMissionFile)
    {
        this.aarLogFileMissionFile = aarLogFileMissionFile;
    }

    @Override
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
        if (TestDriver.getInstance().isEnabled())
        {
            if (!TestDriver.getInstance().isDebugAARLogs())
            {
                AAREventAnalyzer analyzer = new AAREventAnalyzer(logEventData);
                analyzer.analyze();
            }
        }
    }
}

