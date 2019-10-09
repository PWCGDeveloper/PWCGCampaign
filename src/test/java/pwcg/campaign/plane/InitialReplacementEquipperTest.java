package pwcg.campaign.plane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.resupply.depo.EquipmentDepoInitializer;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class InitialReplacementEquipperTest
{
    @Mock private Campaign campaign;
    
    private SerialNumber serialNumber = new SerialNumber();
    
    @Before 
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);      
    }

    @Test
    public void testEquipGermanReplacements() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420501"));
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
        
        IArmedServiceManager serviceManager = ArmedServiceFactory.createServiceManager();
        ArmedService luftwaffe = serviceManager.getArmedService(20101);
        
        EquipmentDepoInitializer replacementEquipper = new EquipmentDepoInitializer(campaign, luftwaffe);
        Equipment equipment = replacementEquipper.createReplacementPoolForService();
        assert(equipment.getAvailableDepoPlanes().size() == 45);
        
        boolean me110e2Found = false;
        boolean he111h6Found = false;
        boolean me109f4Found = false;
        boolean ju87d3Found = false;
        boolean ju52Found = false;
        
        for (EquippedPlane replacementPlane : equipment.getAvailableDepoPlanes().values())
        {
            if (replacementPlane.getType().equals("bf110e2"))
            {
                me110e2Found = true;
            }
            if (replacementPlane.getType().equals("he111h6"))
            {
                he111h6Found = true;
            }
            if (replacementPlane.getType().equals("bf109f4"))
            {
                me109f4Found = true;
            }
            if (replacementPlane.getType().equals("ju87d3"))
            {
                ju87d3Found = true;
            }
            if (replacementPlane.getType().equals("ju523mg4e"))
            {
                ju52Found = true;
            }
        }
        
        assert(me110e2Found);
        assert(he111h6Found);
        assert(me109f4Found);
        assert(ju87d3Found);
        assert(ju52Found);
    }

    @Test
    public void testEquipRussianReplacements() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420101"));
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
        
        IArmedServiceManager serviceManager = ArmedServiceFactory.createServiceManager();
        ArmedService vvs = serviceManager.getArmedService(10101);
        
        EquipmentDepoInitializer replacementEquipper = new EquipmentDepoInitializer(campaign, vvs);
        Equipment equipment = replacementEquipper.createReplacementPoolForService();
        assert(equipment.getAvailableDepoPlanes().size() == 112);
        
        boolean i16Found = false;
        boolean mig3 = false;
        boolean lagg3 = false;
        boolean p40 = false;
        boolean pe2Found = false;
        boolean il2Found = false;
        
        for (EquippedPlane replacementPlane : equipment.getAvailableDepoPlanes().values())
        {
            if (replacementPlane.getType().equals("i16t24"))
            {
                i16Found = true;
            }
            if (replacementPlane.getType().equals("mig3s24"))
            {
                mig3 = true;
            }
            if (replacementPlane.getType().equals("lagg3s29"))
            {
                lagg3 = true;
            }
            if (replacementPlane.getType().equals("p40e1"))
            {
                p40 = true;
            }
            if (replacementPlane.getType().equals("pe2s35"))
            {
                pe2Found = true;
            }
            if (replacementPlane.getType().equals("il2m41"))
            {
                il2Found = true;
            }
        }
        
        assert(i16Found);
        assert(mig3);
        assert(lagg3);
        assert(p40);
        assert(pe2Found);
        assert(il2Found);
    }

    @Test
    public void testEquipItalianReplacements() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430101"));
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
        
        IArmedServiceManager serviceManager = ArmedServiceFactory.createServiceManager();
        ArmedService regiaAeronautica = serviceManager.getArmedService(20202);
        
        EquipmentDepoInitializer replacementEquipper = new EquipmentDepoInitializer(campaign, regiaAeronautica);
        Equipment equipment = replacementEquipper.createReplacementPoolForService();
        assert(equipment.getAvailableDepoPlanes().size() == 1);
        
        boolean macchiFound = false;
        
        for (EquippedPlane replacementPlane : equipment.getAvailableDepoPlanes().values())
        {
            if (replacementPlane.getType().equals("mc202s8"))
            {
                macchiFound = true;
            }
        }
        
        assert(macchiFound);
    }
}
