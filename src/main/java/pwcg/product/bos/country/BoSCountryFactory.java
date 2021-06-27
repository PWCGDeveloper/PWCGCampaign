package pwcg.product.bos.country;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.ICountryFactory;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;

public class BoSCountryFactory implements ICountryFactory
{

    public ICountry makeNeutralCountry()
    {
        return new BoSCountry(BoSCountry.NEUTRAL_CODE);
    }

    public ICountry makeMapReferenceCountry(Side side)
    {
        if (side == Side.ALLIED)
        {
            PWCGMap map = PWCGContext.getInstance().getCurrentMap();
            if (map == null)
            {
                return new BoSCountry(BoSCountry.RUSSIA_CODE);
            }
            else if (map.getMapIdentifier() == FrontMapIdentifier.BODENPLATTE_MAP)
            {
                return new BoSCountry(BoSCountry.USA_CODE);
            }
            else
            {
                return new BoSCountry(BoSCountry.RUSSIA_CODE);
            }
        }
        else
        {
            return new BoSCountry(BoSCountry.GERMANY_CODE);
        }
    }

    public ICountry makeCountryByCode(int countryCode)
    {
        return new BoSCountry(countryCode);
    }

    public ICountry makeCountryByService(ArmedService service)
    {
        return BoSCountry.getCountryByService(service);
    }

    @Override
    public ICountry makeCountryByCountry(Country country)
    {
        return new BoSCountry(country);
    }
}
