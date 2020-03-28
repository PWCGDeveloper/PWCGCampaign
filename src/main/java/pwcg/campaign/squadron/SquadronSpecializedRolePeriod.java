package pwcg.campaign.squadron;

import java.util.Date;

import pwcg.campaign.plane.SpecializedRole;

public class SquadronSpecializedRolePeriod
{
    private Date startDate;
    private Date endDate;
    private SpecializedRole specializedRole;

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

    public SpecializedRole getSpecializedRole()
    {
        return specializedRole;
    }

    public void setSpecializedRole(SpecializedRole specializedRole)
    {
        this.specializedRole = specializedRole;
    }
}
