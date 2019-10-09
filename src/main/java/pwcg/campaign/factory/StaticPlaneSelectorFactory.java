package pwcg.campaign.factory;

import pwcg.campaign.api.IStaticPlaneSelector;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.plane.BoSStaticPlaneSelector;
import pwcg.product.fc.plane.FCStaticPlaneSelector;
import pwcg.product.rof.plane.RoFStaticPlaneSelector;

public class StaticPlaneSelectorFactory
{
    public static IStaticPlaneSelector createStaticPlaneSelector() throws PWCGException
    {
        if (PWCGContext.getProduct() == PWCGProduct.ROF)
        {
            return new RoFStaticPlaneSelector();
        }
        else if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            return new BoSStaticPlaneSelector();
        }
        else if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            return new FCStaticPlaneSelector();
        }
        else
        {
            throw new PWCGException("No valid product selected");
        }

    }
}
