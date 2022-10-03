package pwcg.campaign.context;

import java.util.Date;

import pwcg.campaign.api.Side;

public class RadarPeriod
{
    private Date startDate;
    private Date endDate;
    private Side side;

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public Side getSide()
    {
        return side;
    }

    public void setSide(Side side)
    {
        this.side = side;
    }

}
