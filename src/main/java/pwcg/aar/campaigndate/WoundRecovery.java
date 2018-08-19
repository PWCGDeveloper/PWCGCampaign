package pwcg.aar.campaigndate;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMemberStatus;
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
    
    public Date calcDateOfRecovery(int pilotStatus) throws PWCGException 
    {
        int daysForWounds = calculateDaysForWounds(pilotStatus);        
        Date newDateAfterWounds = DateUtils.advanceTimeDays(campaign.getDate(), daysForWounds);
        return newDateAfterWounds;
    }    

    public int calculateDaysForWounds(int pilotStatus) throws PWCGException 
    {
        int daysForWound = 0;
        if (pilotStatus == SquadronMemberStatus.STATUS_WOUNDED)
        {
            int randomAdditional = RandomNumberGenerator.getRandom(20);
            daysForWound = 7 + randomAdditional;
        }
        
        if (pilotStatus == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            int randomAdditional = RandomNumberGenerator.getRandom(60);
            daysForWound = 30 + randomAdditional;
        }
        
        return daysForWound;
    }
}