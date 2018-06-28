package pwcg.campaign.factory;

import pwcg.campaign.api.IStaticPlaneSelector;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.ww1.plane.RoFStaticPlaneSelector;
import pwcg.campaign.ww2.plane.BoSStaticPlaneSelector;

public class StaticPlaneSelectorFactory
{
    public static IStaticPlaneSelector createStaticPlaneSelector()
    {
        if (PWCGContextManager.isRoF())
        {
            return new RoFStaticPlaneSelector();
        }
        else
        {
            return new BoSStaticPlaneSelector();
        }
    }
}
