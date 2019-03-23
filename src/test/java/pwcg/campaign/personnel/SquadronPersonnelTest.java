package pwcg.campaign.personnel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class SquadronPersonnelTest {

    Campaign campaign;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        campaign = CampaignCache.makeCampaign(SquadrontTestProfile.JG_51_PROFILE_MOSCOW);
    }

    @Test
    public void isHumanSquadronTest() throws PWCGException
    {
        SquadronPersonnel squadronpersonnel = campaign.getPersonnelManager().getSquadronPersonnel(SquadrontTestProfile.JG_51_PROFILE_MOSCOW.getSquadronId());
        assert(squadronpersonnel.isPlayerSquadron());
    }

    @Test
    public void isNotHumanSquadronTest() throws PWCGException
    {
        SquadronPersonnel squadronpersonnel = campaign.getPersonnelManager().getSquadronPersonnel(20111052);
        assert(squadronpersonnel.isPlayerSquadron() == false);
    }
}
