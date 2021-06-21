package pwcg.aar.outofmission.phase2.awards;

import pwcg.aar.AARFactory;
import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.campaign.Campaign;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.SquadronMember;
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

    public AARPersonnelAwards generateAwards(SquadronMember squadronMember, int victoriesInMissionForPilot) throws PWCGException 
    {
        promotions(squadronMember);
        medals(squadronMember, victoriesInMissionForPilot);
        return personnelAwards;
    }
    
    private void promotions(SquadronMember squadronMember) throws PWCGException 
    {
        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);
        if (!promotion.equals(PromotionEventHandler.NO_PROMOTION))
        {
            personnelAwards.addPromotion(squadronMember.getSerialNumber(), promotion);
        }
    }

    private void medals(SquadronMember squadronMember, int victoriesInMissionForPilot) throws PWCGException 
    {
        MedalEventHandler medalHandler = new MedalEventHandler(campaign);
        medalHandler.awardMedals(squadronMember, victoriesInMissionForPilot);
        awardWoundMedalIfWounded(squadronMember);
        assignMedals(squadronMember, medalHandler);
    }

    private void awardWoundMedalIfWounded(SquadronMember squadronMember) throws PWCGException
	{
        boolean pilotWounded = aarContext.getPersonnelLosses().pilotisWoundedToday(squadronMember);
        if (pilotWounded)
        {
            MedalEventHandler medalHandler = new MedalEventHandler(campaign);
            medalHandler.awardWoundMedals(squadronMember);
            assignMedals(squadronMember, medalHandler);
        }
	}

    private void assignMedals(SquadronMember squadronMember, MedalEventHandler medalHandler)
	{
		for (Medal medal : medalHandler.getMedalAwardsForSquadronMember(squadronMember.getSerialNumber()))
        {
		    personnelAwards.addMedal(squadronMember.getSerialNumber(), medal);
        }
	}
}
