package pwcg.campaign.personnel;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class BeforeCampaignDateFinderTest
{
    @Test
    public void initialDateIsOKTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        Date useDate = BeforeCampaignDateFinder.useEarliestPossibleDate(DateUtils.getDateYYYYMMDD("19411002"));
        assert (useDate.equals(DateUtils.getDateYYYYMMDD("19411002")));
    }
    
    @Test
    public void initialDateIsNotOKTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        Date useDate = BeforeCampaignDateFinder.useEarliestPossibleDate(DateUtils.getDateYYYYMMDD("19410930"));
        assert (useDate.equals(DateUtils.getDateYYYYMMDD("19411001")));
    }
}
