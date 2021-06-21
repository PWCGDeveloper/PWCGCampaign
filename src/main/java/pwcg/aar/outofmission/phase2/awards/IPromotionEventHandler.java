package pwcg.aar.outofmission.phase2.awards;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public interface IPromotionEventHandler
{
    public String determinePromotion(Campaign campaign, SquadronMember pilot) throws PWCGException;
}