package pwcg.campaign.factory;

import pwcg.campaign.api.IGroundUnitNames;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.product.bos.ground.vehicle.BoSGroundUnitNames;
import pwcg.product.fc.ground.vehicle.FCGroundUnitNames;
import pwcg.product.rof.ground.vehicle.RoFGroundUnitNames;

public class GroundUnitNameFactory
{
    public static IGroundUnitNames createGroundUnitNames()
    {
        if (PWCGContext.getProduct() == PWCGProduct.ROF)
        {
            return new RoFGroundUnitNames();
        }
        else if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            return new FCGroundUnitNames();
        }
        else
        {
            return new BoSGroundUnitNames();
        }
    }
}
