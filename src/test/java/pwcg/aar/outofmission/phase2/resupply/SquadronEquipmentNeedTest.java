package pwcg.aar.outofmission.phase2.resupply;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignEquipmentManager;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.resupply.equipment.SquadronEquipmentNeed;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class SquadronEquipmentNeedTest
{
    @Mock private Campaign campaign;
    @Mock private Company squadron;
    @Mock private CampaignEquipmentManager campaignEquipmentManager;
    @Mock private Equipment equipment;
    @Mock private AARPersonnelLosses lossesInMissionData;
    @Mock private CompanyPersonnel squadronPersonnel;
    @Mock private EquippedTank equippedPlane;

    private Map<Integer, EquippedTank> activeEquippedPlaneCollection = new HashMap<>();
    private Map<Integer, EquippedTank> inactiveEquippedPlaneCollection = new HashMap<>();
    
    SerialNumber serialNumber = new SerialNumber();
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        activeEquippedPlaneCollection.clear();
        inactiveEquippedPlaneCollection.clear();
        
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420430"));
        Mockito.when(campaign.getEquipmentManager()).thenReturn(campaignEquipmentManager);
        Mockito.when(campaignEquipmentManager.getEquipmentForCompany(ArgumentMatchers.any())).thenReturn(equipment);

        Mockito.when(equipment.getActiveEquippedTanks()).thenReturn(activeEquippedPlaneCollection);
        Mockito.when(equipment.getRecentlyInactiveEquippedTanks(ArgumentMatchers.any())).thenReturn(inactiveEquippedPlaneCollection);
     }

    @Test
    public void testResupplyWithNoEquipment() throws PWCGException
    {
        SquadronEquipmentNeed squadronTransferNeed = new SquadronEquipmentNeed(campaign, squadron);
        squadronTransferNeed.determineResupplyNeeded();
        Assertions.assertTrue (squadronTransferNeed.needsResupply() == true);
        
        for (int i = 0; i < Company.COMPANY_EQUIPMENT_SIZE - 1; ++i)
        {
            squadronTransferNeed.noteResupply();
            Assertions.assertTrue (squadronTransferNeed.needsResupply() == true);
        }

        squadronTransferNeed.noteResupply();
        Assertions.assertTrue (squadronTransferNeed.needsResupply() == false);
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
        Assertions.assertTrue (squadronResupplyNeed.needsResupply() == true);
        
        for (int i = 0; i < 2; ++i)
        {
            squadronResupplyNeed.noteResupply();
            Assertions.assertTrue (squadronResupplyNeed.needsResupply() == true);
        }

        squadronResupplyNeed.noteResupply();
        Assertions.assertTrue (squadronResupplyNeed.needsResupply() == false);
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
        Assertions.assertTrue (squadronResupplyNeed.needsResupply() == true);
        
        for (int i = 0; i < 2; ++i)
        {
            squadronResupplyNeed.noteResupply();
            Assertions.assertTrue (squadronResupplyNeed.needsResupply() == true);
        }

        squadronResupplyNeed.noteResupply();
        Assertions.assertTrue (squadronResupplyNeed.needsResupply() == false);
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
        Assertions.assertTrue (squadronResupplyNeed.needsResupply() == false);
    }

}
