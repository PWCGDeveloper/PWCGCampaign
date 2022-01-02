package pwcg.campaign.promotion;

import pwcg.aar.outofmission.phase2.awards.IPromotionEventHandler;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.TankAce;
import pwcg.core.exception.PWCGException;

public class PromotionEventHandlerFactory 
{
	public static IPromotionEventHandler getPromotionEventHandler(Campaign campaign, CrewMember crewMember) throws PWCGException 
	{
        if (crewMember instanceof TankAce)
        {
            return null;
        }
        
        PromotionMinimumCriteria promotionMinimumCriteria = new PromotionMinimumCriteria();
        promotionMinimumCriteria.setMinimumPromotionStandards(crewMember, campaign.getDate());
        return new PromotionArbitrator(promotionMinimumCriteria);
	}
}
