package pwcg.campaign.shipping;

import java.util.Date;

import pwcg.campaign.api.Side;
import pwcg.campaign.io.json.CargoRoutesIOJson;
import pwcg.campaign.io.json.ShippingLaneIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public class ShippingLaneManager
{

    private ShippingLanes shippingLanes = new ShippingLanes();
    private CargoRoutes cargoRoutes = new CargoRoutes();

    public ShippingLaneManager()
    {
    }

    public void configure(String mapName) throws PWCGException
    {
        shippingLanes = ShippingLaneIOJson.readJson(mapName);
        if (shippingLanes == null)
        {
            shippingLanes = new ShippingLanes();
        }

        cargoRoutes = CargoRoutesIOJson.readJson(mapName);
        if (cargoRoutes == null)
        {
            cargoRoutes = new CargoRoutes();
        }
    }

    public ShippingLane getClosestShippingLaneBySide(Coordinate targetGeneralLocation, Side side) throws PWCGException
    {
        return shippingLanes.getClosestShippingLaneBySide(targetGeneralLocation, side);
    }

    public CargoRoute getNearbyCargoShipRouteBySide(Date date, Coordinate targetGeneralLocation, Side side) throws PWCGException
    {
        CargoRoute cargoRoute = cargoRoutes.getNearbyCargoShipRouteBySide(date, targetGeneralLocation, side);
        if (cargoRoute != null)
        {
            return cargoRoute.copy();
        }
        else
        {
            return null;
        }
    }

    public CargoRoute getCargoShipRouteByName(String skirmishName) throws PWCGException
    {
        CargoRoute cargoRoute = cargoRoutes.getCargoShipRouteByName(skirmishName);
        return cargoRoute;
    }
}
