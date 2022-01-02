package pwcg.campaign.context;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class PWCGContext 
{
    protected static BoSContext bosContextManager = null;
    protected static PWCGProduct product = PWCGProduct.NONE;

	protected PWCGContext()
    {
    }

    public static IPWCGContextManager getInstance() 
    {
        try
        {
            return buildProductContext();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
        
        return null;
    }

    public static PWCGProduct getProduct()
    {
        return product;
    }

    public static void setProduct(PWCGProduct product) throws PWCGException
    {
        if (PWCGContext.product != product)
        {
            bosContextManager = null;
            PWCGContext.product = product;
            buildProductContext();
        }
    }

    private static IPWCGContextManager buildProductContext() throws PWCGException
    {
        if (product == PWCGProduct.BOS)
        {
            if (PWCGContext.bosContextManager == null)
            {
                PWCGContext.bosContextManager = new BoSContext();
                PWCGContext.bosContextManager.initialize();
            }
            
            return PWCGContext.bosContextManager;
        }
        else
        {
            throw new PWCGException("No product defined");
        }
    }
}
