package pwcg.aar.inmission.phase1.parse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.prelim.AARHeaderParser;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class AARHeaderParserTest
{
    @Test
    public void testSuccessfulHeaderParse() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        AARHeaderParser headerParser = new AARHeaderParser();
        String missionFileName = headerParser.parseLine("Patrik Schorner", "T:0 AType:0 GDate:1941.10.2 GTime:12:0:0 MFile:missions/patrik schorner 1941-10-02.mission MID: GType:0 CNTRS:0:0,101:1,201:2");
        Assertions.assertTrue (missionFileName.equals("Patrik Schorner 1941-10-02"));
    }
    
    @Test
    public void testFailedHeaderParse() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        AARHeaderParser headerParser = new AARHeaderParser();
        String missionFileName = headerParser.parseLine("Patrik Schorner", "AType:20 USERID:6cfc9723-e261-44cc-b59c-1a1abc71edb5 USERNICKID:beadb889-54ff-4b2c-ba6c-185bc706600b");
        Assertions.assertTrue (missionFileName.equals(""));
    }
    
}
