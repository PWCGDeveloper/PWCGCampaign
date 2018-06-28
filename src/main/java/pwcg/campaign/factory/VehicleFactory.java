package pwcg.campaign.factory;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.ww1.ground.vehicle.RoFVehicleFactory;
import pwcg.campaign.ww2.ground.vehicle.BoSVehicleFactory;
import pwcg.mission.ground.vehicle.IVehicleFactory;

public class VehicleFactory
{
    public static IVehicleFactory createVehicleFactory()
    {
        if (PWCGContextManager.isRoF())
        {
            return new RoFVehicleFactory();
        }
        else
        {
            return new BoSVehicleFactory();
        }
    }

}
