package pwcg.campaign;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.ww1.country.RoFServiceManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class ArmedServiceManagerTest
{   
    @Test
    public void testGetArmedServiceByNameTest () throws PWCGException
    {        
    	IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
    	ArmedService armedService = armedServiceManager.getArmedServiceByName(RoFServiceManager.DEUTSCHE_LUFTSTREITKRAFTE_NAME, DateUtils.getDateYYYYMMDD("19171001"));
        assert (armedService.getServiceId() == RoFServiceManager.DEUTSCHE_LUFTSTREITKRAFTE);
    }
    
    @Test
    public void testGetArmedServiceByIdTest () throws PWCGException
    {        
    	IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
    	ArmedService armedService = armedServiceManager.getArmedServiceById(RoFServiceManager.LAVIATION_MARINE, DateUtils.getDateYYYYMMDD("19171001"));
        assert (armedService.getName().equals(RoFServiceManager.LAVIATION_MARINE_NAME));
    }
    
    @Test
    public void testGetArmedServiceByService () throws PWCGException
    {        
    	IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
    	ArmedService armedService = armedServiceManager.getArmedService(RoFServiceManager.LAVIATION_MILITAIRE);
        assert (armedService.getName().equals(RoFServiceManager.LAVIATION_MILITAIRE_NAME));
    }
    
    @Test
    public void testGetRFCArmedServiceByIdTest () throws PWCGException
    {        
    	IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
    	ArmedService armedService = armedServiceManager.getArmedServiceById(RoFServiceManager.RFC, DateUtils.getDateYYYYMMDD("19171001"));
        assert (armedService.getName().equals(RoFServiceManager.RFC_NAME));
    }
    
    @Test
    public void testGetRNASArmedServiceByIdTest () throws PWCGException
    {        
    	IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
    	ArmedService armedService = armedServiceManager.getArmedServiceById(RoFServiceManager.RNAS, DateUtils.getDateYYYYMMDD("19171001"));
        assert (armedService.getName().equals(RoFServiceManager.RNAS_NAME));
    }
    
    @Test
    public void testGetRAFFromRFCArmedServiceByIdTest () throws PWCGException
    {        
    	IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
    	ArmedService armedService = armedServiceManager.getArmedServiceById(RoFServiceManager.RFC, DateUtils.getDateYYYYMMDD("19180501"));
        assert (armedService.getName().equals(RoFServiceManager.RAF_NAME));
    }
    
    @Test
    public void testGetRAFFromRNASArmedServiceByIdTest () throws PWCGException
    {        
    	IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
    	ArmedService armedService = armedServiceManager.getArmedServiceById(RoFServiceManager.RNAS, DateUtils.getDateYYYYMMDD("19180501"));
        assert (armedService.getName().equals(RoFServiceManager.RAF_NAME));
    }

    @Test
    public void testGetArmedService () throws PWCGException
    {        
    	IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
    	ArmedService armedService = armedServiceManager.getArmedService(RoFServiceManager.LAVIATION_MILITAIRE);
        assert (armedService.getName().equals(RoFServiceManager.LAVIATION_MILITAIRE_NAME));
    }

    @Test
    public void testGetAllArmedService () throws PWCGException
    {        
    	IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
    	List<ArmedService> armedServices = armedServiceManager.getAllArmedServices();
        assert (armedServices.size() == 11);
    }

    @Test
    public void testGetAxisArmedService () throws PWCGException
    {        
    	IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
    	List<ArmedService> armedServices = armedServiceManager.getAxisServices(DateUtils.getDateYYYYMMDD("19171001"));
        assert (armedServices.size() == 3);
    }

    @Test
    public void testGetAlliedArmedService () throws PWCGException
    {        
    	IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
    	List<ArmedService> armedServices = armedServiceManager.getAlliedServices(DateUtils.getDateYYYYMMDD("19171001"));
        assert (armedServices.size() == 8);
    }

}
