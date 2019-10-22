package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.data.AARContext;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.Ace;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class PersonnelAceLossOutOfMissionHandlerTest
{
    private Campaign campaign;
 
    @Mock
    private AARContext aarContext;
    
    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = CampaignCache.makeCampaignForceCreation(SquadronTestProfile.ESC_103_PROFILE);
        
        Mockito.when(aarContext.getNewDate()).thenReturn(DateUtils.getDateYYYYMMDD("19171001"));
    }

    @Test
    public void testAceLossesOutOfMission () throws PWCGException
    {     
        OutOfMissionAceLossCalculator aceLossOutOfMissionHandler = new OutOfMissionAceLossCalculator(campaign, aarContext);
        Map<Integer, Ace> acesKilled = aceLossOutOfMissionHandler.acesKilledHistorically();
        assert (acesKilled.containsKey(101064));
        assert (acesKilled.containsKey(101175));
    }

}
