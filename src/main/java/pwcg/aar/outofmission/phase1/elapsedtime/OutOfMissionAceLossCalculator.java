package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMemberStatus;
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

    public Map<Integer, Ace> acesKilledHistorically() throws PWCGException
    {
        PWCGContextManager.getInstance().getAceManager().loadFromHistoricalAces(aarContext.getNewDate());

        Map<Integer, Ace> acesKilled = new HashMap<>();
        List<Ace> deadAces = PWCGContextManager.getInstance().getAceManager().acesKilledHistoricallyInTimePeriod(campaign.getDate(), aarContext.getNewDate());

        for (Ace deadAce : deadAces)
        {
            Ace deadAceThisCampaign = PWCGContextManager.getInstance().getAceManager().getAceWithCampaignAdjustment(
                    campaign, campaign.getPersonnelManager().getCampaignAces(), deadAce.getSerialNumber(), campaign.getDate());

            if (deadAceThisCampaign != null)
            {
                if (deadAceThisCampaign.getPilotActiveStatus() <= SquadronMemberStatus.STATUS_CAPTURED)
                {
                    continue;
                }
    
                acesKilled.put(deadAce.getSerialNumber(), deadAce);
            }
        }
        
        return acesKilled;
    }
}
