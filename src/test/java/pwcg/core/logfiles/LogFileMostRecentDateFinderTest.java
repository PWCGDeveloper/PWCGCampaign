package pwcg.core.logfiles;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class LogFileMostRecentDateFinderTest
{
    @Mock Campaign campaign;
    
    @Test
    public void testCompareDatesMoreRecent() throws PWCGException
    {
        Mockito.when(campaign.getName()).thenReturn("My Campaign");
    
        LogFileMostRecentDateFinder logSetFinder = new LogFileMostRecentDateFinder(campaign, new LogSetFinder(null));
        Date mostRecentDate = logSetFinder.compareDates(DateUtils.getDateYYYYMMDD("19420901"), "My Campaign 1942-09-02.mission");        
        Assertions.assertEquals("19420902", DateUtils.getDateStringYYYYMMDD(mostRecentDate));
    }
    
    @Test
    public void testCompareDatesNotMoreRecent() throws PWCGException
    {
        Mockito.when(campaign.getName()).thenReturn("My Campaign");
    
        LogFileMostRecentDateFinder logSetFinder = new LogFileMostRecentDateFinder(campaign, new LogSetFinder(null));
        Date mostRecentDate = logSetFinder.compareDates(DateUtils.getDateYYYYMMDD("19420902"), "My Campaign 1942-09-01.mission");        
        Assertions.assertEquals("19420902", DateUtils.getDateStringYYYYMMDD(mostRecentDate));
    }
    
    @Test
    public void testCompareDatesNull() throws PWCGException
    {
        Mockito.when(campaign.getName()).thenReturn("My Campaign");
    
        LogFileMostRecentDateFinder logSetFinder = new LogFileMostRecentDateFinder(campaign, new LogSetFinder(null));
        Date mostRecentDate = logSetFinder.compareDates(null, "My Campaign 1942-09-01.mission");        
        Assertions.assertEquals("19420901", DateUtils.getDateStringYYYYMMDD(mostRecentDate));
    }
    
    @Test
    public void testCompareDatesNullNoCampaignMatch() throws PWCGException
    {
        Mockito.when(campaign.getName()).thenReturn("My Campaign");
    
        LogFileMostRecentDateFinder logSetFinder = new LogFileMostRecentDateFinder(campaign, new LogSetFinder(null));
        Date mostRecentDate = logSetFinder.compareDates(null, "Not My Campaign 1942-09-01.mission");        
        Assertions.assertNull(mostRecentDate);
    }
    
    @Test
    public void testCompareDatesNotNullNoCampaignMatch() throws PWCGException
    {
        Mockito.when(campaign.getName()).thenReturn("My Campaign");
    
        LogFileMostRecentDateFinder logSetFinder = new LogFileMostRecentDateFinder(campaign, new LogSetFinder(null));
        Date mostRecentDate = logSetFinder.compareDates(DateUtils.getDateYYYYMMDD("19420902"), "Not My Campaign 1942-09-01.mission");        
        Assertions.assertEquals("19420902", DateUtils.getDateStringYYYYMMDD(mostRecentDate));
    }
    
}
