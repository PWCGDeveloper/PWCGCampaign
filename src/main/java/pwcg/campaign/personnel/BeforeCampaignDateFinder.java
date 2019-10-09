package pwcg.campaign.personnel;

import java.util.Date;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;

public class BeforeCampaignDateFinder
{

    public static Date useEarliestPossibleDate(Date initialDate) throws PWCGException
    {
        Date earliestUsableDate = initialDate;
        if (initialDate.before(PWCGContext.getInstance().getEarliestPwcgDate()))
        {
            earliestUsableDate = PWCGContext.getInstance().getEarliestPwcgDate();
        }
        return earliestUsableDate;
    }

}
