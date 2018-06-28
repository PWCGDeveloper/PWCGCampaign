package pwcg.campaign.factory;

import pwcg.campaign.api.IGroundUnitNames;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.ww1.ground.vehicle.RoFGroundUnitNames;
import pwcg.campaign.ww2.ground.vehicle.BoSGroundUnitNames;

public class GroundUnitNameFactory
{
    public static IGroundUnitNames createGroundUnitNames()
    {
        if (PWCGContextManager.isRoF())
        {
            return new RoFGroundUnitNames();
        }
        else
        {
            return new BoSGroundUnitNames();
        }
    }
}
