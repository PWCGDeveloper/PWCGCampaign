package pwcg.campaign.api;

import java.util.Date;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.context.Country;
import pwcg.core.exception.PWCGException;

public interface IArmedServiceManager
{

    ArmedService getArmedService(int serviceId) throws PWCGException;

    ArmedService getArmedServiceById(int serviceId, Date campaignDate) throws PWCGException;

    ArmedService getArmedServiceByName(String serviceName, Date campaignDate) throws PWCGException;

    List<ArmedService> getAllArmedServices() throws PWCGException;

    List<ArmedService> getAllActiveArmedServices(Date date) throws PWCGException;

    List<ArmedService> getAlliedServices(Date date) throws PWCGException;

    List<ArmedService> getAxisServices(Date date) throws PWCGException;
    
    ArmedService determineServiceByParsingSquadronId(int squadronId, Date date) throws PWCGException;

    public abstract ArmedService getPrimaryServiceForNation(Country country, Date date) throws PWCGException;

}