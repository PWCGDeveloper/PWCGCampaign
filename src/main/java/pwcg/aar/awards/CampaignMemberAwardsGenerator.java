package pwcg.aar.awards;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.campaign.Campaign;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public abstract class CampaignMemberAwardsGenerator
{
    protected Campaign campaign;
    protected AARContext aarContext;
    protected AARPersonnelAwards personnelAwards = new AARPersonnelAwards();

    abstract public AARPersonnelAwards createCampaignMemberAwards() throws PWCGException;
    
    public CampaignMemberAwardsGenerator(Campaign campaign, AARContext aarContexts)
    {
        this.campaign = campaign;
        this.aarContext = aarContexts;
    }

    protected void missionsFlown(SquadronMember squadronMember) throws PWCGException 
    {        
        int updatedMissionsFlown = MissionsFlownCalculator.calculateMissionsFlown(campaign, squadronMember);
        personnelAwards.getMissionsFlown().put(squadronMember.getSerialNumber(), updatedMissionsFlown);
    }
 
    protected void promotions(SquadronMember squadronMember) throws PWCGException 
    {
        PromotionEventHandler promotionHandler = new PromotionEventHandler(campaign);
        String promotion = promotionHandler.promoteNonHistoricalPilots(squadronMember);
        if (!promotion.equals(PromotionEventHandler.NO_PROMOTION))
        {
            personnelAwards.addPromotion(squadronMember.getSerialNumber(), promotion);
        }
    }

    protected void medals(SquadronMember squadronMember, int victoriesInMissionForPilot) throws PWCGException 
    {
        MedalEventHandler medalHandler = new MedalEventHandler(campaign);
        medalHandler.awardMedals(squadronMember, victoriesInMissionForPilot);
        woundMedals(squadronMember);
        
        assignMedals(squadronMember, medalHandler);
    }
    
    protected void woundMedals(SquadronMember squadronMember) throws PWCGException 
    {
        int oddsWoundedRoll = RandomNumberGenerator.getRandom(100);
        if (oddsWoundedRoll < 1)
        {
            awardWoundMedal(squadronMember);
        }
    }

    protected void awardWoundMedal(SquadronMember squadronMember) throws PWCGException
	{
		MedalEventHandler medalHandler = new MedalEventHandler(campaign);
		medalHandler.awardWoundMedals(squadronMember);
		assignMedals(squadronMember, medalHandler);
	}

    protected void assignMedals(SquadronMember squadronMember, MedalEventHandler medalHandler)
	{
		for (Medal medal : medalHandler.getMedalAwardsForSquadronMember(squadronMember.getSerialNumber()))
        {
		    personnelAwards.addMedal(squadronMember.getSerialNumber(), medal);
        }
	}

}
