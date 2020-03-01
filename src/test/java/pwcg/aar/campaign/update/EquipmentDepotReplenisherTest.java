package pwcg.aar.campaign.update;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneArchType;
import pwcg.campaign.plane.SquadronPlaneAssignment;
import pwcg.campaign.resupply.depot.EquipmentDepot;
import pwcg.campaign.resupply.depot.EquipmentDepotReplenisher;
import pwcg.campaign.resupply.depot.EquipmentReplacementUtils;
import pwcg.campaign.resupply.depot.EquipmentUpgradeRecord;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.country.BoSServiceManager;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentDepotReplenisherTest 
{
	private Campaign campaign;
	
    @Mock EquippedPlane me109E7;
    @Mock EquippedPlane me109K4;
	
    @Before
    public void setup() throws PWCGException
    {
    	PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaignForceCreation(SquadronTestProfile.KG53_PROFILE);
        
        Mockito.when(me109E7.getArchType()).thenReturn("bf109");
        Mockito.when(me109E7.getGoodness()).thenReturn(10);
        Mockito.when(me109K4.getArchType()).thenReturn("bf109");
        Mockito.when(me109K4.getGoodness()).thenReturn(95);
    }
    
    @Test
    public void testArchTypesInProductionForServiceLife() throws PWCGException 
    {
        SquadronManager squadronmanager = PWCGContext.getInstance().getSquadronManager();
        for (Squadron squadron: squadronmanager.getAllSquadrons())
        {
            for (SquadronPlaneAssignment planeAssignment: squadron.getPlaneAssignments())
            {
                PlaneArchType planeArchType = PWCGContext.getInstance().getPlaneTypeFactory().getPlaneArchType(planeAssignment.getArchType());
                String selectedPlaneType = EquipmentReplacementUtils.getTypeForReplacement(planeAssignment.getSquadronWithdrawal(), planeArchType);
                assert(selectedPlaneType.length() > 0);
            }
        }
    }

    @Test
    public void testUpdateWithReplacementsGermans() throws PWCGException 
    {
        Map<Integer, Integer> replacementsAvailableBefore = determineReplacementsAvailableByService();
        EquipmentDepotReplenisher equipmentReplacementUpdater  = new EquipmentDepotReplenisher(campaign);
        equipmentReplacementUpdater.replenishDepotsForServices();
        Map<Integer, Integer> replacementsAvailableAfter = determineReplacementsAvailableByService();
        validateReplacements(replacementsAvailableBefore, replacementsAvailableAfter);
    }

    @Test
    public void testUpgradeWithWorsePlane() throws PWCGException 
    {
        EquipmentDepot equipmentDepot = campaign.getEquipmentManager().getEquipmentDepotForService(BoSServiceManager.LUFTWAFFE);
        EquipmentUpgradeRecord upgradeRecord = equipmentDepot.getUpgrade(me109E7);
        assert(upgradeRecord != null);
        assert(upgradeRecord.getUpgrade().getArchType().equals("bf109"));
        assert(upgradeRecord.getUpgrade().getGoodness() > 10);
        
        int upgradeSerialNumber = upgradeRecord.getUpgrade().getSerialNumber();
        
        EquippedPlane planeInDepot = equipmentDepot.getPlaneFromDepot(upgradeSerialNumber);
        assert(planeInDepot != null);

        planeInDepot = equipmentDepot.removeEquippedPlaneFromDepot(upgradeSerialNumber);
        assert(planeInDepot != null);

        planeInDepot = equipmentDepot.getPlaneFromDepot(upgradeSerialNumber);
        assert(planeInDepot == null);
    }

    @Test
    public void testUpgradeWithBetterPlane() throws PWCGException 
    {
        EquipmentDepot equipmentDepo = campaign.getEquipmentManager().getEquipmentDepotForService(BoSServiceManager.LUFTWAFFE);
        EquipmentUpgradeRecord replacementPlane = equipmentDepo.getUpgrade(me109K4);
        assert(replacementPlane == null);
    }

    private Map<Integer, Integer> determineReplacementsAvailableByService()
    {
        Map<Integer, Integer> replacementsAvailable = new HashMap<>();
        for (Integer serviceId : campaign.getEquipmentManager().getServiceIdsForDepots())
        {
            EquipmentDepot equipmentDepot = campaign.getEquipmentManager().getEquipmentDepotForService(serviceId);
            replacementsAvailable.put(serviceId, equipmentDepot.getDepotSize());
        }
        return replacementsAvailable;
    }
    
    private void validateReplacements(Map<Integer, Integer> replacementsAvailableBefore, Map<Integer, Integer> replacementsAvailableAfter) throws PWCGException
    {
        for (Integer serviceId : replacementsAvailableBefore.keySet())
        {
            assert(replacementsAvailableAfter.get(serviceId) >= replacementsAvailableBefore.get(serviceId));
        }
    }
}
