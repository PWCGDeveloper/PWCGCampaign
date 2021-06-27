package pwcg.campaign.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.mission.flight.factory.BoSFlightTypeCoopFactory;
import pwcg.mission.flight.factory.BoSFlightTypeFactory;
import pwcg.mission.flight.factory.FCFlightTypeCoopFactory;
import pwcg.mission.flight.factory.FCFlightTypeFactory;
import pwcg.mission.flight.factory.IFlightTypeFactory;
import pwcg.mission.flight.factory.SkirmishFlightTypeFactory;

public class PWCGFlightTypeAbstractFactory
{
    public static IFlightTypeFactory createFlightTypeFactory(Campaign campaign)
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

    public static IFlightTypeFactory createSkirmishFlightTypeFactory(Campaign campaign, Skirmish skirmish)
    {
        IFlightTypeFactory backupFlightTypeFactory = createFlightTypeFactory(campaign);
        return new SkirmishFlightTypeFactory(campaign, skirmish, backupFlightTypeFactory);
    }
}
