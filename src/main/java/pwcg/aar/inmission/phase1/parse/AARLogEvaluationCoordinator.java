package pwcg.aar.inmission.phase1.parse;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARLogEvaluationCoordinator
{
    private AARMissionLogRawData missionLogRawData = new AARMissionLogRawData(); 
 
    public AARLogEvaluationCoordinator()
    {
    }
    
    
    public AARMissionLogRawData performAARPhase1Parse(Campaign campaign, AARMissionLogFileSet missionLogFileSet) throws PWCGException
    {
        IAARLogParser aarLogParser = new AARLogParser(missionLogFileSet);;
        AARLogEventData logEventData = aarLogParser.parseLogFilesForMission(campaign);
        missionLogRawData.setLogEventData(logEventData);
        return missionLogRawData;
    }
}
