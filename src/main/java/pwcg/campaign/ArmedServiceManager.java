package pwcg.campaign;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;

public abstract class ArmedServiceManager
{
    protected HashMap<Integer, List<ArmedService>> armedServicesByCountry = new HashMap<Integer, List<ArmedService>>();

    public abstract ArmedService getArmedServiceById(int armedServiceId, Date campaignDate) throws PWCGException;
    public abstract ArmedService getArmedServiceByName(String armedServiceName, Date campaignDate) throws PWCGException ;
    protected abstract void initialize() ;
    public abstract ArmedService getPrimaryServiceForNation(Country country, Date date) throws PWCGException;

    public ArmedService getArmedService(int armedServiceId) throws PWCGException
    {
        for(List <ArmedService> armedServiceList : armedServicesByCountry.values())
        {
            for (ArmedService armedService : armedServiceList)
            {
                if (armedService.getServiceId() == armedServiceId)
                {
                    return armedService;
                }
            }
        }
        
        throw new PWCGException("No armed service found for id " + armedServiceId);
    }

    public List<ArmedService> getAllArmedServices() throws PWCGException
    {
        List<ArmedService> allServices = new ArrayList<ArmedService>();
        
        for (List<ArmedService> armedServicesForCountry : armedServicesByCountry.values())
        {
            allServices.addAll(armedServicesForCountry);
        }
        
        return allServices;
    }

    public List<ArmedService> getAlliedServices(Date date) throws PWCGException
    {
        List<ArmedService> alliedServices = new ArrayList<ArmedService>();
        
        for (int countryCode : armedServicesByCountry.keySet())
        {
            if (CountryFactory.makeCountryByCode(countryCode).getSide() == Side.ALLIED)
            {
                List<ArmedService> armedServicesForCountry = armedServicesByCountry.get(countryCode);
                alliedServices.addAll(armedServicesForCountry);
            }
        }
        
        return alliedServices;
    }

    public List<ArmedService> getAxisServices(Date date) throws PWCGException
    {
        List<ArmedService> axisServices = new ArrayList<ArmedService>();
        
        for (int countryCode : armedServicesByCountry.keySet())
        {
            if (CountryFactory.makeCountryByCode(countryCode).getSide() == Side.AXIS)
            {
                List<ArmedService> armedServicesForCountry = armedServicesByCountry.get(countryCode);
                axisServices.addAll(armedServicesForCountry);
            }
        }
        
        return axisServices;
    }

}
