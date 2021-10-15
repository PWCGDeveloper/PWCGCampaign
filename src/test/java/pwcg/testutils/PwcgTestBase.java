package pwcg.testutils;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;

public class PwcgTestBase
{
    public PwcgTestBase(PWCGProduct product) throws PWCGException
    {
        PWCGContext.setProduct(product);
        TestDriver.getInstance().reset();
    }
}
