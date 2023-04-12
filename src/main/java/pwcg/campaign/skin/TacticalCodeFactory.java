package pwcg.campaign.skin;

import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.skin.bos.TacticalCodeBritain;
import pwcg.campaign.skin.bos.TacticalCodeGermany;
import pwcg.campaign.skin.bos.TacticalCodeItaly;
import pwcg.campaign.skin.bos.TacticalCodeRussia;
import pwcg.campaign.skin.bos.TacticalCodeUSA;
import pwcg.campaign.skin.fc.TacticalCodeBelgiumWWI;
import pwcg.campaign.skin.fc.TacticalCodeBritainWWI;
import pwcg.campaign.skin.fc.TacticalCodeFranceWWI;
import pwcg.campaign.skin.fc.TacticalCodeGermanyWWI;
import pwcg.campaign.skin.fc.TacticalCodeUSAWWI;
import pwcg.core.exception.PWCGException;

public class TacticalCodeFactory
{
    public static TacticalCode getTacticalCode(Country country) throws PWCGException
    {
        if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            return getTacticalCodeBoS(country);
        }
        else
        {
            return getTacticalCodeFC(country);
        }
    }
    
    public static TacticalCode getTacticalCodeBoS(Country country) throws PWCGException
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
            throw new PWCGException("No tactical code builder for country " + country);
        }
    }
    
    public static TacticalCode getTacticalCodeFC(Country country) throws PWCGException
    {
        if (country == Country.BRITAIN)
        {
            return new TacticalCodeBritainWWI();
        }
        else if (country == Country.USA)
        {
            return new TacticalCodeUSAWWI();
        }
        else if (country == Country.GERMANY)
        {
            return new TacticalCodeGermanyWWI();
        }
        else if (country == Country.FRANCE)
        {
            return new TacticalCodeFranceWWI();
        }
        else if (country == Country.BELGIUM)
        {
            return new TacticalCodeBelgiumWWI();
        }
        else
        {
            throw new PWCGException("No tactical code builder for country " + country);
        }
    }
}
