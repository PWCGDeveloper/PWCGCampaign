package pwcg.aar.inmission.phase1.parse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.AARMissionLogFileSet;

@ExtendWith(MockitoExtension.class)
public class AARMissionLogFileSetTest
{
    public AARMissionLogFileSetTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }
    
    @Test
    public void testDateParse() throws PWCGException
    {
        AARMissionLogFileSet missionLogFileSet = new AARMissionLogFileSet();
        missionLogFileSet.setLogFileName("missionReport(2018-05-05_18-20-11)[0].txt");
        String dateString = missionLogFileSet.getFileSetTimeStamp();
        Assertions.assertTrue (dateString.equals("2018-05-05_18-20-11"));
    }

}
