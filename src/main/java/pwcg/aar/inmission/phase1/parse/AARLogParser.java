package pwcg.aar.inmission.phase1.parse;

import pwcg.campaign.Campaign;
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
        return logLineParser.parseLogLinesForMission(logReader.readLogFilesForMission(campaign));
    }
}

