package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class HistoricalAceAwardsGeneratorTest
{
    private Campaign campaign;

    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);
    }

    @Test
    public void testHistoricalAceUpdateOverTime () throws PWCGException
    {     
        Date newDate = DateUtils.getDateYYYYMMDD("19181030");
        HistoricalAceAwardsGenerator awardsGenerator = new HistoricalAceAwardsGenerator(campaign, newDate);
        HistoricalAceAwards historicalAceEvents = awardsGenerator.aceEvents();
        
        assert (historicalAceEvents.getAceVictories().size() > 0);
        assert (historicalAceEvents.getAceMedals().size() > 0);
        assert (historicalAceEvents.getAcePromotions().size() > 0);
    }


}
