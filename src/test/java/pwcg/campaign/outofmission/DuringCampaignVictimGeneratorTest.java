package pwcg.campaign.outofmission;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.EnemySquadronFinder;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class DuringCampaignVictimGeneratorTest
{
    private Campaign campaign;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.MOSCOW_MAP);
        campaign = CampaignCache.makeCampaign(SquadrontTestProfile.JG_51_PROFILE_MOSCOW);
    }

    @Test
    public void testVictimGeneration () throws PWCGException
    {               
        Squadron victorSquadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadrontTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId());        
        EnemySquadronFinder enemySquadronFinder = new EnemySquadronFinder(campaign);
        Squadron victimSquadron = enemySquadronFinder.getRandomEnemyViableSquadron(victorSquadron, campaign.getDate());

        DuringCampaignVictimGenerator  victimGenerator = new DuringCampaignVictimGenerator(campaign, victimSquadron);
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
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadrontTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId());
        DuringCampaignVictimGenerator  victimGenerator = new DuringCampaignVictimGenerator(campaign, squadron);
        SquadronMember victim = victimGenerator.generateVictimAiCrew();
        assert(victim == null);
    }

}
