package pwcg.campaign.context;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;

public class PWCGContextManager 
{
    protected static RoFContextManager rofContextManager = null;
    protected static BoSContextManager bosContextManager = null;
    private static boolean isRoF = true;

	protected PWCGContextManager()
    {
    }

    public static IPWCGContextManager getInstance() 
    {
        try
        {
            if (isRoF)
            {
                if (PWCGContextManager.rofContextManager == null)
                {
                    PWCGContextManager.rofContextManager = new RoFContextManager();
                    PWCGContextManager.rofContextManager.initialize();
                }
                
                return PWCGContextManager.rofContextManager;
            }
            else
            {
                if (PWCGContextManager.bosContextManager == null)
                {
                    PWCGContextManager.bosContextManager = new BoSContextManager();
                    PWCGContextManager.bosContextManager.initialize();
                }
                
                return PWCGContextManager.bosContextManager;
            }
        }
        catch (Exception e)
        {
            Logger.logException(e);
        }
        
        return null;
    }

    public static void setRoF(boolean isRoF) throws PWCGException
    {
        PWCGContextManager.isRoF = isRoF;
    }

    public static boolean isRoF()
    {
        return isRoF;
    }
 }
