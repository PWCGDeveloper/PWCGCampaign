package pwcg.aar.awards;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.plane.Role;
import pwcg.campaign.plane.RoleCategory;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.country.BoSServiceManager;

public class PromotionEventHandlerFactory 
{
	public static IPromotionEventHandler getPromotionEventHandler(Campaign campaign, SquadronMember squadronMember) throws PWCGException 
	{
        if (squadronMember instanceof Ace)
        {
            return null;
        }
        
        Role squadronPrimaryRole = squadronMember.determineSquadron().determineSquadronPrimaryRole(campaign.getDate());
        if (squadronPrimaryRole.isRoleCategory(RoleCategory.FIGHTER))
		{
            ArmedService service =squadronMember.determineService(campaign.getDate());
            if (service.getServiceId() == BoSServiceManager.LUFTWAFFE)
            {
                return new LuftwaffePromotionEventHandlerFighter();
            }
            else
            {
                return new DefaultPromotionEventHandlerFighter();
            }
		}
        else if (squadronPrimaryRole.isRoleCategory(RoleCategory.BOMBER) || squadronPrimaryRole.isRoleCategory(RoleCategory.TRANSPORT))
        {
            return new PromotionEventHandlerBomb();
        }
        else
        {
            return new PromotionEventHandlerRecon();
        }
	}


}
