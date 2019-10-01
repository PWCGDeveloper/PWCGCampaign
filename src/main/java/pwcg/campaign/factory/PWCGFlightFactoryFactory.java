package pwcg.campaign.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.mission.flight.factory.BoSFlightCoopFactory;
import pwcg.mission.flight.factory.BoSFlightTypeFactory;
import pwcg.mission.flight.factory.BoSSpecialFlightFactory;
import pwcg.mission.flight.factory.IFlightTypeFactory;
import pwcg.mission.flight.factory.RoFFlightTypeFactory;
import pwcg.mission.flight.factory.RoFSpecialFlightFactory;

public class PWCGFlightFactoryFactory
{
    
    public static IFlightTypeFactory createFlightFactory(Campaign campaign)
    {
        if (PWCGContextManager.isRoF())
        {
            return new RoFFlightTypeFactory(campaign);
        }
        else
        {
            if (campaign.isCoop())
            {
                return new BoSFlightCoopFactory(campaign);
            }
            else
            {
                return new BoSFlightTypeFactory(campaign);
            }
        }
    }

    public static IFlightTypeFactory createSpecialFlightFactory(Campaign campaign)
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
