package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.TankAce;
import pwcg.core.exception.PWCGException;

public class OutOfMissionAceLossCalculator
{
    private Campaign campaign;
    private AARContext aarContext;

    public OutOfMissionAceLossCalculator(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }

    public List<CrewMember> acesKilledHistorically() throws PWCGException
    {
        PWCGContext.getInstance().getAceManager().loadFromHistoricalAces(aarContext.getNewDate());

        Map<Integer, TankAce> acesKilled = new HashMap<>();
        List<TankAce> deadAces = PWCGContext.getInstance().getAceManager().acesKilledHistoricallyInTimePeriod(campaign.getDate(), aarContext.getNewDate());

        for (TankAce deadAce : deadAces)
        {
            TankAce deadAceThisCampaign = PWCGContext.getInstance().getAceManager().getAceWithCampaignAdjustment(
                    campaign, campaign.getPersonnelManager().getCampaignAces(), deadAce.getSerialNumber(), campaign.getDate());

            if (deadAceThisCampaign != null)
            {
                if (deadAceThisCampaign.getCrewMemberActiveStatus() <= CrewMemberStatus.STATUS_CAPTURED)
                {
                    continue;
                }
    
                acesKilled.put(deadAce.getSerialNumber(), deadAce);
            }
        }
        
        return new ArrayList<CrewMember>(acesKilled.values());
    }
}
