package pwcg.aar.inmission.phase1.parse;

import java.util.List;

import pwcg.aar.prelim.AARHeaderParser;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

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
            missionFileNameFromLogs = FileUtils.stripFileExtension(missionFileNameFromLogs);
            String missionFileNameFromPwcg = pwcgMissionData.getMissionHeader().getMissionFileName();
            if (missionFileNameFromPwcg.toLowerCase().equalsIgnoreCase(missionFileNameFromLogs.toLowerCase()))
            {
                AARMissionLogFileSet logFileMissionFileSet = makeMissionLogFileSet(logFileName, pwcgMissionData);
                return logFileMissionFileSet;
            }
    	}
        
        return null;
    }

    private AARMissionLogFileSet makeMissionLogFileSet(String logFileName, PwcgMissionData pwcgMissionData) throws PWCGException
    {
        AARMissionLogFileSet logFileMissionFile = new AARMissionLogFileSet();
        logFileMissionFile.setLogFileName(logFileName);                    
        return logFileMissionFile;
    }
}
