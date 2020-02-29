package pwcg.aar.outofmission.phase2.resupply;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.campaign.resupply.SquadronNeedFactory;
import pwcg.campaign.resupply.SquadronNeedFactory.SquadronNeedType;
import pwcg.campaign.resupply.depo.EquipmentNeedForSquadronsCalculator;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentReplacementWeightNeedTest
{
    private static Campaign campaign;
    private static boolean runSetupOneTime = false;

    @Before
    public void setup() throws PWCGException
    {
        if (!runSetupOneTime)
        {
            runSetupOneTime = true;
            PWCGContext.setProduct(PWCGProduct.BOS);
            campaign = CampaignCache.makeCampaignForceCreation(SquadronTestProfile.KG53_PROFILE);
            deactivateAircraft();
        }
    }
    
    
    @Test
    public void testGermanEquipmentNeed() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20101);
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getActiveSquadronsForService(campaign.getDate(), service);
        
        SquadronNeedFactory squadronNeedFactory = new SquadronNeedFactory(SquadronNeedType.EQUIPMENT);
        EquipmentNeedForSquadronsCalculator equipmentReplacementWeightNeed = new EquipmentNeedForSquadronsCalculator(campaign, squadronNeedFactory);
        Map<String, Integer> aircraftNeedByArchType = equipmentReplacementWeightNeed.getAircraftNeedByArchType(squadronsForService);

        assert(aircraftNeedByArchType.containsKey("bf109"));
        assert(aircraftNeedByArchType.containsKey("fw190"));
        assert(aircraftNeedByArchType.containsKey("he111"));

        assert(!aircraftNeedByArchType.containsKey("bf110"));
        assert(!aircraftNeedByArchType.containsKey("ju87"));
        assert(!aircraftNeedByArchType.containsKey("ju88"));
        assert(!aircraftNeedByArchType.containsKey("ju52"));
        assert(!aircraftNeedByArchType.containsKey("hs129"));
        assert(!aircraftNeedByArchType.containsKey("yak"));
        assert(!aircraftNeedByArchType.containsKey("il2"));
        
        int me109Weight = aircraftNeedByArchType.get("bf109");
        assert(me109Weight == 1);

        int fw190Weight = aircraftNeedByArchType.get("fw190");
        assert(fw190Weight == 2);

        int he111Weight = aircraftNeedByArchType.get("he111");
        assert(he111Weight == 5);
    }
    
    @Test
    public void testRussianEquipmentNeed() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(10101);
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getActiveSquadronsForService(campaign.getDate(), service);
        
        SquadronNeedFactory squadronNeedFactory = new SquadronNeedFactory(SquadronNeedType.EQUIPMENT);
        EquipmentNeedForSquadronsCalculator equipmentReplacementWeightNeed = new EquipmentNeedForSquadronsCalculator(campaign, squadronNeedFactory);
        Map<String, Integer> aircraftNeedByArchType = equipmentReplacementWeightNeed.getAircraftNeedByArchType(squadronsForService);

        assert(aircraftNeedByArchType.containsKey("il2"));
        
        assert(!aircraftNeedByArchType.containsKey("i16"));
        assert(!aircraftNeedByArchType.containsKey("lagg"));
        assert(!aircraftNeedByArchType.containsKey("pe2"));
        assert(!aircraftNeedByArchType.containsKey("mig3"));
        assert(!aircraftNeedByArchType.containsKey("yak"));
        assert(!aircraftNeedByArchType.containsKey("bf109"));
        assert(!aircraftNeedByArchType.containsKey("he111"));
        
        int il2Weight = aircraftNeedByArchType.get("il2");
        assert(il2Weight == 6);
    }
    
    @Test
    public void testItalianEquipmentNeed() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20202);
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getActiveSquadronsForService(campaign.getDate(), service);
        
        SquadronNeedFactory squadronNeedFactory = new SquadronNeedFactory(SquadronNeedType.EQUIPMENT);
        EquipmentNeedForSquadronsCalculator equipmentReplacementWeightNeed = new EquipmentNeedForSquadronsCalculator(campaign, squadronNeedFactory);
        Map<String, Integer> aircraftNeedByArchType = equipmentReplacementWeightNeed.getAircraftNeedByArchType(squadronsForService);

        assert(aircraftNeedByArchType.containsKey("mc200"));
        
        Integer numNeeded = aircraftNeedByArchType.get("mc200");
        assert(numNeeded == 3);
    }
    
    private void deactivateAircraft() throws PWCGException
    {
        Equipment gruppo21 = campaign.getEquipmentManager().getEquipmentForSquadron(20115021);
        destroyPlanesInSquadron(gruppo21, 3);

        Equipment i_jg51 = campaign.getEquipmentManager().getEquipmentForSquadron(20111051);
        destroyPlanesInSquadron(i_jg51, 2);

        Equipment ii_jg52 = campaign.getEquipmentManager().getEquipmentForSquadron(20112052);
        destroyPlanesInSquadron(ii_jg52, 1);

        Equipment i_kg53 = campaign.getEquipmentManager().getEquipmentForSquadron(20131053);
        destroyPlanesInSquadron(i_kg53, 5);

        Equipment vvs_312Reg = campaign.getEquipmentManager().getEquipmentForSquadron(10121312);
        destroyPlanesInSquadron(vvs_312Reg, 4);

        Equipment vvs_175Reg = campaign.getEquipmentManager().getEquipmentForSquadron(10121175);
        destroyPlanesInSquadron(vvs_175Reg, 2);
    }


    private void destroyPlanesInSquadron(Equipment squadronEquipment, int numToDestroy) throws PWCGException
    {
        int numDestroyed = 0;
        for (EquippedPlane equippedPlane : squadronEquipment.getActiveEquippedPlanes().values())
        {
            equippedPlane.setPlaneStatus(PlaneStatus.STATUS_DESTROYED);
            Date dateDestroyed = DateUtils.removeTimeDays(campaign.getDate(), 10);
            equippedPlane.setDateRemovedFromService(dateDestroyed);
            ++numDestroyed;
            if (numDestroyed == numToDestroy)
            {
                break;
            }
        }
    }
}
