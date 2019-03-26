package pwcg.campaign.ww2.medals;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.medals.MedalManager;
import pwcg.core.exception.PWCGException;

public abstract class BoSMedalManager extends MedalManager
{
    public BoSMedalManager (Campaign campaign)
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
        else if (country.isCountry(Country.RUSSIA))
        {
            medalManager = new RussianMedalManager(campaign);
        }
        else if (country.isCountry(Country.ITALY))
        {
            medalManager = new ItalianMedalManager(campaign);
        }
        else if (country.isCountry(Country.USA))
        {
            medalManager = new AmericanMedalManager(campaign);
        }
        else if (country.isCountry(Country.BRITAIN))
        {
            medalManager = new BritishMedalManager(campaign);
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
        medalManagers.add(new GermanMedalManager(campaign));
        medalManagers.add(new RussianMedalManager(campaign));
        medalManagers.add(new ItalianMedalManager(campaign));
        
        
        return medalManagers;
    }
}
