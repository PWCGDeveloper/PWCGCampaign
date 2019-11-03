package pwcg.aar.inmission.phase1.parse.event;

import pwcg.core.exception.PWCGException;

public class LogEventFactory
{
    public static IAType0 createAType0(String campaignName, String line) throws PWCGException 
    {
        return new pwcg.aar.inmission.phase1.parse.event.AType0(campaignName, line);
    }

    public static IAType10 createAType10(String line) throws PWCGException 
    {
        return new pwcg.aar.inmission.phase1.parse.event.AType10(line);
    }

    public static IAType12 createAType12(String line) throws PWCGException 
    {
        return new pwcg.aar.inmission.phase1.parse.event.AType12(line);
    }

    public static IAType17 createAType17(String line) throws PWCGException 
    {
        return new pwcg.aar.inmission.phase1.parse.event.AType17(line);
    }

    public static IAType2 createAType2(String line) throws PWCGException 
    {
        return new pwcg.aar.inmission.phase1.parse.event.AType2(line);
    }

    public static IAType3 createAType3(String line) throws PWCGException 
    {
        return new pwcg.aar.inmission.phase1.parse.event.AType3(line);
    }

    public static IAType6 createAType6(String line) throws PWCGException 
    {
        return new pwcg.aar.inmission.phase1.parse.event.AType6(line);
    }

    public static IAType18 createAType18(String line) throws PWCGException
    {
        return new pwcg.aar.inmission.phase1.parse.event.AType18(line);
    }
}
