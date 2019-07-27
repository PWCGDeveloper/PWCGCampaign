package pwcg.aar.prelim;

import java.util.Date;
import java.util.List;

import pwcg.aar.inmission.phase1.parse.AARMissionFileLogResultMatcher;
import pwcg.aar.inmission.phase1.parse.AARMissionLogFileSet;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class AARMostRecentLogSetFinder
{
    private AARMissionFileLogResultMatcher matcher;
    private AARMissionLogFileSet aarLogFileMissionFile;
    private PwcgMissionData pwcgMissionData;
    private Campaign campaign;

    public AARMostRecentLogSetFinder(Campaign campaign, AARMissionFileLogResultMatcher matcher)
    {
        this.campaign = campaign;
        this.matcher = matcher;
    }

    public void getMostRecentAARLogFileMissionDataSetForCampaign(List<String> sortedLogSets, List<PwcgMissionData> sortedPwcgMissionDataForCampaign) throws PWCGException
    {        
        pwcgMissionData = getMissionDataForCampaignDate(sortedPwcgMissionDataForCampaign);
        aarLogFileMissionFile = matcher.matchMissionFileAndLogFile(pwcgMissionData, sortedLogSets);
    }
    
    private PwcgMissionData getMissionDataForCampaignDate(List<PwcgMissionData> sortedPwcgMissionDataForCampaign) throws PWCGException
    {
        for (PwcgMissionData missionData : sortedPwcgMissionDataForCampaign)
        {
            Date missionDataDate = DateUtils.getDateYYYYMMDD(missionData.getMissionHeader().getDate());
            if (missionDataDate.equals(campaign.getDate()))
            {
                return missionData;
            }
        }
        
        throw new PWCGException("Could not find mission data for campaign on date " + DateUtils.getDateStringYYYYMMDD(campaign.getDate()));
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
