package pwcg.campaign.squadron;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.plane.SpecializedRole;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class SquadronSpecializedRoleSet
{
    private List<SquadronSpecializedRolePeriod> squadronSpecializedRolePeriods = new ArrayList<>();

    public SpecializedRole selectSpecializedRoleForMission(Date date) throws PWCGException 
    {
        for (SquadronSpecializedRolePeriod specializedRolePeriod : squadronSpecializedRolePeriods)
        {
            if (DateUtils.isDateInRange(date, specializedRolePeriod.getStartDate(), specializedRolePeriod.getEndDate()))
            {
                return specializedRolePeriod.getSpecializedRole();
            }
        }
        
        return SpecializedRole.SPECIALIZED_ROLE_NONE;
    }
}
