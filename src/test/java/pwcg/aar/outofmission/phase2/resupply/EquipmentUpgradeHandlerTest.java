package pwcg.aar.outofmission.phase2.resupply;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.resupply.depot.EquipmentDepot;
import pwcg.campaign.resupply.equipment.EquipmentUpgradeHandler;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentUpgradeHandlerTest
{
    private Campaign campaign;
    
    @Mock private ArmedService armedService;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaignForceCreation(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
        Mockito.when(armedService.getServiceId()).thenReturn(20101);
     }

    @Test
    public void testEquipmentUpgrade() throws PWCGException
    {
        PlaneTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlaneTypeFactory();
        EquipmentDepot equipmentDepotBeforeTest =  campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());

        // Add good planes to the depo
        List<Integer> veryGoodPlanesInDepot = new ArrayList<>();
        for (int i = 0; i < 2; ++i)
        {
            PlaneType planeTypeMe109K4 = planeTypeFactory.createPlaneTypeByType("bf109k4");        
            EquippedPlane me109K4ForDepot = new EquippedPlane(planeTypeMe109K4, campaign.getSerialNumber().getNextPlaneSerialNumber(), -1, PlaneStatus.STATUS_DEPOT);
            equipmentDepotBeforeTest.addPlaneToDepot(me109K4ForDepot);
            veryGoodPlanesInDepot.add(me109K4ForDepot.getSerialNumber());
        }

        for (int veryGoodPlaneInDepot : veryGoodPlanesInDepot)
        {
            assert(equipmentDepotBeforeTest.getPlaneFromDepot(veryGoodPlaneInDepot) != null);
        }

        // replace planes in squadron with different quality 109s, but all worse than the 
        // very good planes in the depot
        Squadron playerSquadron = campaign.determinePlayerSquadrons().get(0);
        Equipment equipmentForSquadronBeforeTest = campaign.getEquipmentManager().getEquipmentForSquadron(playerSquadron.getSquadronId());

        for (EquippedPlane planeInSquadronBeforeTest : equipmentForSquadronBeforeTest.getActiveEquippedPlanes().values())
        {
            equipmentForSquadronBeforeTest.removeEquippedPlane(planeInSquadronBeforeTest.getSerialNumber());
        }
        
        List<Integer> planesThatShouldBeReplaced = new ArrayList<>();
        for (int i = 0; i < 2; ++i)
        {
            PlaneType planeTypeMe109F2 = planeTypeFactory.createPlaneTypeByType("bf109f2");        
            EquippedPlane me109F2ForSquadron = new EquippedPlane(planeTypeMe109F2, campaign.getSerialNumber().getNextPlaneSerialNumber(), -1, PlaneStatus.STATUS_DEPLOYED);
            equipmentForSquadronBeforeTest.addEquippedPlane(me109F2ForSquadron);
            planesThatShouldBeReplaced.add(me109F2ForSquadron.getSerialNumber());
        }
        
        List<Integer> planesThatShouldNotBeReplaced = new ArrayList<>();
        for (int i = 0; i < 10; ++i)
        {
            PlaneType planeTypeMe109F4 = planeTypeFactory.createPlaneTypeByType("bf109f4");        
            EquippedPlane me109F4ForSquadron = new EquippedPlane(planeTypeMe109F4, campaign.getSerialNumber().getNextPlaneSerialNumber(), -1, PlaneStatus.STATUS_DEPLOYED);
            equipmentForSquadronBeforeTest.addEquippedPlane(me109F4ForSquadron);
            planesThatShouldNotBeReplaced.add(me109F4ForSquadron.getSerialNumber());
        }

        // Run the upgrade
        EquipmentUpgradeHandler equipmentUpgradeHandler = new EquipmentUpgradeHandler(campaign);
        equipmentUpgradeHandler.upgradeEquipment(armedService);

        // The worst planes in the squadrons should be replaced and now be in the depot
        EquipmentDepot equipmentDepotAfterTest =  campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());
        Equipment equipmentForSquadronAfterTest = campaign.getEquipmentManager().getEquipmentForSquadron(playerSquadron.getSquadronId());
        for (int planeThatShouldBeReplaced : planesThatShouldBeReplaced)
        {
            assert(equipmentForSquadronAfterTest.getEquippedPlane(planeThatShouldBeReplaced) == null);
            assert(equipmentDepotAfterTest.getPlaneFromDepot(planeThatShouldBeReplaced) != null);
        }

        // The better planes should be left in the squadron
        for (int planeThatShouldNotBeReplaced : planesThatShouldNotBeReplaced)
        {
            assert(equipmentForSquadronAfterTest.getEquippedPlane(planeThatShouldNotBeReplaced) != null);
            assert(equipmentDepotAfterTest.getPlaneFromDepot(planeThatShouldNotBeReplaced) == null);
        }
        
        // The very good planes in the depot should be moved from the depot to  the squadron
        for (int veryGoodPlaneInDepot : veryGoodPlanesInDepot)
        {
            assert(equipmentForSquadronAfterTest.getEquippedPlane(veryGoodPlaneInDepot) != null);
            assert(equipmentDepotAfterTest.getPlaneFromDepot(veryGoodPlaneInDepot) == null);
        }
    }

    @Test
    public void testEquipmentUpgradeNotNeeded() throws PWCGException
    {
        PlaneTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlaneTypeFactory();

        // Clear out the depot and replace with a couple of bad planes
        EquipmentDepot equipmentDepotBeforeTest =  campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());
        for (EquippedPlane planeInSquadronBeforeTest : equipmentDepotBeforeTest.getAllPlanesInDepot())
        {
            equipmentDepotBeforeTest.removeEquippedPlaneFromDepot(planeInSquadronBeforeTest.getSerialNumber());
        }

        List<Integer> badlanesInDepot = new ArrayList<>();
        for (int i = 0; i < 2; ++i)
        {
            PlaneType planeTypeMe109F2 = planeTypeFactory.createPlaneTypeByType("bf109f2");        
            EquippedPlane me109F2ForDepot = new EquippedPlane(planeTypeMe109F2, campaign.getSerialNumber().getNextPlaneSerialNumber(), -1, PlaneStatus.STATUS_DEPOT);
            equipmentDepotBeforeTest.addPlaneToDepot(me109F2ForDepot);
            badlanesInDepot.add(me109F2ForDepot.getSerialNumber());
        }

        
        // Clear out the squadron and add better planes than we have in the depot
        Squadron playerSquadron = campaign.determinePlayerSquadrons().get(0);
        Equipment equipmentForSquadronBeforeTest = campaign.getEquipmentManager().getEquipmentForSquadron(playerSquadron.getSquadronId());

        for (EquippedPlane planeInSquadronBeforeTest : equipmentForSquadronBeforeTest.getActiveEquippedPlanes().values())
        {
            equipmentForSquadronBeforeTest.removeEquippedPlane(planeInSquadronBeforeTest.getSerialNumber());
        }
        
        List<Integer> goodPlanesInTheSquadron = new ArrayList<>();
        for (int i = 0; i < 12; ++i)
        {
            PlaneType planeTypeMe109K4 = planeTypeFactory.createPlaneTypeByType("bf109k4");        
            EquippedPlane me109K4ForSquadron = new EquippedPlane(planeTypeMe109K4, campaign.getSerialNumber().getNextPlaneSerialNumber(), -1, PlaneStatus.STATUS_DEPLOYED);
            equipmentForSquadronBeforeTest.addEquippedPlane(me109K4ForSquadron);
            goodPlanesInTheSquadron.add(me109K4ForSquadron.getSerialNumber());
        }


        // Run the upgrade
        EquipmentUpgradeHandler equipmentUpgradeHandler = new EquipmentUpgradeHandler(campaign);
        equipmentUpgradeHandler.upgradeEquipment(armedService);

        
        EquipmentDepot equipmentDepotAfterTest =  campaign.getEquipmentManager().getEquipmentDepotForService(armedService.getServiceId());
        Equipment equipmentForSquadronAfterTest = campaign.getEquipmentManager().getEquipmentForSquadron(playerSquadron.getSquadronId());

        // Good planes in the squadron should stay in the squadron
        for (int planeThatShouldNotBeReplaced : goodPlanesInTheSquadron)
        {
            assert(equipmentForSquadronAfterTest.getEquippedPlane(planeThatShouldNotBeReplaced) != null);
            assert(equipmentDepotAfterTest.getPlaneFromDepot(planeThatShouldNotBeReplaced) == null);
        }
        
        // Bad planes in the depot should stay in the depot
        for (int badlaneInDepot : badlanesInDepot)
        {
            assert(equipmentForSquadronAfterTest.getEquippedPlane(badlaneInDepot) == null);
            assert(equipmentDepotAfterTest.getPlaneFromDepot(badlaneInDepot) != null);
        }
    }
}
