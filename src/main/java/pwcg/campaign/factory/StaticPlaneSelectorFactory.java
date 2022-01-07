package pwcg.campaign.factory;

import pwcg.campaign.api.IStaticPlaneSelector;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.StaticPlaneSelector;
import pwcg.core.exception.PWCGException;

public class StaticPlaneSelectorFactory
{
    public static IStaticPlaneSelector createStaticPlaneSelector() throws PWCGException
    {
        if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            return new StaticPlaneSelector();
        }
        else
        {
            throw new PWCGException("No valid product selected");
        }

    }
}
