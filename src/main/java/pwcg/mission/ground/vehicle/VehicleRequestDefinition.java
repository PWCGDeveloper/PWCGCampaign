package pwcg.mission.ground.vehicle;

import java.util.Date;

import pwcg.campaign.context.Country;
import pwcg.core.utils.DateUtils;

public class VehicleRequestDefinition
{
    private Country country;
    private Date date;
    private VehicleClass vehicleClass;
    
    public VehicleRequestDefinition(Country country, Date date, VehicleClass vehicleClass)
    {
        this.country = country;
        this.date = date;
        this.vehicleClass = vehicleClass;
    }

    public Country getCountry()
    {
        return country;
    }

    public Date getDate()
    {
        return date;
    }

    public VehicleClass getVehicleClass()
    {
        return vehicleClass;
    }
    
    public String toString()
    {
        return "Country = " + country + "     Date = " + DateUtils.getDateStringYYYYMMDD(date) + "     Class = " + vehicleClass;
    }
}
