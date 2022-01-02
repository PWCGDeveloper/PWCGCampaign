package pwcg.aar.outofmission.phase2.awards;

import pwcg.aar.AARFactory;
import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.promotion.PromotionArbitrator;
import pwcg.core.exception.PWCGException;

public class CampaignMemberAwardsGenerator
{
    private Campaign campaign;
    private AARPersonnelAwards personnelAwards;
    private AARContext aarContext;

    public CampaignMemberAwardsGenerator(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
        this.personnelAwards = AARFactory.makeAARPersonnelAwards();
    }

    public AARPersonnelAwards generateAwards(CrewMember crewMember, int victoriesInMissionForCrewMember) throws PWCGException 
    {
        promotions(crewMember);
        medals(crewMember, victoriesInMissionForCrewMember);
        return personnelAwards;
    }
    
    private void promotions(CrewMember crewMember) throws PWCGException 
    {
        String promotion = PromotionEventHandler.promoteNonHistoricalCrewMembers(campaign, crewMember);
        if (!promotion.equals(PromotionArbitrator.NO_PROMOTION))
        {
            personnelAwards.addPromotion(crewMember.getSerialNumber(), promotion);
        }
    }

    private void medals(CrewMember crewMember, int victoriesInMissionForCrewMember) throws PWCGException 
    {
        MedalEventHandler medalHandler = new MedalEventHandler(campaign);
        medalHandler.awardMedals(crewMember, victoriesInMissionForCrewMember);
        awardWoundMedalIfWounded(crewMember);
        assignMedals(crewMember, medalHandler);
    }

    private void awardWoundMedalIfWounded(CrewMember crewMember) throws PWCGException
	{
        boolean crewMemberWounded = aarContext.getPersonnelLosses().crewMemberisWoundedToday(crewMember);
        if (crewMemberWounded)
        {
            MedalEventHandler medalHandler = new MedalEventHandler(campaign);
            medalHandler.awardWoundMedals(crewMember);
            assignMedals(crewMember, medalHandler);
        }
	}

    private void assignMedals(CrewMember crewMember, MedalEventHandler medalHandler)
	{
		for (Medal medal : medalHandler.getMedalAwardsForCrewMember(crewMember.getSerialNumber()))
        {
		    personnelAwards.addMedal(crewMember.getSerialNumber(), medal);
        }
	}
}
