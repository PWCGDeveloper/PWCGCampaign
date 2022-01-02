package pwcg.aar.outofmission.phase2.awards;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.promotion.PromotionArbitrator;
import pwcg.campaign.promotion.PromotionEventHandlerFactory;
import pwcg.core.exception.PWCGException;

public class PromotionEventHandler 
{
	public static String promoteNonHistoricalCrewMembers(Campaign campaign, CrewMember crewMember) throws PWCGException 
	{
	    String promotion = PromotionArbitrator.NO_PROMOTION;
        if (crewMember instanceof TankAce)
        {
            return promotion;
        }
        
        IPromotionEventHandler promotionHandler = PromotionEventHandlerFactory.getPromotionEventHandler(campaign, crewMember);
        if (promotionHandler != null)
        {
            promotion = promotionHandler.determinePromotion(campaign, crewMember);
        }
		
		return promotion;
	}
}
