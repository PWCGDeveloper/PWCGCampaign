package pwcg.campaign.personnel;

import java.util.Date;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;

public class BeforeCampaignDateFinder
{

    public static Date useEarliestPossibleDate(Date initialDate) throws PWCGException
    {
        Date earliestUsableDate = initialDate;
        if (initialDate.before(PWCGContextManager.getInstance().getEarliestPwcgDate()))
        {
            earliestUsableDate = PWCGContextManager.getInstance().getEarliestPwcgDate();
        }
        return earliestUsableDate;
    }

}
