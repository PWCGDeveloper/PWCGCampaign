package pwcg.aar.campaigndate;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;

public class WoundRecovery
{
    protected Campaign campaign;

    public WoundRecovery(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public Date calcDateOfRecovery(int crewMemberStatus) throws PWCGException 
    {
        int daysForWounds = calculateDaysForWounds(crewMemberStatus);        
        Date newDateAfterWounds = DateUtils.advanceTimeDays(campaign.getDate(), daysForWounds);
        return newDateAfterWounds;
    }    

    public int calculateDaysForWounds(int crewMemberStatus) throws PWCGException 
    {
        int daysForWound = 0;
        if (crewMemberStatus == CrewMemberStatus.STATUS_WOUNDED)
        {
            int randomAdditional = RandomNumberGenerator.getRandom(20);
            daysForWound = 4 + randomAdditional;
        }
        
        if (crewMemberStatus == CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            int randomAdditional = RandomNumberGenerator.getRandom(60);
            daysForWound = 25 + randomAdditional;
        }
        
        return daysForWound;
    }
}