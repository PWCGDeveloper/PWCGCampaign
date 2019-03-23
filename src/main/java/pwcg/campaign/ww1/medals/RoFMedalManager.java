package pwcg.campaign.ww1.medals;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.medals.MedalManager;
import pwcg.core.exception.PWCGException;

public abstract class RoFMedalManager extends MedalManager
{
    
    public RoFMedalManager(Campaign campaign)
    {
        super(campaign);
    }

    public static MedalManager getManager(ICountry country, Campaign campaign) throws PWCGException 
    {
        MedalManager medalManager = null;
        
        if (country.isCountry(Country.GERMANY))
        {
            medalManager = new GermanMedalManager(campaign);
        }
        if (country.isCountry(Country.AUSTRIA))
        {
            medalManager = new AustrianMedalManager(campaign);
        }
        if (country.isCountry(Country.FRANCE))
        {
            medalManager = new FrenchMedalManager(campaign);
        }
        if (country.isCountry(Country.BRITAIN))
        {
            medalManager = new BritishMedalManager(campaign);
        }
        if (country.isCountry(Country.USA))
        {
            medalManager = new AmericanMedalManager(campaign);
        }
        if (country.isCountry(Country.BELGIUM))
        {
            medalManager = new BelgianMedalManager(campaign);
        }
        if (country.isCountry(Country.RUSSIA))
        {
            medalManager = new RussianMedalManager(campaign);
        }
               
        if (medalManager == null)
        {
            throw new PWCGException ("No medal manager for country " + country.getCountryName());
        }
        
        return medalManager;
    }

    public List<MedalManager> getAllManagers(Campaign campaign) 
    {
        List<MedalManager> medalManagers = new ArrayList<MedalManager>();
        medalManagers.add(new FrenchMedalManager(campaign));
        medalManagers.add(new BritishMedalManager(campaign));
        medalManagers.add(new AmericanMedalManager(campaign));
        medalManagers.add(new BelgianMedalManager(campaign));
        medalManagers.add(new RussianMedalManager(campaign));
        medalManagers.add(new GermanMedalManager(campaign));
        medalManagers.add(new AustrianMedalManager(campaign));
        
        
        return medalManagers;
    }


}
