package pwcg.aar.inmission.phase1.parse;

import java.io.File;
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

    public AARMissionLogFileSet matchMissionFileAndLogFile(PwcgMissionData pwcgMissionData, List<File> sortedLogSets) throws PWCGException 
    {
    	for (File logFile : sortedLogSets)
    	{
            String missionFileNameFromLogs = aarHeaderParser.parseHeaderOnly(campaign.getCampaignData().getName(), logFile);
            String missionFileNameFromPwcg = pwcgMissionData.getMissionHeader().getMissionFileName();
            if (missionFileNameFromPwcg.toLowerCase().equalsIgnoreCase(missionFileNameFromLogs.toLowerCase()))
            {
                AARMissionLogFileSet logFileMissionFileSet = makeMissionLogFileSet(logFile, pwcgMissionData);
                if (AARLogSetValidator.isLogSetValid(campaign, logFileMissionFileSet))
                {
                    return logFileMissionFileSet;
                }
            }
    	}
        
        return null;
    }

    private AARMissionLogFileSet makeMissionLogFileSet(File logFile, PwcgMissionData pwcgMissionData) throws PWCGException
    {
        AARMissionLogFileSet logFileMissionFile = new AARMissionLogFileSet();
        logFileMissionFile.setLogFileName(FileUtils.stripFileExtension(logFile.getName()));                    
        return logFileMissionFile;
    }
}
