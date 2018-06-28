package pwcg.campaign.factory;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.ww1.map.RoFMapLoader;
import pwcg.campaign.ww2.map.BoSMapLoader;

public class MapLoaderFactory
{
    public static Runnable createMapLoader()
    {
        if (PWCGContextManager.isRoF())
        {
            return new RoFMapLoader();
        }
        else
        {
            return new BoSMapLoader();
        }
    }
}
