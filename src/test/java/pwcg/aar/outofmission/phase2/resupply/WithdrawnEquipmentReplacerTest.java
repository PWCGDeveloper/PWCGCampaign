package pwcg.aar.outofmission.phase2.resupply;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.campaign.Campaign;
import pwcg.campaign.PlaneMarkingManagerFactory;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneEquipmentFactory;
import pwcg.campaign.resupply.equipment.WithdrawnEquipmentReplacer;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class WithdrawnEquipmentReplacerTest
{
    @Mock
    private Campaign campaign;
    
    @Mock
    private Company squadron;
    
    private Equipment equipment = new Equipment();
    private SerialNumber serialNumber = new SerialNumber();
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(squadron.getCompanyId()).thenReturn(20111051);
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
        equipment = new Equipment();
    }

    @Test
    public void testRemovalOfMe109F2() throws PWCGException
    {
        Date campaigndate = DateUtils.getDateYYYYMMDD("19420404");
        Mockito.when(campaign.getDate()).thenReturn(campaigndate);
        Mockito.when(campaign.getPlaneMarkingManager()).thenReturn(PlaneMarkingManagerFactory.buildIPlaneMarkingManager());

        for (int i = 0; i < 6; ++i)
        {
            EquippedTank equippedPlane  = TankEquipmentFactory.makeTankForSquadron(campaign, "bf109f2", 20111051);
            equipment.addEquippedTankToCompany(campaign, 20111051, equippedPlane);
        }
        
        for (int i = 0; i < 8; ++i)
        {
            EquippedTank equippedPlane  = TankEquipmentFactory.makeTankForSquadron(campaign, "bf109f4", 20111051);
            equipment.addEquippedTankToCompany(campaign, 20111051, equippedPlane);
        }

        WithdrawnEquipmentReplacer withdrawnEquipmentReplacer = new WithdrawnEquipmentReplacer(campaign, equipment, squadron);
        int numAdded = withdrawnEquipmentReplacer.replaceWithdrawnEquipment();
        assert(numAdded == 6);
        assert(equipment.getActiveEquippedTanks().size() == 14);
        for (EquippedTank equippedPlane: equipment.getActiveEquippedTanks().values())
        {
            assert(equippedPlane.getType().equals("bf109f4") || equippedPlane.getType().equals("bf109g2"));
        }
    }

    @Test
    public void testKeepingMe109F2() throws PWCGException
    {
        Date campaigndate = DateUtils.getDateYYYYMMDD("19420401");
        Mockito.when(campaign.getDate()).thenReturn(campaigndate);
        Mockito.when(campaign.getPlaneMarkingManager()).thenReturn(PlaneMarkingManagerFactory.buildIPlaneMarkingManager());

        for (int i = 0; i < 6; ++i)
        {
            EquippedTank equippedPlane  = TankEquipmentFactory.makeTankForSquadron(campaign, "bf109f2", 20111051);
            equipment.addEquippedTankToCompany(campaign, 20111051, equippedPlane);
        }
        
        for (int i = 0; i < 8; ++i)
        {
            EquippedTank equippedPlane  = TankEquipmentFactory.makeTankForSquadron(campaign, "bf109f4", 20111051);
            equipment.addEquippedTankToCompany(campaign, 20111051, equippedPlane);
        }

        WithdrawnEquipmentReplacer withdrawnEquipmentReplacer = new WithdrawnEquipmentReplacer(campaign, equipment, squadron);
        int numAdded = withdrawnEquipmentReplacer.replaceWithdrawnEquipment();
        assert(numAdded == 0);
        assert(equipment.getActiveEquippedTanks().size() == 14);
        boolean me109F2Found = false;
        for (EquippedTank equippedPlane: equipment.getActiveEquippedTanks().values())
        {
            if (equippedPlane.getType().equals("bf109f2"))
            {
                me109F2Found = true;
            }
        }
        assert(me109F2Found);
    }

    @Test
    public void testReplaceOnlyMe109F2() throws PWCGException
    {
        Date campaigndate = DateUtils.getDateYYYYMMDD("19420404");
        Mockito.when(campaign.getDate()).thenReturn(campaigndate);
        Mockito.when(campaign.getPlaneMarkingManager()).thenReturn(PlaneMarkingManagerFactory.buildIPlaneMarkingManager());

        for (int i = 0; i < 3; ++i)
        {
            EquippedTank equippedPlane  = TankEquipmentFactory.makeTankForSquadron(campaign, "bf109f2", 20111051);
            equipment.addEquippedTankToCompany(campaign, 20111051, equippedPlane);
        }
        
        for (int i = 0; i < 8; ++i)
        {
            EquippedTank equippedPlane  = TankEquipmentFactory.makeTankForSquadron(campaign, "bf109f4", 20111051);
            equipment.addEquippedTankToCompany(campaign, 20111051, equippedPlane);
        }

        assert(equipment.getActiveEquippedTanks().size() == 11);

        WithdrawnEquipmentReplacer withdrawnEquipmentReplacer = new WithdrawnEquipmentReplacer(campaign, equipment, squadron);
        int numAdded = withdrawnEquipmentReplacer.replaceWithdrawnEquipment();
        assert(numAdded == 3);
        assert(equipment.getActiveEquippedTanks().size() == 11);
        for (EquippedTank equippedPlane: equipment.getActiveEquippedTanks().values())
        {
            assert(equippedPlane.getType().equals("bf109f4") || equippedPlane.getType().equals("bf109g2"));
        }

        for (EquippedTank equippedPlane: equipment.getRecentlyInactiveEquippedTanks(campaign.getDate()).values())
        {
            assert(equippedPlane.getType().equals("bf109f2"));
        }
    }

    @Test
    public void testKeepMe109F2BecauseItIsARequestEquipment() throws PWCGException
    {
        Date campaigndate = DateUtils.getDateYYYYMMDD("19420404");
        Mockito.when(campaign.getDate()).thenReturn(campaigndate);
        Mockito.when(campaign.getPlaneMarkingManager()).thenReturn(PlaneMarkingManagerFactory.buildIPlaneMarkingManager());

        for (int i = 0; i < 3; ++i)
        {
            EquippedTank equippedPlane  = TankEquipmentFactory.makeTankForSquadron(campaign, "bf109f2", 20111051);
            equippedPlane.setEquipmentRequest(true);
            equipment.addEquippedTankToCompany(campaign, 20111051, equippedPlane);
        }
        
        for (int i = 0; i < 8; ++i)
        {
            EquippedTank equippedPlane  = TankEquipmentFactory.makeTankForSquadron(campaign, "bf109f4", 20111051);
            equipment.addEquippedTankToCompany(campaign, 20111051, equippedPlane);
        }

        assert(equipment.getActiveEquippedTanks().size() == 11);

        WithdrawnEquipmentReplacer withdrawnEquipmentReplacer = new WithdrawnEquipmentReplacer(campaign, equipment, squadron);
        int numAdded = withdrawnEquipmentReplacer.replaceWithdrawnEquipment();
        assert(numAdded == 0);
        assert(equipment.getActiveEquippedTanks().size() == 11);
        
        int bf109F2Found = 0;
        for (EquippedTank equippedPlane: equipment.getActiveEquippedTanks().values())
        {
            assert(equippedPlane.getType().equals("bf109f2") || equippedPlane.getType().equals("bf109f4") || equippedPlane.getType().equals("bf109g2"));
            if (equippedPlane.getType().equals("bf109f2"))
            {
                ++bf109F2Found;
            }
        }
        assert(bf109F2Found == 3);
    }

    @Test
    public void testAddExtraMe109F4() throws PWCGException
    {
        Date campaigndate = DateUtils.getDateYYYYMMDD("19420404");
        Mockito.when(campaign.getDate()).thenReturn(campaigndate);
        Mockito.when(campaign.getPlaneMarkingManager()).thenReturn(PlaneMarkingManagerFactory.buildIPlaneMarkingManager());

        for (int i = 0; i < 3; ++i)
        {
            EquippedTank equippedPlane  = TankEquipmentFactory.makeTankForSquadron(campaign, "bf109f2", 20111051);
            equipment.addEquippedTankToCompany(campaign, 20111051, equippedPlane);
        }
        
        for (int i = 0; i < 3; ++i)
        {
            EquippedTank equippedPlane  = TankEquipmentFactory.makeTankForSquadron(campaign, "bf109f4", 20111051);
            equipment.addEquippedTankToCompany(campaign, 20111051, equippedPlane);
        }

        WithdrawnEquipmentReplacer withdrawnEquipmentReplacer = new WithdrawnEquipmentReplacer(campaign, equipment, squadron);
        int numAdded = withdrawnEquipmentReplacer.replaceWithdrawnEquipment();
        assert(numAdded == 7);
        assert(equipment.getActiveEquippedTanks().size() == 10);
        for (EquippedTank equippedPlane: equipment.getActiveEquippedTanks().values())
        {
            assert(equippedPlane.getType().equals("bf109f4") || equippedPlane.getType().equals("bf109g2"));
        }
    }
}
