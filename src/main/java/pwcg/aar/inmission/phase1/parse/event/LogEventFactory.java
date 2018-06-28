package pwcg.aar.inmission.phase1.parse.event;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;

public class LogEventFactory
{
    public static IAType0 createAType0(String campaignName, String line) throws PWCGException 
    {
        if (PWCGContextManager.isRoF())
        {
            return new pwcg.aar.inmission.phase1.parse.event.rof.AType0(campaignName, line);
        }
        else
        {
            return new pwcg.aar.inmission.phase1.parse.event.bos.AType0(campaignName, line);
        }
    }

    public static IAType10 createAType10(String line) throws PWCGException 
    {
        if (PWCGContextManager.isRoF())
        {
            return new pwcg.aar.inmission.phase1.parse.event.rof.AType10(line);
        }
        else
        {
            return new pwcg.aar.inmission.phase1.parse.event.bos.AType10(line);
        }
    }

    public static IAType12 createAType12(String line) throws PWCGException 
    {
        if (PWCGContextManager.isRoF())
        {
            return new pwcg.aar.inmission.phase1.parse.event.rof.AType12(line);
        }
        else
        {
            return new pwcg.aar.inmission.phase1.parse.event.bos.AType12(line);
        }
    }

    public static IAType17 createAType17(String line) throws PWCGException 
    {
        if (PWCGContextManager.isRoF())
        {
            return new pwcg.aar.inmission.phase1.parse.event.rof.AType17(line);
        }
        else
        {
            return new pwcg.aar.inmission.phase1.parse.event.bos.AType17(line);
        }
    }

    public static IAType2 createAType2(String line) throws PWCGException 
    {
        if (PWCGContextManager.isRoF())
        {
            return new pwcg.aar.inmission.phase1.parse.event.rof.AType2(line);
        }
        else
        {
            return new pwcg.aar.inmission.phase1.parse.event.bos.AType2(line);
        }
    }

    public static IAType3 createAType3(String line) throws PWCGException 
    {
        if (PWCGContextManager.isRoF())
        {
            return new pwcg.aar.inmission.phase1.parse.event.rof.AType3(line);
        }
        else
        {
            return new pwcg.aar.inmission.phase1.parse.event.bos.AType3(line);
        }
    }

    public static IAType6 createAType6(String line) throws PWCGException 
    {
        if (PWCGContextManager.isRoF())
        {
            return new pwcg.aar.inmission.phase1.parse.event.rof.AType6(line);
        }
        else
        {
            return new pwcg.aar.inmission.phase1.parse.event.bos.AType6(line);
        }
    }
}
