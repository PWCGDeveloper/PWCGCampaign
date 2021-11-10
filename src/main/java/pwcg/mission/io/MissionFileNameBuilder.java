package pwcg.mission.io;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class MissionFileNameBuilder
{
    public static String buildMissionFileName(Campaign campaign) 
    {
        String dateStr = DateUtils.getDateStringDashDelimitedYYYYMMDD(campaign.getDate());
        String filename = campaign.getName() + " " + dateStr;
        
        return filename;
    }
    
    public static Date getDateFromMissionFileName(String filename) throws PWCGException
    {
        if (filename.contains(" 19"))
        {
            int dateStartIndex = filename.lastIndexOf(" 19");
            String dateString = filename.substring(dateStartIndex);
            dateString = dateString.substring(0, 11);
            Date date = DateUtils.getDateDashDelimitedYYYYMMDD(dateString);
            return date;
        }
        
        return null;
    }
    
    public static String getCampaignNameFromMissionFileName(String filename) throws PWCGException
    {
        if (filename.contains(" 19"))
        {
            int dateStartIndex = filename.lastIndexOf(" 19");
            String campaignNameString = filename.substring(0, dateStartIndex);
            return campaignNameString.trim();
        }
        
        return "";
    }

}
