package pwcg.mission.ground.vehicle;

import java.util.Date;

import org.junit.jupiter.api.Test;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class VehicleFactoryFCTest
{
    public VehicleFactoryFCTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void vehicleCreateTest() throws PWCGException
    {
        createVehicles(CountryFactory.makeCountryByCountry(Country.FRANCE), DateUtils.getDateYYYYMMDD("19170801"));
        createVehicles(CountryFactory.makeCountryByCountry(Country.GERMANY), DateUtils.getDateYYYYMMDD("19170801"));
        createVehicles(CountryFactory.makeCountryByCountry(Country.BRITAIN), DateUtils.getDateYYYYMMDD("19170801"));
        createVehicles(CountryFactory.makeCountryByCountry(Country.USA), DateUtils.getDateYYYYMMDD("19180501"));
    }
    
    public void createVehicles(ICountry country, Date date) throws PWCGException
    {
        for (VehicleClass vehicleClass: VehicleClass.getAllVehicleClasses())
        {
            if (vehicleClass.isStatic())
            {
                continue;
            }
            
            IVehicle vehicle = VehicleFactory.createVehicle(country, date, vehicleClass);
            assert(vehicle != null);
        }
    }
    

    @Test
    public void locomotiveCreateTest() throws PWCGException
    {
        createLocomotive(CountryFactory.makeCountryByCountry(Country.FRANCE), DateUtils.getDateYYYYMMDD("19170801"));
        createLocomotive(CountryFactory.makeCountryByCountry(Country.GERMANY), DateUtils.getDateYYYYMMDD("19170801"));
        createLocomotive(CountryFactory.makeCountryByCountry(Country.BRITAIN), DateUtils.getDateYYYYMMDD("19170801"));
        createLocomotive(CountryFactory.makeCountryByCountry(Country.USA), DateUtils.getDateYYYYMMDD("19180501"));
    }
    
    public void createLocomotive(ICountry country, Date date) throws PWCGException
    {
        IVehicle vehicle = VehicleFactory.createLocomotive(country, date);
        assert(vehicle != null);
    }

}
