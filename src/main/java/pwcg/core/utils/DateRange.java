package pwcg.core.utils;

import java.util.Date;

public class DateRange
{
    private Date startDate;
    private Date endDate;
    
    public DateRange(Date startDate, Date endDate)
    {
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    public DateRange()
    {
    }
    
    public boolean isInDateRange(Date date)
    {
        if (date.before(startDate))
        {
            return false;
        }
       if (date.after(endDate))
       {
           return false;
       }
       
       return true;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }
}
