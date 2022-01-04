package pwcg.aar.campaign.update;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.TankArchType;
import pwcg.campaign.plane.SquadronPlaneAssignment;
import pwcg.campaign.resupply.depot.EquipmentDepot;
import pwcg.campaign.resupply.depot.EquipmentDepotReplenisher;
import pwcg.campaign.resupply.depot.EquipmentReplacementUtils;
import pwcg.campaign.resupply.depot.EquipmentUpgradeRecord;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.country.BoSServiceManager;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EquipmentDepotReplenisherTest
{
    private Campaign campaign;

    @Mock
    EquippedTank me109E7;
    @Mock
    EquippedTank me109K4;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.KG53_PROFILE);
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        Mockito.when(me109E7.getArchType()).thenReturn("bf109");
        Mockito.when(me109E7.getGoodness()).thenReturn(10);
        Mockito.when(me109K4.getArchType()).thenReturn("bf109");
        Mockito.when(me109K4.getGoodness()).thenReturn(95);
    }

    @Test
    public void testArchTypesInProductionForServiceLife() throws PWCGException
    {
        CompanyManager squadronmanager = PWCGContext.getInstance().getCompanyManager();
        for (Company squadron : squadronmanager.getAllCompanies())
        {
            for (CompanyTankAssignment planeAssignment : squadron.getPlaneAssignments())
            {
                TankArchType planeArchType = PWCGContext.getInstance().getTankTypeFactory().getTankArchType(planeAssignment.getArchType());
                String selectedTankType = EquipmentReplacementUtils.getTypeForReplacement(planeAssignment.getCompanyWithdrawal(), planeArchType);
                Assertions.assertTrue (selectedTankType.length() > 0);
            }
        }
    }

    @Test
    public void testUpdateWithReplacementsGermans() throws PWCGException
    {
        Map<Integer, Integer> replacementsAvailableBefore = determineReplacementsAvailableByService();
        EquipmentDepotReplenisher equipmentReplacementUpdater = new EquipmentDepotReplenisher(campaign);
        equipmentReplacementUpdater.replenishDepotsForServices();
        Map<Integer, Integer> replacementsAvailableAfter = determineReplacementsAvailableByService();
        validateReplacements(replacementsAvailableBefore, replacementsAvailableAfter);
    }

    @Test
    public void testUpgradeWithWorsePlane() throws PWCGException
    {
        EquipmentDepot equipmentDepot = campaign.getEquipmentManager().getEquipmentDepotForService(BoSServiceManager.LUFTWAFFE);
        EquipmentUpgradeRecord upgradeRecord = equipmentDepot.getUpgrade(me109E7);
        Assertions.assertTrue (upgradeRecord != null);
        Assertions.assertTrue (upgradeRecord.getUpgrade().getArchType().equals("bf109"));
        Assertions.assertTrue (upgradeRecord.getUpgrade().getGoodness() > 10);

        int upgradeSerialNumber = upgradeRecord.getUpgrade().getSerialNumber();

        EquippedTank planeInDepot = equipmentDepot.getPlaneFromDepot(upgradeSerialNumber);
        Assertions.assertTrue (planeInDepot != null);

        planeInDepot = equipmentDepot.removeEquippedPlaneFromDepot(upgradeSerialNumber);
        Assertions.assertTrue (planeInDepot != null);

        planeInDepot = equipmentDepot.getPlaneFromDepot(upgradeSerialNumber);
        Assertions.assertTrue (planeInDepot == null);
    }

    @Test
    public void testUpgradeWithBetterPlane() throws PWCGException
    {
        EquipmentDepot equipmentDepo = campaign.getEquipmentManager().getEquipmentDepotForService(BoSServiceManager.LUFTWAFFE);
        EquipmentUpgradeRecord replacementPlane = equipmentDepo.getUpgrade(me109K4);
        Assertions.assertTrue (replacementPlane == null);
    }

    private Map<Integer, Integer> determineReplacementsAvailableByService() throws PWCGException
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
            Assertions.assertTrue (replacementsAvailableAfter.get(serviceId) >= replacementsAvailableBefore.get(serviceId));
        }
    }
}
