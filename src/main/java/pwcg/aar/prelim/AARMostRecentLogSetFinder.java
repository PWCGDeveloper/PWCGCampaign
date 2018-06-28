package pwcg.aar.prelim;

import java.util.List;

import pwcg.aar.inmission.phase1.parse.AARMissionFileLogResultMatcher;
import pwcg.aar.inmission.phase1.parse.AARMissionLogFileSet;
import pwcg.core.exception.PWCGException;

public class AARMostRecentLogSetFinder
{
    private AARMissionFileLogResultMatcher matcher;
    private AARMissionLogFileSet aarLogFileMissionFile;
    private PwcgMissionData pwcgMissionData;
    
    public AARMostRecentLogSetFinder(AARMissionFileLogResultMatcher matcher)
    {
    	this.matcher = matcher;
    }

    public void getMostRecentAARLogFileMissionDataSetForCampaign(List<String> sortedLogSets, List<PwcgMissionData> sortedPwcgMissionDataForCampaign) throws PWCGException
    {        
        aarLogFileMissionFile = matcher.matchMissionFileAndLogFile(sortedPwcgMissionDataForCampaign.get(0), sortedLogSets);
        pwcgMissionData = sortedPwcgMissionDataForCampaign.get(0);
    }

    public AARMissionLogFileSet getAarLogFileMissionFile()
    {
        return aarLogFileMissionFile;
    }

    public PwcgMissionData getPwcgMissionData()
    {
        return pwcgMissionData;
    }
}
