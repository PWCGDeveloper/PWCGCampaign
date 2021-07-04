package pwcg.aar.inmission.phase1.parse;

import java.util.List;

import pwcg.aar.prelim.AARHeaderParser;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARMissionFileLogResultMatcher
{
    private Campaign campaign;
    private AARHeaderParser aarHeaderParser;        

    public AARMissionFileLogResultMatcher(Campaign campaign, AARHeaderParser aarHeaderParser)
    {
        this.campaign = campaign;
        this.aarHeaderParser = aarHeaderParser;
    }

    public AARMissionLogFileSet matchMissionFileAndLogFile(PwcgMissionData pwcgMissionData, List<String> sortedLogSets) throws PWCGException 
    {
    	for (String logFileName : sortedLogSets)
    	{
            String missionFileNameFromLogs = aarHeaderParser.parseHeaderOnly(campaign.getCampaignData().getName(), logFileName);
            String missionFileNameFromPwcg = pwcgMissionData.getMissionHeader().getMissionFileName();
            if (missionFileNameFromPwcg.toLowerCase().equalsIgnoreCase(missionFileNameFromLogs.toLowerCase()))
            {
                AARMissionLogFileSet logFileMissionFileSet = makeMissionLogFileSet(logFileName, pwcgMissionData);
                return logFileMissionFileSet;
            }
    	}
        
        throw new PWCGException("Unable to find matching logs set for " + pwcgMissionData.getMissionHeader().getMissionFileName());
    }

    private AARMissionLogFileSet makeMissionLogFileSet(String logFileName, PwcgMissionData pwcgMissionData) throws PWCGException
    {
        AARMissionLogFileSet logFileMissionFile = new AARMissionLogFileSet();
        logFileMissionFile.setLogFileName(logFileName);                    
        return logFileMissionFile;
    }
}
