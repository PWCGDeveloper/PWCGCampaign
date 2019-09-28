package pwcg.campaign.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.mission.flight.factory.BoSFlightCoopFactory;
import pwcg.mission.flight.factory.BoSFlightFactory;
import pwcg.mission.flight.factory.FlightFactory;
import pwcg.mission.flight.factory.RoFFlightFactory;

public class PWCGFlightFactoryFactory
{
    
    public static FlightFactory createFlightFactory(Campaign campaign)
    {
        if (PWCGContextManager.isRoF())
        {
            return new RoFFlightFactory(campaign);
        }
        else
        {
            if (campaign.isCoop())
            {
                return new BoSFlightCoopFactory(campaign);
                
            }
            else
            {
                return new BoSFlightFactory(campaign);
                
            }
        }
    }
    
    public static FlightFactory createSpecialFlightFactory(Campaign campaign)
    {
        if (PWCGContextManager.isRoF())
        {
            return new RoFSpecialFlightFactory(campaign);
        }
        else
        {
            return new BoSSpecialFlightFactory(campaign);
        }
    }

}
