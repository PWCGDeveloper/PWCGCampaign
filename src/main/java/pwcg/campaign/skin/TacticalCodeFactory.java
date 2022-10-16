package pwcg.campaign.skin;

import pwcg.campaign.context.Country;

public class TacticalCodeFactory
{
    public static TacticalCode getTacticalCode(Country country)
    {
        if (country == Country.BRITAIN)
        {
            return new TacticalCodeBritain();
        }
        else if (country == Country.USA)
        {
            return new TacticalCodeUSA();
        }
        else if (country == Country.RUSSIA)
        {
            return new TacticalCodeRussia();
        }
        else if (country == Country.GERMANY)
        {
            return new TacticalCodeGermany();
        }
        else if (country == Country.ITALY)
        {
            return new TacticalCodeItaly();
        }
        else
        {
            return null;
        }
    }
}
