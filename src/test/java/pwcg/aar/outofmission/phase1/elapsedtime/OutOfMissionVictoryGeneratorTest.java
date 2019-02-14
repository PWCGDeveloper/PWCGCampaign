package pwcg.aar.outofmission.phase1.elapsedtime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.outofmission.DuringCampaignVictimGenerator;
import pwcg.campaign.outofmission.OutOfMissionVictoryGenerator;
import pwcg.campaign.personnel.EnemySquadronFinder;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheRoF;
import pwcg.testutils.CampaignPersonnelTestHelper;

@RunWith(MockitoJUnitRunner.class)
public class OutOfMissionVictoryGeneratorTest
{
    private Campaign campaign;

    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.FRANCE_MAP);
        campaign = CampaignCache.makeCampaign(CampaignCacheRoF.ESC_103_PROFILE);
    }

    @Test
    public void testVictoryAwarded () throws PWCGException
    {     
        SquadronMember aiSquadMember = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Corporal");        
        Squadron squadronMemberSquadron = aiSquadMember.determineSquadron();
        DuringCampaignVictimGenerator duringCampaignVictimGenerator = new DuringCampaignVictimGenerator(campaign, squadronMemberSquadron);

        EnemySquadronFinder enemySquadronFinder = new EnemySquadronFinder(campaign);
        Squadron victimSquadron = enemySquadronFinder.getRandomEnemyViableSquadron(squadronMemberSquadron, campaign.getDate());
        
        OutOfMissionVictoryGenerator victoryGenerator = new OutOfMissionVictoryGenerator(campaign, victimSquadron, duringCampaignVictimGenerator, aiSquadMember);
        Victory victory = victoryGenerator.generateOutOfMissionVictory(campaign.getDate());
        
        assert (victory.getVictim().getAirOrGround() == Victory.AIR_VICTORY);
    }
}
