package pwcg.campaign.company;

import java.util.Date;
import java.util.List;

public class SquadronRolePeriod
{
    private Date startDate;
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
}
