package pwcg.aar.campaigndate;

import java.util.Date;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SerialNumber.SerialNumberClassification;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class AARTimePassedAfterWounds
{
    protected Campaign campaign;

    public AARTimePassedAfterWounds(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public Date calcDateOfRecovery(LogPilot playerCrewMember) throws PWCGException 
    {
        int daysForWounds = calculateDaysForWounds(playerCrewMember);        
        Date newDateAfterWounds = DateUtils.advanceTimeDays(campaign.getDate(), daysForWounds);
        return newDateAfterWounds;
    }

    private int calculateDaysForWounds(LogPilot playerCrewMember) throws PWCGException
    {
        int daysForWounds = 0;
        CampaignDaysPassed campaignDaysPassed = new CampaignDaysPassed(campaign);
        if (SerialNumber.getSerialNumberClassification(playerCrewMember.getSerialNumber()) == SerialNumberClassification.PLAYER)
        {
            daysForWounds = campaignDaysPassed.calcDaysForWound(playerCrewMember.getStatus());
        }
        
        return daysForWounds;
    }
}