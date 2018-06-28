package pwcg.campaign.target;

import java.util.Date;

import pwcg.campaign.api.Side;

public class TargetPreference
{
    private Date startDate;
    private Date endDate;
    private Side targetSide;
    private TacticalTarget targetType;
    private int oddsOfUse;

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

    public Side getTargetSide()
    {
        return targetSide;
    }

    public void setTargetSide(Side targetSide)
    {
        this.targetSide = targetSide;
    }

    public TacticalTarget getTargetType()
    {
        return targetType;
    }

    public void setTargetType(TacticalTarget targetType)
    {
        this.targetType = targetType;
    }

    public int getOddsOfUse()
    {
        return oddsOfUse;
    }

    public void setOddsOfUse(int oddsOfUse)
    {
        this.oddsOfUse = oddsOfUse;
    }
}
