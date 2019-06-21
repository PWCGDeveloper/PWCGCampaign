package pwcg.campaign.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
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
            return new BoSFlightFactory(campaign);
        }
    }
}
