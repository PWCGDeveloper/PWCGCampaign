package pwcg.campaign.shipping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.factory.CountryFactory;
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

    public CargoRoute getCargoRouteForSideAndDate(Side side, Date date) throws PWCGException
    {
        List<CargoRoute> availableRouteDefinitions = new ArrayList<>();
        for (CargoRoute cargoRoute : routeDefinitions)
        {
            ICountry country = CountryFactory.makeCountryByCountry(cargoRoute.getCountry());
            if (country.getSide() == side)
            {
                if (DateUtils.isDateInRange(date, cargoRoute.getRouteStartDate(), cargoRoute.getRouteStopDate()))
                {
                    availableRouteDefinitions.add(cargoRoute);
                }
            }
        }
        
        
        if (!availableRouteDefinitions.isEmpty())
        {
            Collections.shuffle(availableRouteDefinitions);
            return availableRouteDefinitions.get(0);
        }
        
        return null;
    }

    public CargoRoute getNearbyCargoShipRouteBySide(Coordinate targetGeneralLocation, Side side)
    {
        List<CargoRoute> nearbyRouteDefinitions = new ArrayList<>();
        for (CargoRoute cargoRoute : routeDefinitions)
        {
            double distance = MathUtils.calcDist(cargoRoute.getRouteDestination(), targetGeneralLocation);
            if (distance < 120000)
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

}
