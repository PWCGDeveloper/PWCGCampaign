package pwcg.aar.outofmission.phase1.elapsedtime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.outofmission.DuringCampaignVictimGenerator;
import pwcg.campaign.outofmission.OutOfMissionVictoryGenerator;
import pwcg.campaign.personnel.EnemySquadronFinder;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignPersonnelTestHelper;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class OutOfMissionVictoryGeneratorTest
{
    private Campaign campaign;

    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.ARRAS_MAP);
        campaign = CampaignCache.makeCampaignForceCreation(SquadronTestProfile.ESC_103_PROFILE);
    }

    @Test
    public void testVictoryAwarded () throws PWCGException
    {     
        SquadronMember aiSquadMember = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Corporal");        
        Squadron squadronMemberSquadron = aiSquadMember.determineSquadron();

        EnemySquadronFinder enemySquadronFinder = new EnemySquadronFinder(campaign);
        Squadron victimSquadron = enemySquadronFinder.getRandomEnemyViableSquadron(squadronMemberSquadron, campaign.getDate());
        DuringCampaignVictimGenerator duringCampaignVictimGenerator = new DuringCampaignVictimGenerator(campaign, victimSquadron);
        
        OutOfMissionVictoryGenerator victoryGenerator = new OutOfMissionVictoryGenerator(campaign, victimSquadron, duringCampaignVictimGenerator, aiSquadMember);
        Victory victory = victoryGenerator.generateOutOfMissionVictory(campaign.getDate());
        
        assert (victory.getVictim().getAirOrGround() == Victory.AIR_VICTORY);
    }
}
