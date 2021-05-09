package pwcg.testutils;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.utils.TestDriver;

public class PwcgTestBase
{
    public PwcgTestBase(PWCGProduct product)
    {
        PWCGContext.setProduct(product);
        TestDriver.getInstance().reset();
    }
}
