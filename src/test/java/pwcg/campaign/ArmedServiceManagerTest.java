package pwcg.campaign;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.fc.country.FCServiceManager;

@ExtendWith(MockitoExtension.class)
public class ArmedServiceManagerTest
{
    public ArmedServiceManagerTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void testGetArmedServiceByNameTest() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedServiceByName(FCServiceManager.DEUTSCHE_LUFTSTREITKRAFTE_NAME,
                DateUtils.getDateYYYYMMDD("19170801"));
        Assertions.assertTrue (armedService.getServiceId() == FCServiceManager.DEUTSCHE_LUFTSTREITKRAFTE);
    }

    @Test
    public void testGetArmedServiceByService() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedService(FCServiceManager.LAVIATION_MILITAIRE);
        Assertions.assertTrue (armedService.getName().equals(FCServiceManager.LAVIATION_MILITAIRE_NAME));
    }

    @Test
    public void testGetRFCArmedServiceByIdTest() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedServiceById(FCServiceManager.RFC, DateUtils.getDateYYYYMMDD("19170801"));
        Assertions.assertTrue (armedService.getName().equals(FCServiceManager.RFC_NAME));
    }

    @Test
    public void testGetRNASArmedServiceByIdTest() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedServiceById(FCServiceManager.RNAS, DateUtils.getDateYYYYMMDD("19170801"));
        Assertions.assertTrue (armedService.getName().equals(FCServiceManager.RNAS_NAME));
    }

    @Test
    public void testGetRAFFromRFCArmedServiceByIdTest() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedServiceById(FCServiceManager.RFC, DateUtils.getDateYYYYMMDD("19180501"));
        Assertions.assertTrue (armedService.getName().equals(FCServiceManager.RAF_NAME));
    }

    @Test
    public void testGetRAFFromRNASArmedServiceByIdTest() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedServiceById(FCServiceManager.RNAS, DateUtils.getDateYYYYMMDD("19180501"));
        Assertions.assertTrue (armedService.getName().equals(FCServiceManager.RAF_NAME));
    }

    @Test
    public void testGetArmedService() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        ArmedService armedService = armedServiceManager.getArmedService(FCServiceManager.LAVIATION_MILITAIRE);
        Assertions.assertTrue (armedService.getName().equals(FCServiceManager.LAVIATION_MILITAIRE_NAME));
    }

    @Test
    public void testGetAllArmedService() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        List<ArmedService> armedServices = armedServiceManager.getAllArmedServices();
        Assertions.assertTrue (armedServices.size() == 7);
    }

    @Test
    public void testGetAxisArmedService() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        List<ArmedService> armedServices = armedServiceManager.getAxisServices(DateUtils.getDateYYYYMMDD("19170801"));
        Assertions.assertTrue (armedServices.size() == 1);
    }

    @Test
    public void testGetAlliedArmedService() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        List<ArmedService> armedServices = armedServiceManager.getAlliedServices(DateUtils.getDateYYYYMMDD("19170801"));
        Assertions.assertTrue (armedServices.size() == 5);
    }

    @Test
    public void testGetAlliedArmedServicePostRaF() throws PWCGException
    {
        IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
        List<ArmedService> armedServices = armedServiceManager.getAlliedServices(DateUtils.getDateYYYYMMDD("19181001"));
        Assertions.assertTrue (armedServices.size() == 4);
    }

}
