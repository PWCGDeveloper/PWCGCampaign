package pwcg.aar.awards;

import pwcg.aar.AARFactory;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.campaign.Campaign;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class CampaignMemberAwardsGeneratorCommon
{
    private Campaign campaign;
    private AARPersonnelAwards personnelAwards;
    
    public CampaignMemberAwardsGeneratorCommon(Campaign campaign)
    {
        this.campaign = campaign;
        this.personnelAwards = AARFactory.makeAARPersonnelAwards();
    }
    
    public void missionsFlown(SquadronMember squadronMember) throws PWCGException 
    {        
        int updatedMissionsFlown = MissionsFlownCalculator.calculateMissionsFlown(campaign, squadronMember);
        personnelAwards.getMissionsFlown().put(squadronMember.getSerialNumber(), updatedMissionsFlown);
    }
 
    public void promotions(SquadronMember squadronMember) throws PWCGException 
    {
        String promotion = PromotionEventHandler.promoteNonHistoricalPilots(campaign, squadronMember);
        if (!promotion.equals(PromotionEventHandler.NO_PROMOTION))
        {
            personnelAwards.addPromotion(squadronMember.getSerialNumber(), promotion);
        }
    }

    public void medals(SquadronMember squadronMember, int victoriesInMissionForPilot) throws PWCGException 
    {
        MedalEventHandler medalHandler = new MedalEventHandler(campaign);
        medalHandler.awardMedals(squadronMember, victoriesInMissionForPilot);
        woundMedals(squadronMember);
        
        assignMedals(squadronMember, medalHandler);
    }

    public void awardWoundMedal(SquadronMember squadronMember) throws PWCGException
	{
		MedalEventHandler medalHandler = new MedalEventHandler(campaign);
		medalHandler.awardWoundMedals(squadronMember);
		assignMedals(squadronMember, medalHandler);
	}
    
    private void woundMedals(SquadronMember squadronMember) throws PWCGException 
    {
        int oddsWoundedRoll = RandomNumberGenerator.getRandom(100);
        if (oddsWoundedRoll < 1)
        {
            awardWoundMedal(squadronMember);
        }
    }

    private void assignMedals(SquadronMember squadronMember, MedalEventHandler medalHandler)
	{
		for (Medal medal : medalHandler.getMedalAwardsForSquadronMember(squadronMember.getSerialNumber()))
        {
		    personnelAwards.addMedal(squadronMember.getSerialNumber(), medal);
        }
	}

    public AARPersonnelAwards getPersonnelAwards()
    {
        return personnelAwards;
    }
}
