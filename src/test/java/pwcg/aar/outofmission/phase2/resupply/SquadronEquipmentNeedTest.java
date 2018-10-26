package pwcg.aar.outofmission.phase2.resupply;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignEquipmentManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.resupply.equipment.SquadronEquipmentNeed;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class SquadronEquipmentNeedTest
{
    @Mock private Campaign campaign;
    @Mock private Squadron squadron;
    @Mock private CampaignEquipmentManager campaignEquipmentManager;
    @Mock private Equipment equipment;
    @Mock private AARPersonnelLosses lossesInMissionData;
    @Mock private SquadronPersonnel squadronPersonnel;
    @Mock private EquippedPlane equippedPlane;

    private Map<Integer, EquippedPlane> activeEquippedPlaneCollection = new HashMap<>();
    private Map<Integer, EquippedPlane> inactiveEquippedPlaneCollection = new HashMap<>();
    
    SerialNumber serialNumber = new SerialNumber();
    
    @Before
    public void setup() throws PWCGException
    {
        activeEquippedPlaneCollection.clear();
        inactiveEquippedPlaneCollection.clear();
        
        PWCGContextManager.setRoF(false);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420430"));
        Mockito.when(campaign.getSquadronId()).thenReturn(501011);
        Mockito.when(campaign.getEquipmentManager()).thenReturn(campaignEquipmentManager);
        Mockito.when(campaignEquipmentManager.getEquipmentForSquadron(Matchers.any())).thenReturn(equipment);

        Mockito.when(equipment.getActiveEquippedPlanes()).thenReturn(activeEquippedPlaneCollection);
        Mockito.when(equipment.getRecentlyInactiveEquippedPlanes(Matchers.any())).thenReturn(inactiveEquippedPlaneCollection);
     }

    @Test
    public void testResupplyWithNoEquipment() throws PWCGException
    {
        SquadronEquipmentNeed squadronTransferNeed = new SquadronEquipmentNeed(campaign, squadron);
        squadronTransferNeed.determineResupplyNeeded();
        assert (squadronTransferNeed.needsResupply() == true);
        
        for (int i = 0; i < Squadron.SQUADRON_EQUIPMENT_SIZE - 1; ++i)
        {
            squadronTransferNeed.noteResupply();
            assert (squadronTransferNeed.needsResupply() == true);
        }

        squadronTransferNeed.noteResupply();
        assert (squadronTransferNeed.needsResupply() == false);
    }
    

    @Test
    public void testResupplyWithActiveEquipment() throws PWCGException
    {
        for (int i = 0; i < 11; ++i)
        {
            activeEquippedPlaneCollection.put(serialNumber.getNextPlaneSerialNumber(), equippedPlane);
        }
        
        SquadronEquipmentNeed squadronResupplyNeed = new SquadronEquipmentNeed(campaign, squadron);
        squadronResupplyNeed.determineResupplyNeeded();
        assert (squadronResupplyNeed.needsResupply() == true);
        
        for (int i = 0; i < 2; ++i)
        {
            squadronResupplyNeed.noteResupply();
            assert (squadronResupplyNeed.needsResupply() == true);
        }

        squadronResupplyNeed.noteResupply();
        assert (squadronResupplyNeed.needsResupply() == false);
    }
    

    @Test
    public void testResupplyWithActiveAndInactiveEquipment() throws PWCGException
    {
        for (int i = 0; i < 9; ++i)
        {
            activeEquippedPlaneCollection.put(serialNumber.getNextPlaneSerialNumber(), equippedPlane);
        }
        
        for (int i = 0; i < 2; ++i)
        {
            inactiveEquippedPlaneCollection.put(serialNumber.getNextPlaneSerialNumber(), equippedPlane);
        }

        SquadronEquipmentNeed squadronResupplyNeed = new SquadronEquipmentNeed(campaign, squadron);
        squadronResupplyNeed.determineResupplyNeeded();
        assert (squadronResupplyNeed.needsResupply() == true);
        
        for (int i = 0; i < 2; ++i)
        {
            squadronResupplyNeed.noteResupply();
            assert (squadronResupplyNeed.needsResupply() == true);
        }

        squadronResupplyNeed.noteResupply();
        assert (squadronResupplyNeed.needsResupply() == false);
    }

    @Test
    public void testNoResupplyNeeded() throws PWCGException
    {
        for (int i = 0; i < 10; ++i)
        {
            activeEquippedPlaneCollection.put(serialNumber.getNextPlaneSerialNumber(), equippedPlane);
        }
        
        for (int i = 0; i < 4; ++i)
        {
            inactiveEquippedPlaneCollection.put(serialNumber.getNextPlaneSerialNumber(), equippedPlane);
        }

        SquadronEquipmentNeed squadronResupplyNeed = new SquadronEquipmentNeed(campaign, squadron);
        squadronResupplyNeed.determineResupplyNeeded();
        assert (squadronResupplyNeed.needsResupply() == false);
    }

}
