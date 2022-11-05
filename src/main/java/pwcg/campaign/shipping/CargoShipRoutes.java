package pwcg.campaign.shipping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.skirmish.TargetDistance;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;

public class CargoShipRoutes
{
    private List<CargoShipRoute> routeDefinitions = new ArrayList<>();

    public List<CargoShipRoute> getRouteDefinitions()
    {
        return routeDefinitions;
    }

    public CargoShipRoute getNearbyCargoShipRouteBySide(Campaign campaign, Squadron squadron, Side side) throws PWCGException
    {
        List<CargoShipRoute> nearbyRouteDefinitions = new ArrayList<>();
        for (CargoShipRoute cargoRoute : routeDefinitions)
        {
            if (!DateUtils.isDateInRange(campaign.getDate(), cargoRoute.getRouteStartDate(), cargoRoute.getRouteStopDate()))
            {
                continue;
            }
            
            double distance = MathUtils.calcDist(cargoRoute.getRouteDestination(), squadron.determineCurrentPosition(campaign.getDate()));
            if (distance < TargetDistance.findMaxTargetDistanceForSquadron(campaign, squadron.getSquadronId()))
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

    public CargoShipRoute getCargoShipRouteByName(String skirmishName) throws PWCGException
    {
        for (CargoShipRoute cargoRoute : routeDefinitions)
        {
            if (cargoRoute.getName().equals(skirmishName))
            {
                return cargoRoute;
            }
        }
        
        throw new PWCGException("No cargo route for name " + skirmishName);
    }

    public void setRouteDefinitions(List<CargoShipRoute> routeDefinitions)
    {
        this.routeDefinitions = routeDefinitions;
    }

}
