package pwcg.campaign.shipping;

import java.util.Date;

import pwcg.campaign.context.Country;
import pwcg.core.location.Coordinate;

public class CargoRoute
{
    private Date routeStartDate;
    private Date routeStopDate;
    private Country country;
    private Coordinate routeStartPosition;
    private Coordinate routeDestination;

    public Date getRouteStartDate()
    {
        return routeStartDate;
    }

    public Date getRouteStopDate()
    {
        return routeStopDate;
    }

    public Country getCountry()
    {
        return country;
    }

    public Coordinate getRouteStartPosition()
    {
        return routeStartPosition;
    }

    public Coordinate getRouteDestination()
    {
        return routeDestination;
    }

}
