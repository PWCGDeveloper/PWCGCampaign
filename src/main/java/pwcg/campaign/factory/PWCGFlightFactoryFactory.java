package pwcg.campaign.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.mission.flight.factory.BoSFlightTypeCoopFactory;
import pwcg.mission.flight.factory.BoSFlightTypeFactory;
import pwcg.mission.flight.factory.BoSFlightTypeSpecialFactory;
import pwcg.mission.flight.factory.FCFlightTypeCoopFactory;
import pwcg.mission.flight.factory.FCFlightTypeFactory;
import pwcg.mission.flight.factory.FCFlightTypeSpecialFactory;
import pwcg.mission.flight.factory.IFlightTypeFactory;

public class PWCGFlightFactoryFactory
{
    
    public static IFlightTypeFactory createFlightFactory(Campaign campaign)
    {
        if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            if (campaign.isCoop())
            {
                return new FCFlightTypeCoopFactory(campaign);
            }
            else
            {
                return new FCFlightTypeFactory(campaign);
            }
        }
        else
        {
            if (campaign.isCoop())
            {
                return new BoSFlightTypeCoopFactory(campaign);
            }
            else
            {
                return new BoSFlightTypeFactory(campaign);
            }
        }
    }

    public static IFlightTypeFactory createSpecialFlightFactory(Campaign campaign)
    {
        if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            return new FCFlightTypeSpecialFactory(campaign);
        }
        else
        {
            return new BoSFlightTypeSpecialFactory(campaign);
        }
    }

}
