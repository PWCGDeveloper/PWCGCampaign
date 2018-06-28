package pwcg.campaign.squadron;

import java.util.Date;
import java.util.List;

public class SquadronRolePeriod
{
    private Date startDate;
    private Date endDate;
    private List<SquadronRoleWeight> weightedRoles;

    public List<SquadronRoleWeight> getWeightedRoles()
    {
        return weightedRoles;
    }

    public void setWeightedRoles(List<SquadronRoleWeight> weightedRoles)
    {
        this.weightedRoles = weightedRoles;
    }

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
}
