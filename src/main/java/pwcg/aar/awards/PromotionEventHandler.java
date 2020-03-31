package pwcg.aar.awards;

import pwcg.campaign.Campaign;
import pwcg.campaign.plane.Role;
import pwcg.campaign.plane.RoleCategory;
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
        
        Role squadronPrimaryRole = squadronMember.determineSquadron().determineSquadronPrimaryRole(campaign.getDate());
        if (squadronPrimaryRole.isRoleCategory(RoleCategory.FIGHTER))
		{
            PromotionEventHandlerFighter fighterPromotions = new PromotionEventHandlerFighter();
            promotion = fighterPromotions.determineScoutPromotion(campaign, squadronMember);
		}
        if (squadronPrimaryRole.isRoleCategory(RoleCategory.BOMBER) || squadronPrimaryRole.isRoleCategory(RoleCategory.TRANSPORT))
        {
            PromotionEventHandlerBomb tacticalBomberPromotions = new PromotionEventHandlerBomb();
            promotion = tacticalBomberPromotions.determineStrategicPromotion(campaign, squadronMember);
        }
        else
        {
            PromotionEventHandlerRecon reconPromotions = new PromotionEventHandlerRecon();
            promotion = reconPromotions.determineReconPromotion(campaign, squadronMember);
        }
		
		return promotion;
	}


}
