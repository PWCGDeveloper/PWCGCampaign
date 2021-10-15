package pwcg.campaign.outofmission;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.EnemySquadronFinder;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DuringCampaignVictimGeneratorTest
{
    private Campaign campaign;    
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
    }

    @Test
    public void testVictimGeneration () throws PWCGException
    {               
        Squadron victorSquadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId());        
        EnemySquadronFinder enemySquadronFinder = new EnemySquadronFinder(campaign);
        Squadron victimSquadron = enemySquadronFinder.getEnemyForOutOfMission(victorSquadron, campaign.getDate());

        DuringCampaignAirVictimGenerator  victimGenerator = new DuringCampaignAirVictimGenerator(campaign, victimSquadron);
        Side victorSide = victorSquadron.determineSquadronCountry(campaign.getDate()).getSide();

        SquadronMember victim = victimGenerator.generateVictimAiCrew();
        Squadron victimSquadronFromVictim = victim.determineSquadron();
        Side victimSide = victimSquadron.determineSquadronCountry(campaign.getDate()).getSide();
        
        assert(victimSide != victorSide);
        assert(victimSquadron.getSquadronId() == victimSquadronFromVictim.getSquadronId());

    }

    @Test
    public void testNotFromPlayerSquadron () throws PWCGException
    {               
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId());
        DuringCampaignAirVictimGenerator  victimGenerator = new DuringCampaignAirVictimGenerator(campaign, squadron);
        SquadronMember victim = victimGenerator.generateVictimAiCrew();
        assert(victim != null);
    }

}
