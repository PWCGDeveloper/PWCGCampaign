package pwcg.aar.awards;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class PromotionEventHandler 
{
	public static String NO_PROMOTION = "No Promotion";

	public static String promoteNonHistoricalPilots(Campaign campaign, SquadronMember squadronMember) throws PWCGException 
	{
	    String promotion = NO_PROMOTION;
        if (squadronMember instanceof Ace)
        {
            return promotion;
        }
        
        IPromotionEventHandler promotionHandler = PromotionEventHandlerFactory.getPromotionEventHandler(campaign, squadronMember);
        if (promotionHandler != null)
        {
            promotion = promotionHandler.determinePromotion(campaign, squadronMember);
        }
		
		return promotion;
	}
}
