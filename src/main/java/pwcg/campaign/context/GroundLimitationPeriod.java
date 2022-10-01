package pwcg.campaign.context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class GroundLimitationPeriod
{
    private Date startDate;
    private Date endDate;
    private List<PwcgMapGroundUnitLimitation> groundLimitations = new ArrayList<>();
    
    public GroundLimitationPeriod(Date startDate, Date endDate, List<PwcgMapGroundUnitLimitation> groundLimitations)
    {
        this.startDate = startDate;
        this.endDate = endDate;
        this.groundLimitations = groundLimitations;
    }

    public boolean isLimited(Date date, PwcgMapGroundUnitLimitation limitation) throws PWCGException
    {
        if (!DateUtils.isDateInRange(date, startDate, endDate))
        {
            return false;
        }
        
        for (PwcgMapGroundUnitLimitation groundLimitation : groundLimitations)
        {
            if (groundLimitation == limitation)
            {
                return true;
            }
        }

        return false;
    }
}
