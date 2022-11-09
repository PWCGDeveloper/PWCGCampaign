package pwcg.aar.outofmission.phase1.elapsedtime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.data.AARContext;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.outofmission.DuringCampaignAirVictimGenerator;
import pwcg.campaign.outofmission.OutOfMissionAirVictoryBuilder;
import pwcg.campaign.personnel.EnemySquadronFinder;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.TestCampaignFactoryBuilder;
import pwcg.testutils.CampaignPersonnelTestHelper;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
public class OutOfMissionAirVictoryBuilderTest
{    
    @Mock private AARContext aarContext;
    @Mock private ArmedService service;

    @Test
    public void testVictoryAwarded () throws PWCGException
    {     
        PWCGContext.setProduct(PWCGProduct.BOS);
        Campaign campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.JG_51_PROFILE_STALINGRAD);

        SquadronMember aiSquadMember = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Serzhant");        
        Squadron squadronMemberSquadron = aiSquadMember.determineSquadron();

        EnemySquadronFinder enemySquadronFinder = new EnemySquadronFinder(campaign);
        Squadron victimSquadron = enemySquadronFinder.getEnemyForOutOfMission(squadronMemberSquadron, campaign.getDate());
        DuringCampaignAirVictimGenerator duringCampaignVictimGenerator = new DuringCampaignAirVictimGenerator(campaign, victimSquadron);
        
        OutOfMissionAirVictoryBuilder victoryGenerator = new OutOfMissionAirVictoryBuilder(campaign, victimSquadron, duringCampaignVictimGenerator, aiSquadMember);
        Victory victory = victoryGenerator.generateOutOfMissionVictory(campaign.getDate());
        
        Assertions.assertTrue (victory.getVictim().getAirOrGround() == Victory.AIRCRAFT);
    }
}
