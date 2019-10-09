package pwcg.campaign.factory;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.mission.ground.vehicle.IVehicleFactory;
import pwcg.product.bos.ground.vehicle.BoSVehicleFactory;
import pwcg.product.fc.ground.vehicle.FCVehicleFactory;
import pwcg.product.rof.ground.vehicle.RoFVehicleFactory;

public class VehicleFactory
{
    public static IVehicleFactory createVehicleFactory()
    {
        if (PWCGContext.getProduct() == PWCGProduct.ROF)
        {
            return new RoFVehicleFactory();
        }
        else if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            return new FCVehicleFactory();
        }
        else
        {
            return new BoSVehicleFactory();
        }
    }

}
