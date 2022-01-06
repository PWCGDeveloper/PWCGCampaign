package pwcg.aar.outofmission.phase1.elapsedtime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.data.AARContext;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.outofmission.DuringCampaignAirVictimGenerator;
import pwcg.campaign.outofmission.OutOfMissionAirVictoryBuilder;
import pwcg.campaign.personnel.EnemySquadronFinder;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignPersonnelTestHelper;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OutOfMissionAirVictoryBuilderTest
{
    private Campaign campaign;
    
    @Mock private AARContext aarContext;
    @Mock private ArmedService service;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.GROSS_DEUTSCHLAND_PROFILE);
    }

    @Test
    public void testVictoryAwarded () throws PWCGException
    {     
        CrewMember aiSquadMember = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Serzhant");        
        Company squadronMemberSquadron = aiSquadMember.determineSquadron();

        EnemySquadronFinder enemySquadronFinder = new EnemySquadronFinder(campaign);
        Company victimSquadron = enemySquadronFinder.getEnemyForOutOfMission(squadronMemberSquadron, campaign.getDate());
        DuringCampaignAirVictimGenerator duringCampaignVictimGenerator = new DuringCampaignAirVictimGenerator(campaign, victimSquadron);
        
        OutOfMissionAirVictoryBuilder victoryGenerator = new OutOfMissionAirVictoryBuilder(campaign, victimSquadron, duringCampaignVictimGenerator, aiSquadMember);
        Victory victory = victoryGenerator.generateOutOfMissionVictory(campaign.getDate());
        
        Assertions.assertTrue (victory.getVictim().getAirOrGround() == Victory.AIRCRAFT);
    }
}
