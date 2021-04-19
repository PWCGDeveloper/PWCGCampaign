package pwcg.campaign.shipping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.skirmish.SkirmishDistance;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;

public class CargoRoutes
{
    private List<CargoRoute> routeDefinitions = new ArrayList<>();

    public List<CargoRoute> getRouteDefinitions()
    {
        return routeDefinitions;
    }

    public CargoRoute getNearbyCargoShipRouteBySide(Date date, Coordinate targetGeneralLocation, Side side) throws PWCGException
    {
        List<CargoRoute> nearbyRouteDefinitions = new ArrayList<>();
        for (CargoRoute cargoRoute : routeDefinitions)
        {
            if (!DateUtils.isDateInRange(date, cargoRoute.getRouteStartDate(), cargoRoute.getRouteStopDate()))
            {
                continue;
            }
            
            double distance = MathUtils.calcDist(cargoRoute.getRouteDestination(), targetGeneralLocation);
            if (distance < SkirmishDistance.findMaxSkirmishDistance())
            {
                nearbyRouteDefinitions.add(cargoRoute);
            }
        }
        
        if (!nearbyRouteDefinitions.isEmpty())
        {
            Collections.shuffle(nearbyRouteDefinitions);
            return nearbyRouteDefinitions.get(0);
        }
        return null;
    }

    public CargoRoute getCargoShipRouteByName(String skirmishName) throws PWCGException
    {
        for (CargoRoute cargoRoute : routeDefinitions)
        {
            if (cargoRoute.getName().equals(skirmishName))
            {
                return cargoRoute;
            }
        }
        
        throw new PWCGException("No cargo route for name " + skirmishName);
    }

}
