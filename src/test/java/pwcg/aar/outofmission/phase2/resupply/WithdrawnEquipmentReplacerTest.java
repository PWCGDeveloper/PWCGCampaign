package pwcg.aar.outofmission.phase2.resupply;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneEquipmentFactory;
import pwcg.campaign.resupply.equipment.WithdrawnEquipmentReplacer;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class WithdrawnEquipmentReplacerTest
{
    @Mock
    private Campaign campaign;
    
    @Mock
    private Squadron squadron;
    
    private Equipment equipment = new Equipment();
    private SerialNumber serialNumber = new SerialNumber();
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(squadron.getSquadronId()).thenReturn(20111051);
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
        equipment = new Equipment();
    }

    @Test
    public void testRemovalOfMe109F2() throws PWCGException
    {
        Date campaigndate = DateUtils.getDateYYYYMMDD("19420404");
        Mockito.when(campaign.getDate()).thenReturn(campaigndate);

        PlaneEquipmentFactory planeEquipmentFactory = new PlaneEquipmentFactory(campaign);
        for (int i = 0; i < 6; ++i)
        {
            EquippedPlane equippedPlane  = planeEquipmentFactory.makePlaneForSquadron("bf109f2", 20111051);
            equipment.addEquippedPlane(equippedPlane);
        }
        
        for (int i = 0; i < 8; ++i)
        {
            EquippedPlane equippedPlane  = planeEquipmentFactory.makePlaneForSquadron("bf109f4", 20111051);
            equipment.addEquippedPlane(equippedPlane);
        }

        WithdrawnEquipmentReplacer withdrawnEquipmentReplacer = new WithdrawnEquipmentReplacer(campaign, equipment, squadron);
        int numAdded = withdrawnEquipmentReplacer.replaceWithdrawnEquipment();
        assert(numAdded == 6);
        assert(equipment.getActiveEquippedPlanes().size() == 14);
        for (EquippedPlane equippedPlane: equipment.getActiveEquippedPlanes().values())
        {
            assert(equippedPlane.getType().equals("bf109f4") || equippedPlane.getType().equals("bf109g2"));
        }
    }

    @Test
    public void testKeepingMe109F2() throws PWCGException
    {
        Date campaigndate = DateUtils.getDateYYYYMMDD("19420401");
        Mockito.when(campaign.getDate()).thenReturn(campaigndate);

        PlaneEquipmentFactory planeEquipmentFactory = new PlaneEquipmentFactory(campaign);
        for (int i = 0; i < 6; ++i)
        {
            EquippedPlane equippedPlane  = planeEquipmentFactory.makePlaneForSquadron("bf109f2", 20111051);
            equipment.addEquippedPlane(equippedPlane);
        }
        
        for (int i = 0; i < 8; ++i)
        {
            EquippedPlane equippedPlane  = planeEquipmentFactory.makePlaneForSquadron("bf109f4", 20111051);
            equipment.addEquippedPlane(equippedPlane);
        }

        WithdrawnEquipmentReplacer withdrawnEquipmentReplacer = new WithdrawnEquipmentReplacer(campaign, equipment, squadron);
        int numAdded = withdrawnEquipmentReplacer.replaceWithdrawnEquipment();
        assert(numAdded == 0);
        assert(equipment.getActiveEquippedPlanes().size() == 14);
        boolean me109F2Found = false;
        for (EquippedPlane equippedPlane: equipment.getActiveEquippedPlanes().values())
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

        PlaneEquipmentFactory planeEquipmentFactory = new PlaneEquipmentFactory(campaign);
        for (int i = 0; i < 3; ++i)
        {
            EquippedPlane equippedPlane  = planeEquipmentFactory.makePlaneForSquadron("bf109f2", 20111051);
            equipment.addEquippedPlane(equippedPlane);
        }
        
        for (int i = 0; i < 8; ++i)
        {
            EquippedPlane equippedPlane  = planeEquipmentFactory.makePlaneForSquadron("bf109f4", 20111051);
            equipment.addEquippedPlane(equippedPlane);
        }

        assert(equipment.getActiveEquippedPlanes().size() == 11);

        WithdrawnEquipmentReplacer withdrawnEquipmentReplacer = new WithdrawnEquipmentReplacer(campaign, equipment, squadron);
        int numAdded = withdrawnEquipmentReplacer.replaceWithdrawnEquipment();
        assert(numAdded == 3);
        assert(equipment.getActiveEquippedPlanes().size() == 11);
        for (EquippedPlane equippedPlane: equipment.getActiveEquippedPlanes().values())
        {
            assert(equippedPlane.getType().equals("bf109f4") || equippedPlane.getType().equals("bf109g2"));
        }

        for (EquippedPlane equippedPlane: equipment.getRecentlyInactiveEquippedPlanes(campaign.getDate()).values())
        {
            assert(equippedPlane.getType().equals("bf109f2"));
        }
    }

    @Test
    public void testAddExtraMe109F4() throws PWCGException
    {
        Date campaigndate = DateUtils.getDateYYYYMMDD("19420404");
        Mockito.when(campaign.getDate()).thenReturn(campaigndate);

        PlaneEquipmentFactory planeEquipmentFactory = new PlaneEquipmentFactory(campaign);
        for (int i = 0; i < 3; ++i)
        {
            EquippedPlane equippedPlane  = planeEquipmentFactory.makePlaneForSquadron("bf109f2", 20111051);
            equipment.addEquippedPlane(equippedPlane);
        }
        
        for (int i = 0; i < 3; ++i)
        {
            EquippedPlane equippedPlane  = planeEquipmentFactory.makePlaneForSquadron("bf109f4", 20111051);
            equipment.addEquippedPlane(equippedPlane);
        }

        WithdrawnEquipmentReplacer withdrawnEquipmentReplacer = new WithdrawnEquipmentReplacer(campaign, equipment, squadron);
        int numAdded = withdrawnEquipmentReplacer.replaceWithdrawnEquipment();
        assert(numAdded == 7);
        assert(equipment.getActiveEquippedPlanes().size() == 10);
        for (EquippedPlane equippedPlane: equipment.getActiveEquippedPlanes().values())
        {
            assert(equippedPlane.getType().equals("bf109f4") || equippedPlane.getType().equals("bf109g2"));
        }
    }
}
