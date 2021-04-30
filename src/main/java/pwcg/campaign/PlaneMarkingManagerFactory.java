package pwcg.campaign;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.IPlaneMarkingManager;
import pwcg.product.bos.plane.BoSPlaneMarkingManager;
import pwcg.product.fc.plane.FCPlaneMarkingManager;

public class PlaneMarkingManagerFactory
{
    public static IPlaneMarkingManager buildIPlaneMarkingManager()
    {
        if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            return new BoSPlaneMarkingManager();
        }
        else
        {
            return new FCPlaneMarkingManager();
        }
    }
}
