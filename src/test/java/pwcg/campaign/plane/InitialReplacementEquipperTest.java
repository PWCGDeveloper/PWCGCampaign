package pwcg.campaign.plane;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.resupply.depot.EquipmentDepotInitializer;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class InitialReplacementEquipperTest
{
    @Mock private Campaign campaign;
    
    private SerialNumber serialNumber = new SerialNumber();
    
    public InitialReplacementEquipperTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void testEquipGermanReplacementsEarly() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420501"));
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
        Mockito.when(campaign.getCampaignMap()).thenReturn(FrontMapIdentifier.STALINGRAD_MAP);
        PWCGContext.getInstance().setCampaign(campaign);
        
        IArmedServiceManager serviceManager = ArmedServiceFactory.createServiceManager();
        ArmedService luftwaffe = serviceManager.getArmedService(20101);
        
        EquipmentDepotInitializer replacementEquipper = new EquipmentDepotInitializer(campaign, luftwaffe);
        Equipment equipment = replacementEquipper.createReplacementPoolForService();
        assert(equipment.getAvailableDepotTanks().size() == 15);
        
        boolean me110e2Found = false;
        boolean he111h6Found = false;
        boolean me109f4Found = false;
        boolean ju87d3Found = false;
        boolean ju52Found = false;
        
        for (EquippedTank replacementPlane : equipment.getAvailableDepotTanks().values())
        {
            if (replacementPlane.getType().equals("bf110e2"))
            {
                me110e2Found = true;
            }
            else if (replacementPlane.getType().equals("he111h6"))
            {
                he111h6Found = true;
            }
            else if (replacementPlane.getType().equals("bf109f4"))
            {
                me109f4Found = true;
            }
            else if (replacementPlane.getType().equals("ju87d3"))
            {
                ju87d3Found = true;
            }
            else if (replacementPlane.getType().equals("ju523mg4e"))
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
    public void testEquipGermanReplacementsMid() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430201"));
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
        Mockito.when(campaign.getCampaignMap()).thenReturn(FrontMapIdentifier.STALINGRAD_MAP);
        PWCGContext.getInstance().setCampaign(campaign);

        IArmedServiceManager serviceManager = ArmedServiceFactory.createServiceManager();
        ArmedService luftwaffe = serviceManager.getArmedService(20101);
        
        EquipmentDepotInitializer replacementEquipper = new EquipmentDepotInitializer(campaign, luftwaffe);
        Equipment equipment = replacementEquipper.createReplacementPoolForService();
        assert(equipment.getAvailableDepotTanks().size() == 17);
        
        boolean me110g2Found = false;
        boolean he111h6Found = false;
        boolean he111h16Found = false;
        boolean me109G2Found = false;
        boolean ju87d3Found = false;
        boolean ju52Found = false;
        
        for (EquippedTank replacementPlane : equipment.getAvailableDepotTanks().values())
        {
            if (replacementPlane.getType().equals("bf110g2"))
            {
                me110g2Found = true;
            }
            else if (replacementPlane.getType().equals("he111h6"))
            {
                he111h6Found = true;
            }
            else if (replacementPlane.getType().equals("he111h16"))
            {
                he111h6Found = true;
            }
            else if (replacementPlane.getType().equals("bf109g2"))
            {
                me109G2Found = true;
            }
            else if (replacementPlane.getType().equals("ju87d3"))
            {
                ju87d3Found = true;
            }
            else if (replacementPlane.getType().equals("ju523mg4e"))
            {
                ju52Found = true;
            }
        }
        
        assert(me110g2Found);
        assert(he111h6Found || he111h16Found);
        assert(me109G2Found);
        assert(ju87d3Found);
        assert(ju52Found);
    }

    @Test
    public void testEquipRussianReplacementsEarly() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420101"));
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
        
        IArmedServiceManager serviceManager = ArmedServiceFactory.createServiceManager();
        ArmedService vvs = serviceManager.getArmedService(10101);
        
        EquipmentDepotInitializer replacementEquipper = new EquipmentDepotInitializer(campaign, vvs);
        Equipment equipment = replacementEquipper.createReplacementPoolForService();
        assert(equipment.getAvailableDepotTanks().size() == 90);
        
        boolean c47 = false;
        boolean hurricane = false;
        boolean i16Found = false;
        boolean mig3 = false;
        boolean lagg3 = false;
        boolean p40 = false;
        boolean pe2Found = false;
        boolean il2Found = false;

        for (EquippedTank replacementPlane : equipment.getAvailableDepotTanks().values())
        {
            if (replacementPlane.getType().equals("i16t24"))
            {
                i16Found = true;
            }
            else if (replacementPlane.getType().equals("c47a"))
            {
                c47 = true;
            }
            else if (replacementPlane.getType().equals("hurricanemkii"))
            {
                hurricane = true;
            }
            else if (replacementPlane.getType().equals("mig3s24"))
            {
                mig3 = true;
            }
            else if (replacementPlane.getType().equals("lagg3s29"))
            {
                lagg3 = true;
            }
            else if (replacementPlane.getType().equals("p40e1"))
            {
                p40 = true;
            }
            else if (replacementPlane.getType().equals("pe2s35"))
            {
                pe2Found = true;
            }
            else if (replacementPlane.getType().equals("il2m41"))
            {
                il2Found = true;
            }
        }
        
        assert(hurricane);
        assert(c47);
        assert(i16Found);
        assert(mig3);
        assert(lagg3);
        assert(p40);
        assert(pe2Found);
        assert(il2Found);
    }

    @Test
    public void testEquipRussianReplacementsMid() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430101"));
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
        Mockito.when(campaign.getCampaignMap()).thenReturn(FrontMapIdentifier.STALINGRAD_MAP);
        PWCGContext.getInstance().setCampaign(campaign);

        IArmedServiceManager serviceManager = ArmedServiceFactory.createServiceManager();
        ArmedService vvs = serviceManager.getArmedService(10101);
        
        EquipmentDepotInitializer replacementEquipper = new EquipmentDepotInitializer(campaign, vvs);
        Equipment equipment = replacementEquipper.createReplacementPoolForService();
        assert(equipment.getAvailableDepotTanks().size() == 175);
        
        boolean c47 = false;
        boolean hurricane = false;
        boolean i16Found = false;
        boolean mig3 = false;
        boolean lagg3 = false;
        boolean p40 = false;
        boolean pe2Found = false;
        boolean il2Found = false;
        boolean yak1Found = false;
        
        for (EquippedTank replacementPlane : equipment.getAvailableDepotTanks().values())
        {
            if (replacementPlane.getType().equals("i16t24"))
            {
                i16Found = true;
            }
            else if (replacementPlane.getType().equals("c47a"))
            {
                c47 = true;
            }
            else if (replacementPlane.getType().equals("hurricanemkii"))
            {
                hurricane = true;
            }
            else if (replacementPlane.getType().equals("mig3s24"))
            {
                mig3 = true;
            }
            else if (replacementPlane.getType().equals("lagg3s29"))
            {
                lagg3 = true;
            }
            else if (replacementPlane.getType().equals("p40e1"))
            {
                p40 = true;
            }
            else if (replacementPlane.getType().equals("pe2s87"))
            {
                pe2Found = true;
            }
            else if (replacementPlane.getType().equals("il2m42"))
            {
                il2Found = true;
            }
            else if (replacementPlane.getType().equals("yak1s127"))
            {
                yak1Found = true;
            }
        }
        
        assert(hurricane);
        assert(c47);
        assert(i16Found);
        assert(mig3);
        assert(lagg3);
        assert(p40);
        assert(pe2Found);
        assert(il2Found);
        assert(yak1Found);
    }

    @Test
    public void testEquipItalianReplacements() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430101"));
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
        Mockito.when(campaign.getCampaignMap()).thenReturn(FrontMapIdentifier.STALINGRAD_MAP);
        PWCGContext.getInstance().setCampaign(campaign);
        
        IArmedServiceManager serviceManager = ArmedServiceFactory.createServiceManager();
        ArmedService regiaAeronautica = serviceManager.getArmedService(20202);
        
        EquipmentDepotInitializer replacementEquipper = new EquipmentDepotInitializer(campaign, regiaAeronautica);
        Equipment equipment = replacementEquipper.createReplacementPoolForService();
        assert(equipment.getAvailableDepotTanks().size() == 1);
        
        boolean macchiFound = false;
        
        for (EquippedTank replacementPlane : equipment.getAvailableDepotTanks().values())
        {
            if (replacementPlane.getType().equals("mc202s8"))
            {
                macchiFound = true;
            }
        }
        
        assert(macchiFound);
    }
}
