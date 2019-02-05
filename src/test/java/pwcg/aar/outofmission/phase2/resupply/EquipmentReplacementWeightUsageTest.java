package pwcg.aar.outofmission.phase2.resupply;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.resupply.depo.EquipmentReplacementWeightUsage;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheBoS;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentReplacementWeightUsageTest
{
    private Campaign earlyCampaign;
    private Campaign lateCampaign;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        earlyCampaign = CampaignCache.makeCampaignForceCreation(CampaignCacheBoS.JG_51_PROFILE);
        lateCampaign = CampaignCache.makeCampaignForceCreation(CampaignCacheBoS.JG_51_PROFILE_3);
    }
    
    @Test
    public void testGermanReplacementArchTypes() throws PWCGException
    {
        earlyCampaign = CampaignCache.makeCampaign(CampaignCacheBoS.JG_51_PROFILE);
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20101);
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getActiveSquadronsForService(earlyCampaign.getDate(), service);
        
        EquipmentReplacementWeightUsage equipmentReplacementWeightUsage = new EquipmentReplacementWeightUsage(earlyCampaign);
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftUsageByArchType(squadronsForService);

        assert(aircraftUsageByArchType.containsKey("bf109"));
        assert(aircraftUsageByArchType.containsKey("bf110"));
        assert(aircraftUsageByArchType.containsKey("he111"));
        assert(aircraftUsageByArchType.containsKey("ju87"));
        assert(aircraftUsageByArchType.containsKey("ju88"));
        assert(aircraftUsageByArchType.containsKey("ju52"));

        assert(!aircraftUsageByArchType.containsKey("hs129"));
        assert(!aircraftUsageByArchType.containsKey("fw190"));
        assert(!aircraftUsageByArchType.containsKey("yak"));
        assert(!aircraftUsageByArchType.containsKey("il2"));
        
        int me109Weight = aircraftUsageByArchType.get("bf109");
        int ju88Weight = aircraftUsageByArchType.get("ju88");
        assert(me109Weight > ju88Weight);
    }

    @Test
    public void testRussianReplacementArchTypes() throws PWCGException
    {
        earlyCampaign = CampaignCache.makeCampaign(CampaignCacheBoS.JG_51_PROFILE);
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(10101);
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getActiveSquadronsForService(earlyCampaign.getDate(), service);
        
        EquipmentReplacementWeightUsage equipmentReplacementWeightUsage = new EquipmentReplacementWeightUsage(earlyCampaign);
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftUsageByArchType(squadronsForService);

        assert(aircraftUsageByArchType.containsKey("il2"));
        assert(aircraftUsageByArchType.containsKey("i16"));
        assert(aircraftUsageByArchType.containsKey("lagg"));
        assert(aircraftUsageByArchType.containsKey("pe2"));
        assert(aircraftUsageByArchType.containsKey("mig3"));

        assert(!aircraftUsageByArchType.containsKey("yak"));
        assert(!aircraftUsageByArchType.containsKey("bf109"));
        assert(!aircraftUsageByArchType.containsKey("he111"));
        
        int il2Weight = aircraftUsageByArchType.get("il2");
        int i16Weight = aircraftUsageByArchType.get("i16");
        assert(il2Weight > i16Weight);
    }
    
    @Test
    public void testItalianReplacementArchTypes() throws PWCGException
    {
        earlyCampaign = CampaignCache.makeCampaign(CampaignCacheBoS.JG_51_PROFILE);
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20202);
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getActiveSquadronsForService(earlyCampaign.getDate(), service);
        
        EquipmentReplacementWeightUsage equipmentReplacementWeightUsage = new EquipmentReplacementWeightUsage(earlyCampaign);
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftUsageByArchType(squadronsForService);

        assert(aircraftUsageByArchType.containsKey("mc200"));
        assert(aircraftUsageByArchType.size() == 1);
    }
    
    @Test
    public void testGermanLateReplacementArchTypes() throws PWCGException
    {
        lateCampaign = CampaignCache.makeCampaign(CampaignCacheBoS.JG_51_PROFILE_3);
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20101);
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getActiveSquadronsForService(lateCampaign.getDate(), service);
        
        EquipmentReplacementWeightUsage equipmentReplacementWeightUsage = new EquipmentReplacementWeightUsage(lateCampaign);
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftUsageByArchType(squadronsForService);

        assert(aircraftUsageByArchType.containsKey("bf109"));
        assert(aircraftUsageByArchType.containsKey("ju88"));
        assert(aircraftUsageByArchType.containsKey("fw190"));

        assert(!aircraftUsageByArchType.containsKey("bf110"));
        assert(!aircraftUsageByArchType.containsKey("he111"));
        assert(!aircraftUsageByArchType.containsKey("ju87"));
        assert(!aircraftUsageByArchType.containsKey("ju52"));
        assert(!aircraftUsageByArchType.containsKey("hs129"));        
        assert(!aircraftUsageByArchType.containsKey("he111"));
        
        int me109Weight = aircraftUsageByArchType.get("bf109");
        int ju88Weight = aircraftUsageByArchType.get("ju88");
        assert(me109Weight > ju88Weight);
    }

    @Test
    public void testAmericanReplacementArchTypes() throws PWCGException
    {
        lateCampaign = CampaignCache.makeCampaign(CampaignCacheBoS.JG_51_PROFILE_3);
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(10102);
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getActiveSquadronsForService(lateCampaign.getDate(), service);
        
        EquipmentReplacementWeightUsage equipmentReplacementWeightUsage = new EquipmentReplacementWeightUsage(lateCampaign);
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftUsageByArchType(squadronsForService);

        assert(aircraftUsageByArchType.containsKey("p47"));
    }

    @Test
    public void testBritishReplacementArchTypes() throws PWCGException
    {
        lateCampaign = CampaignCache.makeCampaign(CampaignCacheBoS.JG_51_PROFILE_3);
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(10103);
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getActiveSquadronsForService(lateCampaign.getDate(), service);
        
        EquipmentReplacementWeightUsage equipmentReplacementWeightUsage = new EquipmentReplacementWeightUsage(lateCampaign);
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftUsageByArchType(squadronsForService);

        assert(aircraftUsageByArchType.containsKey("spitfire"));
    }
}
