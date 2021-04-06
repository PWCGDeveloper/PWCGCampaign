package pwcg.campaign;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.skirmish.SkirmishBuilder;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.MissionHumanParticipants;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

public class SkirmishBuilderTest
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }
    
    @Test
    public void testNoSkirmish() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
        PWCGContext.getInstance().setCampaign(campaign);

        List<Skirmish> skirmishes = PWCGContext.getInstance().getCurrentMap().getSkirmishManager().getSkirmishesForDate(campaign.getDate());
        assert (skirmishes.size() == 0);
    }

    @Test
    public void singlePlayerSkirmishInRangeTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_362_PROFILE);
        PWCGContext.getInstance().setCampaign(campaign);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19440917"));

        Skirmish skirmish =createMissionAtSkirmish(campaign);
        assert (skirmish != null);
    }

    @Test
    public void singlePlayerSkirmishTooFarTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_362_PROFILE);
        PWCGContext.getInstance().setCampaign(campaign);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19440928"));

        Skirmish skirmish =createMissionAtSkirmish(campaign);
        assert (skirmish == null);
    }

    private Skirmish createMissionAtSkirmish(Campaign campaign) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = makeParticipatingPlayers(campaign);
       
        SkirmishBuilder skirmishBuilder = new SkirmishBuilder(campaign, participatingPlayers);
        Skirmish skirmish = skirmishBuilder.chooseBestSkirmish();

        return skirmish;
    }

    private MissionHumanParticipants makeParticipatingPlayers(Campaign campaign) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        SquadronMember player = campaign.findReferencePlayer();
        participatingPlayers.addSquadronMember(player);
        return participatingPlayers;
    }


}
