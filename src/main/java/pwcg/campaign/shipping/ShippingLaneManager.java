package pwcg.campaign.shipping;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.io.json.CargoRoutesIOJson;
import pwcg.campaign.io.json.ShipEncounterZonesIOJson;
import pwcg.campaign.io.json.ShippingLaneIOJson;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public class ShippingLaneManager
{

    private ShippingLanes shippingLanes = new ShippingLanes();
    private CargoShipRoutes cargoRoutes = new CargoShipRoutes();
    private ShipEncounterZones shipEncounterZones = new ShipEncounterZones();

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
            cargoRoutes = new CargoShipRoutes();
        }

        shipEncounterZones = ShipEncounterZonesIOJson.readJson(mapName);
        if (shipEncounterZones == null)
        {
            shipEncounterZones = new ShipEncounterZones();
        }
    }

    public ShippingLane getClosestShippingLaneBySide(Coordinate targetGeneralLocation, Side side) throws PWCGException
    {
        return shippingLanes.getClosestShippingLaneBySide(targetGeneralLocation, side);
    }

    public CargoShipRoute getNearbyCargoShipRouteBySide(Campaign campaign, Squadron squadron, Side side) throws PWCGException
    {
        CargoShipRoute cargoRoute = cargoRoutes.getNearbyCargoShipRouteBySide(campaign, squadron, side);
        if (cargoRoute != null)
        {
            return cargoRoute.copy();
        }
        else
        {
            return null;
        }
    }

    public CargoShipRoute getCargoShipRouteByName(String skirmishName) throws PWCGException
    {
        CargoShipRoute cargoRoute = cargoRoutes.getCargoShipRouteByName(skirmishName);
        return cargoRoute;
    }

    public ShipEncounterZone getNearbyEncounterZone(Campaign campaign, Coordinate playerSquadronPosition) throws PWCGException
    {
        ShipEncounterZone shipEncounterZone = shipEncounterZones.getNearbyShipEncounterZone(campaign, playerSquadronPosition);
        return shipEncounterZone;
    }

    public ShipEncounterZone getShipEncounterByName(String skirmishName) throws PWCGException
    {
        ShipEncounterZone shipEncounterZone = shipEncounterZones.getShipEncounterByName(skirmishName);
        return shipEncounterZone;
    }
}
