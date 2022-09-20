package pwcg.campaign.shipping;

import java.util.Date;

import pwcg.campaign.context.Country;
import pwcg.core.location.Coordinate;

public class CargoShipRoute
{
    private String name;
    private Date routeStartDate;
    private Date routeStopDate;
    private Country country;
    private Coordinate routeStartPosition;
    private Coordinate routeDestination;

    public CargoShipRoute copy()
    {
        CargoShipRoute copy = new CargoShipRoute();
        copy.name = this.name;
        copy.routeStartDate = this.routeStartDate;
        copy.routeStopDate = this.routeStopDate;
        copy.routeStartPosition = this.routeStartPosition.copy();
        copy.routeDestination = this.routeDestination.copy();
        copy.country = this.country;
        return copy;
    }

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

    public void setRouteStartPosition(Coordinate routeStartPosition)
    {
        this.routeStartPosition = routeStartPosition;
    }

    public Coordinate getRouteStartPosition()
    {
        return routeStartPosition;
    }

    public Coordinate getRouteDestination()
    {
        return routeDestination;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setRouteStartDate(Date routeStartDate)
    {
        this.routeStartDate = routeStartDate;
    }

    public void setRouteStopDate(Date routeStopDate)
    {
        this.routeStopDate = routeStopDate;
    }

    public void setCountry(Country country)
    {
        this.country = country;
    }

    public void setRouteDestination(Coordinate routeDestination)
    {
        this.routeDestination = routeDestination;
    }

    
}
