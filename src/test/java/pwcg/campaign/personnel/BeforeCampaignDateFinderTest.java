package pwcg.campaign.personnel;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class BeforeCampaignDateFinderTest
{
    @Test
    public void initialDateIsOKTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Date useDate = BeforeCampaignDateFinder.useEarliestPossibleDate(DateUtils.getDateYYYYMMDD("19411002"));
        assert (useDate.equals(DateUtils.getDateYYYYMMDD("19411002")));
    }
    
    @Test
    public void initialDateIsNotOKTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Date useDate = BeforeCampaignDateFinder.useEarliestPossibleDate(DateUtils.getDateYYYYMMDD("19410930"));
        assert (useDate.equals(DateUtils.getDateYYYYMMDD("19411001")));
    }
}
