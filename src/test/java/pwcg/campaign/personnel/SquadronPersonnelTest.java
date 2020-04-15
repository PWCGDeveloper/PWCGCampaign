package pwcg.campaign.personnel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class SquadronPersonnelTest {

    Campaign campaign;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
    }

    @Test
    public void isHumanSquadronTest() throws PWCGException
    {
        SquadronPersonnel squadronpersonnel = campaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId());
        assert(squadronpersonnel.isPlayerSquadron());
    }

    @Test
    public void isNotHumanSquadronTest() throws PWCGException
    {
        SquadronPersonnel squadronpersonnel = campaign.getPersonnelManager().getSquadronPersonnel(20111052);
        assert(squadronpersonnel.isPlayerSquadron() == false);
    }
}
