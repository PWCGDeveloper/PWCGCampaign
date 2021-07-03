package pwcg.campaign.promotion;

import pwcg.aar.outofmission.phase2.awards.IPromotionEventHandler;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class PromotionEventHandlerFactory 
{
	public static IPromotionEventHandler getPromotionEventHandler(Campaign campaign, SquadronMember squadronMember) throws PWCGException 
	{
        if (squadronMember instanceof Ace)
        {
            return null;
        }
        
        PromotionMinimumCriteria promotionMinimumCriteria = new PromotionMinimumCriteria();
        promotionMinimumCriteria.setMinimumPromotionStandards(squadronMember, campaign.getDate());
        return new PromotionArbitrator(promotionMinimumCriteria);
	}
}
