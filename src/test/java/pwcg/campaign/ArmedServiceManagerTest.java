package pwcg.campaign;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.fc.country.FCServiceManager;

@RunWith(MockitoJUnitRunner.class)
public class ArmedServiceManagerTest
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
    }

    @Test
    public void testGetArmedServiceByNameTest() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedServiceByName(FCServiceManager.DEUTSCHE_LUFTSTREITKRAFTE_NAME,
                DateUtils.getDateYYYYMMDD("19170801"));
        assert (armedService.getServiceId() == FCServiceManager.DEUTSCHE_LUFTSTREITKRAFTE);
    }

    @Test
    public void testGetArmedServiceByService() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedService(FCServiceManager.LAVIATION_MILITAIRE);
        assert (armedService.getName().equals(FCServiceManager.LAVIATION_MILITAIRE_NAME));
    }

    @Test
    public void testGetRFCArmedServiceByIdTest() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedServiceById(FCServiceManager.RFC, DateUtils.getDateYYYYMMDD("19170801"));
        assert (armedService.getName().equals(FCServiceManager.RFC_NAME));
    }

    @Test
    public void testGetRNASArmedServiceByIdTest() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedServiceById(FCServiceManager.RNAS, DateUtils.getDateYYYYMMDD("19170801"));
        assert (armedService.getName().equals(FCServiceManager.RNAS_NAME));
    }

    @Test
    public void testGetRAFFromRFCArmedServiceByIdTest() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedServiceById(FCServiceManager.RFC, DateUtils.getDateYYYYMMDD("19180501"));
        assert (armedService.getName().equals(FCServiceManager.RAF_NAME));
    }

    @Test
    public void testGetRAFFromRNASArmedServiceByIdTest() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedServiceById(FCServiceManager.RNAS, DateUtils.getDateYYYYMMDD("19180501"));
        assert (armedService.getName().equals(FCServiceManager.RAF_NAME));
    }

    @Test
    public void testGetArmedService() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedService(FCServiceManager.LAVIATION_MILITAIRE);
        assert (armedService.getName().equals(FCServiceManager.LAVIATION_MILITAIRE_NAME));
    }

    @Test
    public void testGetAllArmedService() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        List<ArmedService> armedServices = armedServiceManager.getAllArmedServices();
        assert (armedServices.size() == 7);
    }

    @Test
    public void testGetAxisArmedService() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        List<ArmedService> armedServices = armedServiceManager.getAxisServices(DateUtils.getDateYYYYMMDD("19170801"));
        assert (armedServices.size() == 1);
    }

    @Test
    public void testGetAlliedArmedService() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        List<ArmedService> armedServices = armedServiceManager.getAlliedServices(DateUtils.getDateYYYYMMDD("19170801"));
        assert (armedServices.size() == 5);
    }

    @Test
    public void testGetAlliedArmedServicePostRaF() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        List<ArmedService> armedServices = armedServiceManager.getAlliedServices(DateUtils.getDateYYYYMMDD("19181001"));
        assert (armedServices.size() == 4);
    }

}
