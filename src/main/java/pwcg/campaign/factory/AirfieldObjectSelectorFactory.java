package pwcg.campaign.factory;

import java.util.Date;

import pwcg.campaign.api.IAirfieldObjectSelector;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.product.rof.airfield.RoFAirfieldObjectSelector;
import pwcg.product.bos.airfield.BoSAirfieldObjectSelector;

public class AirfieldObjectSelectorFactory
{
    public static IAirfieldObjectSelector createAirfieldObjectSelector(Date date)
    {
        if (PWCGContext.getProduct() == PWCGProduct.ROF)
        {
            return new RoFAirfieldObjectSelector(date);
        }
        else
        {
            return new BoSAirfieldObjectSelector(date);
        }
    }
}
