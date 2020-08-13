package pwcg.aar.inmission.phase1.parse;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARLogSetValidator
{

    public static boolean isLogSetValid(Campaign campaign, AARMissionLogFileSet logFileMissionFileSet) throws PWCGException
    {
        AARLogEvaluationCoordinator logEvaluationCoordinator = new AARLogEvaluationCoordinator();
        AARMissionLogRawData missionLogRawData = logEvaluationCoordinator.performAARPhase1Parse(campaign, logFileMissionFileSet);
        AARLogEventData logEventData = missionLogRawData.getLogEventData();
        if (logEventData.getVehicles().isEmpty())
        {
            return false;
        }

        return true;
    }

}
