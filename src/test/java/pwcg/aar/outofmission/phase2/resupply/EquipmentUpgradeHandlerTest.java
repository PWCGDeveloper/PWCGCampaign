package pwcg.aar.outofmission.phase2.resupply;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.campaign.plane.TankType;
import pwcg.campaign.plane.TankTypeFactory;
import pwcg.campaign.resupply.depot.EquipmentDepot;
import pwcg.campaign.resupply.equipment.EquipmentUpgradeHandler;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EquipmentUpgradeHandlerTest
{
    private Campaign campaign;

    @Mock
    private ArmedService armedService;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        Mockito.when(armedService.getServiceId()).thenReturn(20101);
    }

    @Test
    public void testEquipmentUpgradeForPlayer() throws PWCGException
    {
        TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        EquipmentDepot equipmentDepotBeforeTest = campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());

        // Add good planes to the depo
        List<Integer> veryGoodPlanesInDepot = new ArrayList<>();
        for (int i = 0; i < 2; ++i)
        {
            TankType planeTypeMe109K4 = planeTypeFactory.createTankTypeByType("bf109k4");
            EquippedTank me109K4ForDepot = new EquippedTank(planeTypeMe109K4, campaign.getSerialNumber().getNextPlaneSerialNumber(), -1,
                    TankStatus.STATUS_DEPOT);
            equipmentDepotBeforeTest.addPlaneToDepot(me109K4ForDepot);
            veryGoodPlanesInDepot.add(me109K4ForDepot.getSerialNumber());
        }

        for (int veryGoodPlaneInDepot : veryGoodPlanesInDepot)
        {
            Assertions.assertTrue (equipmentDepotBeforeTest.getPlaneFromDepot(veryGoodPlaneInDepot) != null);
        }

        // replace planes in squadron with different quality 109s, but all worse
        // than the very good planes in the depot
        Company playerSquadron = campaign.determinePlayerCompanies().get(0);
        Equipment equipmentForSquadronBeforeTest = campaign.getEquipmentManager().getEquipmentForCompany(playerSquadron.getCompanyId());
        for (EquippedTank planeInSquadronBeforeTest : equipmentForSquadronBeforeTest.getActiveEquippedTanks().values())
        {
            equipmentForSquadronBeforeTest.removeEquippedTank(planeInSquadronBeforeTest.getSerialNumber());
        }

        List<Integer> planesThatShouldBeReplaced = new ArrayList<>();
        for (int i = 0; i < 2; ++i)
        {
            TankType planeTypeMe109F2 = planeTypeFactory.createTankTypeByType("bf109f2");
            EquippedTank me109F2ForSquadron = new EquippedTank(planeTypeMe109F2, campaign.getSerialNumber().getNextPlaneSerialNumber(), -1,
                    TankStatus.STATUS_DEPLOYED);
            equipmentForSquadronBeforeTest.addEquippedTankToCompany(campaign, playerSquadron.getCompanyId(), me109F2ForSquadron);
            planesThatShouldBeReplaced.add(me109F2ForSquadron.getSerialNumber());
        }

        List<Integer> planesThatShouldNotBeReplaced = new ArrayList<>();
        for (int i = 0; i < 10; ++i)
        {
            TankType planeTypeMe109F4 = planeTypeFactory.createTankTypeByType("bf109f4");
            EquippedTank me109F4ForSquadron = new EquippedTank(planeTypeMe109F4, campaign.getSerialNumber().getNextPlaneSerialNumber(), -1,
                    TankStatus.STATUS_DEPLOYED);
            equipmentForSquadronBeforeTest.addEquippedTankToCompany(campaign, playerSquadron.getCompanyId(), me109F4ForSquadron);
            planesThatShouldNotBeReplaced.add(me109F4ForSquadron.getSerialNumber());
        }

        // The better planes should be left in the squadron
        for (int planeThatShouldNotBeReplaced : planesThatShouldNotBeReplaced)
        {
            Assertions.assertTrue (equipmentForSquadronBeforeTest.getEquippedTank(planeThatShouldNotBeReplaced) != null);
        }

        // Run the upgrade
        EquipmentUpgradeHandler equipmentUpgradeHandler = new EquipmentUpgradeHandler(campaign);
        equipmentUpgradeHandler.upgradeEquipment(armedService);

        // The worst planes in the squadrons should be replaced and now be in
        // the depot
        EquipmentDepot equipmentDepotAfterTest = campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());
        Equipment equipmentForSquadronAfterTest = campaign.getEquipmentManager().getEquipmentForCompany(playerSquadron.getCompanyId());
        for (int planeThatShouldBeReplaced : planesThatShouldBeReplaced)
        {
            Assertions.assertTrue (equipmentForSquadronAfterTest.getEquippedTank(planeThatShouldBeReplaced) == null);
            Assertions.assertTrue (equipmentDepotAfterTest.getPlaneFromDepot(planeThatShouldBeReplaced) != null);
        }

        // The better planes should be left in the squadron
        for (int planeThatShouldNotBeReplaced : planesThatShouldNotBeReplaced)
        {
            Assertions.assertTrue (equipmentForSquadronAfterTest.getEquippedTank(planeThatShouldNotBeReplaced) != null);
            Assertions.assertTrue (equipmentDepotAfterTest.getPlaneFromDepot(planeThatShouldNotBeReplaced) == null);
        }

        // The very good planes in the depot should be moved from the depot to
        // the squadron
        for (int veryGoodPlaneInDepot : veryGoodPlanesInDepot)
        {
            Assertions.assertTrue (equipmentForSquadronAfterTest.getEquippedTank(veryGoodPlaneInDepot) != null);
            Assertions.assertTrue (equipmentDepotAfterTest.getPlaneFromDepot(veryGoodPlaneInDepot) == null);
        }
    }

    @Test
    public void testEquipmentUpgradeForAI() throws PWCGException
    {
        TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        EquipmentDepot equipmentDepotBeforeTest = campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());

        // Add good planes to the depo
        List<Integer> veryGoodPlanesInDepot = new ArrayList<>();
        for (int i = 0; i < 2; ++i)
        {
            TankType planeTypeMe109K4 = planeTypeFactory.createTankTypeByType("bf109k4");
            EquippedTank me109K4ForDepot = new EquippedTank(planeTypeMe109K4, campaign.getSerialNumber().getNextPlaneSerialNumber(), -1,
                    TankStatus.STATUS_DEPOT);
            equipmentDepotBeforeTest.addPlaneToDepot(me109K4ForDepot);
            veryGoodPlanesInDepot.add(me109K4ForDepot.getSerialNumber());
        }

        for (int veryGoodPlaneInDepot : veryGoodPlanesInDepot)
        {
            Assertions.assertTrue (equipmentDepotBeforeTest.getPlaneFromDepot(veryGoodPlaneInDepot) != null);
        }

        // replace planes in player squadron with very good quality 109s, to
        // avoid the need for replacement
        Company playerSquadron = campaign.determinePlayerCompanies().get(0);
        Equipment equipmentForSquadronBeforeTest = campaign.getEquipmentManager().getEquipmentForCompany(playerSquadron.getCompanyId());
        for (EquippedTank planeInSquadronBeforeTest : equipmentForSquadronBeforeTest.getActiveEquippedTanks().values())
        {
            equipmentForSquadronBeforeTest.removeEquippedTank(planeInSquadronBeforeTest.getSerialNumber());
        }

        List<Integer> originalPlayerSquadronPlanes = new ArrayList<>();
        for (int i = 0; i < 16; ++i)
        {
            TankType planeTypeMe109K4 = planeTypeFactory.createTankTypeByType("bf109k4");
            EquippedTank me109F2ForSquadron = new EquippedTank(planeTypeMe109K4, campaign.getSerialNumber().getNextPlaneSerialNumber(), -1,
                    TankStatus.STATUS_DEPLOYED);
            equipmentForSquadronBeforeTest.addEquippedTankToCompany(campaign, playerSquadron.getCompanyId(), me109F2ForSquadron);
            originalPlayerSquadronPlanes.add(me109F2ForSquadron.getSerialNumber());
        }

        // Run the upgrade
        EquipmentUpgradeHandler equipmentUpgradeHandler = new EquipmentUpgradeHandler(campaign);
        equipmentUpgradeHandler.upgradeEquipment(armedService);

        // The planes in the depot should be removed and sent to squadrons
        EquipmentDepot equipmentDepotAfterTest = campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());
        for (int depotPlane : veryGoodPlanesInDepot)
        {
            Assertions.assertTrue (equipmentDepotAfterTest.getPlaneFromDepot(depotPlane) == null);
        }

        // No planes should be replaced in the player squadron
        Equipment equipmentForPlayerSquadronAfterTest = campaign.getEquipmentManager().getEquipmentForCompany(playerSquadron.getCompanyId());
        for (int depotPlane : veryGoodPlanesInDepot)
        {
            Assertions.assertTrue (equipmentForPlayerSquadronAfterTest.getEquippedTank(depotPlane) == null);
        }

        // the planes should be in a squadron
        for (int depotPlaneSerialNumber : veryGoodPlanesInDepot)
        {
            boolean planeIsInSquadron = false;
            for (Equipment squadronEquipment : campaign.getEquipmentManager().getEquipmentAllCompanies().values())
            {
                for (int squadronPlaneSerialNumber : squadronEquipment.getAvailableDepotTanks().keySet())
                {
                    if (depotPlaneSerialNumber == squadronPlaneSerialNumber)
                    {
                        planeIsInSquadron = true;
                    }
                }
            }
            Assertions.assertTrue (planeIsInSquadron);
        }
    }

    @Test
    public void testEquipmentUpgradeNotNeeded() throws PWCGException
    {
        TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();

        // Clear out the depot and replace with a couple of bad planes
        EquipmentDepot equipmentDepotBeforeTest = campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());
        for (EquippedTank planeInSquadronBeforeTest : equipmentDepotBeforeTest.getAllPlanesInDepot())
        {
            equipmentDepotBeforeTest.removeEquippedPlaneFromDepot(planeInSquadronBeforeTest.getSerialNumber());
        }

        List<Integer> badPlanesInDepot = new ArrayList<>();
        for (int i = 0; i < 2; ++i)
        {
            TankType planeTypeMe109F2 = planeTypeFactory.createTankTypeByType("bf109f2");
            EquippedTank me109F2ForDepot = new EquippedTank(planeTypeMe109F2, campaign.getSerialNumber().getNextPlaneSerialNumber(), -1,
                    TankStatus.STATUS_DEPOT);
            equipmentDepotBeforeTest.addPlaneToDepot(me109F2ForDepot);
            badPlanesInDepot.add(me109F2ForDepot.getSerialNumber());
        }

        // Clear out the squadron and add better planes than we have in the
        // depot
        Company playerSquadron = campaign.determinePlayerCompanies().get(0);
        Equipment equipmentForSquadronBeforeTest = campaign.getEquipmentManager().getEquipmentForCompany(playerSquadron.getCompanyId());

        for (EquippedTank planeInSquadronBeforeTest : equipmentForSquadronBeforeTest.getActiveEquippedTanks().values())
        {
            equipmentForSquadronBeforeTest.removeEquippedTank(planeInSquadronBeforeTest.getSerialNumber());
        }

        List<Integer> goodPlanesInTheSquadron = new ArrayList<>();
        for (int i = 0; i < 12; ++i)
        {
            TankType planeTypeMe109K4 = planeTypeFactory.createTankTypeByType("bf109k4");
            EquippedTank me109K4ForSquadron = new EquippedTank(planeTypeMe109K4, campaign.getSerialNumber().getNextPlaneSerialNumber(), -1,
                    TankStatus.STATUS_DEPLOYED);
            equipmentForSquadronBeforeTest.addEquippedTankToCompany(campaign, playerSquadron.getCompanyId(), me109K4ForSquadron);
            goodPlanesInTheSquadron.add(me109K4ForSquadron.getSerialNumber());
        }

        // Run the upgrade
        EquipmentUpgradeHandler equipmentUpgradeHandler = new EquipmentUpgradeHandler(campaign);
        equipmentUpgradeHandler.upgradeEquipment(armedService);

        EquipmentDepot equipmentDepotAfterTest = campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());
        Equipment equipmentForSquadronAfterTest = campaign.getEquipmentManager().getEquipmentForCompany(playerSquadron.getCompanyId());

        // Good planes in the squadron should stay in the squadron
        for (int planeThatShouldNotBeReplaced : goodPlanesInTheSquadron)
        {
            Assertions.assertTrue (equipmentForSquadronAfterTest.getEquippedTank(planeThatShouldNotBeReplaced) != null);
            Assertions.assertTrue (equipmentDepotAfterTest.getPlaneFromDepot(planeThatShouldNotBeReplaced) == null);
        }

        // Bad planes in the depot should stay in the depot
        for (int badlaneInDepot : badPlanesInDepot)
        {
            Assertions.assertTrue (equipmentForSquadronAfterTest.getEquippedTank(badlaneInDepot) == null);
            Assertions.assertTrue (equipmentDepotAfterTest.getPlaneFromDepot(badlaneInDepot) != null);
        }
    }
}
