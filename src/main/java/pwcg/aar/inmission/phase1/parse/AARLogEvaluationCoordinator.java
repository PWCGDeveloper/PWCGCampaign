package pwcg.aar.inmission.phase1.parse;

import pwcg.core.exception.PWCGException;

public class AARLogEvaluationCoordinator
{
    private AARMissionLogRawData missionLogRawData = new AARMissionLogRawData(); 
 
    public AARLogEvaluationCoordinator()
    {
    }
    
    
    public AARMissionLogRawData performAARPhase1Parse(AARMissionLogFileSet missionLogFileSet) throws PWCGException
    {
        IAARLogParser aarLogParser = new AARLogParser(missionLogFileSet);;
        AARLogEventData logEventData = aarLogParser.parseLogFilesForMission();
        missionLogRawData.setLogEventData(logEventData);
        return missionLogRawData;
    }
}
