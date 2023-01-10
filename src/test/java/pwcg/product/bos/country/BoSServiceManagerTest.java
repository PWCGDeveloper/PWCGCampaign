package pwcg.product.bos.country;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pwcg.campaign.ArmedService;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;

public class BoSServiceManagerTest
{

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void testVehicleCreation() throws PWCGException
    {
        for (ArmedService service : BoSServiceManager.getInstance().getAllArmedServices())
        {
            Assertions.assertTrue (service.getMapsForService().size() > 0);
        }
        
        ArmedService usaaf = BoSServiceManager.getInstance().getArmedService(BoSServiceManager.USAAF);
        Assertions.assertTrue (usaaf.getMapsForService().size() == 2);
        
        ArmedService vvs = BoSServiceManager.getInstance().getArmedService(BoSServiceManager.VVS);
        Assertions.assertTrue (vvs.getMapsForService().size() == 5);
        
        ArmedService lw = BoSServiceManager.getInstance().getArmedService(BoSServiceManager.LUFTWAFFE);
        Assertions.assertTrue (lw.getMapsForService().size() == 7);
    }
}
