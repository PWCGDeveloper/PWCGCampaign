package pwcg.aar.inmission.phase1.parse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class AARMissionLogFileSetTest
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }
    
    
    @Test
    public void testDateParse() throws PWCGException
    {
        AARMissionLogFileSet missionLogFileSet = new AARMissionLogFileSet();
        missionLogFileSet.setLogFileName("missionReport(2018-05-05_18-20-11)[0].txt");
        String dateString = missionLogFileSet.getFileSetTimeStamp();
        assert (dateString.equals("2018-05-05_18-20-11"));
    }

}
