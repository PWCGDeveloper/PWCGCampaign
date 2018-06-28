package pwcg.aar.awards;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class PromotionEventHandler 
{
	private Campaign campaign = null;

	public static String NO_PROMOTION = "No Promotion";
	
	public PromotionEventHandler (Campaign campaign) 
	{
		this.campaign = campaign;
	}

	public String promoteNonHistoricalPilots(SquadronMember squadronMember) throws PWCGException 
	{
	    String promotion = NO_PROMOTION;
        if (squadronMember instanceof Ace)
        {
            return promotion;
        }
        
        Role primaryRole = squadronMember.determineSquadron().determineSquadronPrimaryRole(campaign.getDate());
		if (primaryRole == Role.ROLE_FIGHTER)
		{
            PromotionEventHandlerFighter fighterPromotions = new PromotionEventHandlerFighter();
            promotion = fighterPromotions.determineScoutPromotion(campaign, squadronMember);
		}
		else if (primaryRole == Role.ROLE_STRAT_BOMB)
		{
		    PromotionEventHandlerStrategic strategicPromotions = new PromotionEventHandlerStrategic();
		    promotion = strategicPromotions.determineStrategicPromotion(campaign, squadronMember);
		}
        else
        {
            PromotionEventHandlerRecon reconPromotions = new PromotionEventHandlerRecon();
            promotion = reconPromotions.determineReconPromotion(campaign, squadronMember);
        }
		
		return promotion;
	}


}
