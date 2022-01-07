package pwcg.campaign.personnel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SquadronPersonnelTest {

    private Campaign campaign;    
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
    }

    @Test
    public void isHumanSquadronTest() throws PWCGException
    {
        CompanyPersonnel squadronpersonnel = campaign.getPersonnelManager().getCompanyPersonnel(SquadronTestProfile.JG_51_PROFILE_MOSCOW.getCompanyId());
        assert(squadronpersonnel.isPlayerSquadron());
    }

    @Test
    public void isNotHumanSquadronTest() throws PWCGException
    {
        CompanyPersonnel squadronpersonnel = campaign.getPersonnelManager().getCompanyPersonnel(20111052);
        assert(squadronpersonnel.isPlayerSquadron() == false);
    }
}
