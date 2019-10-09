package pwcg.campaign.factory;

import pwcg.campaign.api.IAirfieldConfiguration;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.product.rof.airfield.RoFAirfieldConfiguration;
import pwcg.product.bos.airfield.BoSAirfieldConfiguration;

public class AirfieldConfigurationFactory
{
    public static IAirfieldConfiguration createAirfieldConfiguration()
    {
        if (PWCGContext.getProduct() == PWCGProduct.ROF)
        {
            return new RoFAirfieldConfiguration();
        }
        else
        {
            return new BoSAirfieldConfiguration();
        }
    }
}
