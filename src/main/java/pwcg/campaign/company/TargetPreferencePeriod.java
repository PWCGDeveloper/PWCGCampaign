package pwcg.campaign.company;

import java.util.Date;

import pwcg.mission.target.TargetType;

public class TargetPreferencePeriod
{
    private Date startDate;
    private Date endDate;
    private TargetType targetType;
    private Integer targetPreferenceOdds;

    public Date getStartDate()
    {
        return startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public TargetType getTargetType()
    {
        return targetType;
    }

    public Integer getTargetPreferenceOdds()
    {
        return targetPreferenceOdds;
    }
}
