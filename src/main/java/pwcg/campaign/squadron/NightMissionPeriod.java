package pwcg.campaign.squadron;

import java.util.Date;

public class NightMissionPeriod
{
    private Date startDate;
    private Date endDate;
    private Integer nightMissionOdds;

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

    public Integer getNightMissionOdds()
    {
        return nightMissionOdds;
    }

    public void setNightMissionOdds(Integer nightMissionOdds)
    {
        this.nightMissionOdds = nightMissionOdds;
    }
}
