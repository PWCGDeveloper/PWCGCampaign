package pwcg.aar.outofmission.phase2.awards;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;

public interface IPromotionEventHandler
{
    public String determinePromotion(Campaign campaign, CrewMember crewMember) throws PWCGException;
}