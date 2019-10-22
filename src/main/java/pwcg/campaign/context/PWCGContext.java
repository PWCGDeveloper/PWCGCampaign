package pwcg.campaign.context;

import pwcg.core.utils.Logger;

public class PWCGContext 
{
    protected static BoSContext bosContextManager = null;
    protected static FCContext fcContextManager = null;
    protected static PWCGProduct product = PWCGProduct.BOS;

	protected PWCGContext()
    {
    }

    public static IPWCGContextManager getInstance() 
    {
        try
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
            else if (product == PWCGProduct.FC)
            {
                if (PWCGContext.fcContextManager == null)
                {
                    PWCGContext.fcContextManager = new FCContext();
                    PWCGContext.fcContextManager.initialize();
                }
                
                return PWCGContext.fcContextManager;
            }
        }
        catch (Exception e)
        {
            Logger.logException(e);
        }
        
        return null;
    }

    public static PWCGProduct getProduct()
    {
        return product;
    }

    public static void setProduct(PWCGProduct product)
    {
        PWCGContext.product = product;
    }
 }
