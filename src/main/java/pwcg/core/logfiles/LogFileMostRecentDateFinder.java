package pwcg.core.logfiles;

import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.io.MissionFileNameBuilder;

public class LogFileMostRecentDateFinder
{
    private LogSetFinder logSetFinder;
    private Campaign campaign;

    public LogFileMostRecentDateFinder(Campaign campaign, LogSetFinder logSetFinder)
    {
        this.campaign = campaign;
        this.logSetFinder = logSetFinder;
    }

    public Date determineMostRecentAARLogFileMissionDataSetForCampaign() throws PWCGException
    {   
        Date mostRecentLogFileDate = null;
        List<String> sortedLogSets = logSetFinder.getSortedLogFileSets();
        LogFileHeaderParser logFileHeaderParser = new LogFileHeaderParser();
        for(String logFileName : sortedLogSets)
        {
            String missionFileNameFromLogs = logFileHeaderParser.parseHeaderOnly(campaign.getCampaignData().getName(), logFileName);
            if (missionFileNameFromLogs != null)
            {
                mostRecentLogFileDate = compareDates(mostRecentLogFileDate, missionFileNameFromLogs);
            }
        }
        
        return mostRecentLogFileDate;
    }

    public Date compareDates(Date mostRecentLogFileDate, String missionFileNameFromLogs) throws PWCGException
    {
        String campaignNameString = MissionFileNameBuilder.getCampaignNameFromMissionFileName(missionFileNameFromLogs);
        if (campaignNameString.equals(campaign.getName()))
        {
            Date logFileDate = MissionFileNameBuilder.getDateFromMissionFileName(missionFileNameFromLogs);
            if (logFileDate != null)
            {
                if (mostRecentLogFileDate != null)
                {
                    if (logFileDate.after(mostRecentLogFileDate))
                    {
                        mostRecentLogFileDate = logFileDate;
                    }
                }
                else
                {
                    mostRecentLogFileDate = logFileDate;
                }
            }
        }
        return mostRecentLogFileDate;
    }
}
