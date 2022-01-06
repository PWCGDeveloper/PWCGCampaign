package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.outofmission.phase2.awards.HistoricalAceAwards;
import pwcg.aar.outofmission.phase2.awards.HistoricalAceAwardsGenerator;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HistoricalAceAwardsGeneratorTest
{
    private Campaign campaign;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);
    }

    @Test
    public void testHistoricalAceUpdateOverTime () throws PWCGException
    {     
        Date newDate = DateUtils.getDateYYYYMMDD("19181030");
        HistoricalAceAwardsGenerator awardsGenerator = new HistoricalAceAwardsGenerator(campaign, newDate);
        HistoricalAceAwards historicalAceEvents = awardsGenerator.aceEvents();
        
        Assertions.assertTrue (historicalAceEvents.getAceVictories().size() > 0);
        Assertions.assertTrue (historicalAceEvents.getAceMedals().size() > 0);
        Assertions.assertTrue (historicalAceEvents.getAcePromotions().size() > 0);
    }


}
