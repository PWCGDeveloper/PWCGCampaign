package pwcg.product.bos.country;

import pwcg.campaign.api.ICountry;

public class NeutralCountryFactory
{
    public static int NEUTRAL_CODE = 0;

    public static ICountry makeNeutralCountry()
    {
        return new BoSCountry(BoSCountry.NEUTRAL_CODE);
    }
}
