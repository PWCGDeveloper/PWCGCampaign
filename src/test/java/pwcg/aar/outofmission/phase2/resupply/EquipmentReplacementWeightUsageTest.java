package pwcg.aar.outofmission.phase2.resupply;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.resupply.depot.EquipmentReplacementWeightUsage;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.country.BoSServiceManager;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EquipmentReplacementWeightUsageTest
{
    private static Campaign earlyCampaign;
    private static Campaign lateCampaign;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        earlyCampaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
        lateCampaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_26_PROFILE_WEST);
    }
    
    @Test
    public void testGermanReplacementArchTypes() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20101);
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> squadronsForService = squadronManager.getActiveCompaniesForService(earlyCampaign.getDate(), service);
        
        EquipmentReplacementWeightUsage equipmentReplacementWeightUsage = new EquipmentReplacementWeightUsage(earlyCampaign.getDate());
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftUsageByArchType(squadronsForService);

        assert(aircraftUsageByArchType.containsKey("bf109"));
        assert(aircraftUsageByArchType.containsKey("bf110"));
        assert(aircraftUsageByArchType.containsKey("he111"));
        assert(aircraftUsageByArchType.containsKey("ju87"));
        assert(aircraftUsageByArchType.containsKey("ju88"));

        assert(!aircraftUsageByArchType.containsKey("hs129"));
        assert(!aircraftUsageByArchType.containsKey("fw190"));
        assert(!aircraftUsageByArchType.containsKey("yak"));
        assert(!aircraftUsageByArchType.containsKey("il2"));
        assert(!aircraftUsageByArchType.containsKey("fw190d"));
        assert(!aircraftUsageByArchType.containsKey("me262"));
        assert(!aircraftUsageByArchType.containsKey("ju52"));
        
        int me109Weight = aircraftUsageByArchType.get("bf109");
        int ju88Weight = aircraftUsageByArchType.get("ju88");
        assert(me109Weight > ju88Weight);
    }

    @Test
    public void testRussianReplacementArchTypes() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(10101);
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> squadronsForService = squadronManager.getActiveCompaniesForService(earlyCampaign.getDate(), service);
        
        EquipmentReplacementWeightUsage equipmentReplacementWeightUsage = new EquipmentReplacementWeightUsage(earlyCampaign.getDate());
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
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20202);
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> squadronsForService = squadronManager.getActiveCompaniesForService(earlyCampaign.getDate(), service);
        
        EquipmentReplacementWeightUsage equipmentReplacementWeightUsage = new EquipmentReplacementWeightUsage(earlyCampaign.getDate());
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftUsageByArchType(squadronsForService);

        assert(aircraftUsageByArchType.containsKey("mc200"));
        assert(aircraftUsageByArchType.size() == 1);
    }
    
    @Test
    public void testGermanLateReplacementArchTypes() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20101);
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> squadronsForService = squadronManager.getActiveCompaniesForService(lateCampaign.getDate(), service);
        
        EquipmentReplacementWeightUsage equipmentReplacementWeightUsage = new EquipmentReplacementWeightUsage(lateCampaign.getDate());
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftUsageByArchType(squadronsForService);

        assert(aircraftUsageByArchType.containsKey("bf109"));
        assert(aircraftUsageByArchType.containsKey("ju88"));
        assert(aircraftUsageByArchType.containsKey("fw190"));
        assert(aircraftUsageByArchType.containsKey("fw190d"));
        assert(aircraftUsageByArchType.containsKey("me262"));

        assert(aircraftUsageByArchType.containsKey("bf110"));
        assert(aircraftUsageByArchType.containsKey("he111"));
        assert(aircraftUsageByArchType.containsKey("ju87"));
        assert(aircraftUsageByArchType.containsKey("hs129"));        
        assert(aircraftUsageByArchType.containsKey("he111"));
        assert(!aircraftUsageByArchType.containsKey("ju52"));

        int me109Weight = aircraftUsageByArchType.get("bf109");
        int ju88Weight = aircraftUsageByArchType.get("ju88");
        
        assert(me109Weight > ju88Weight);
    }

    @Test
    public void testAmericanReplacementArchTypes() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(BoSServiceManager.USAAF);
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> squadronsForService = squadronManager.getActiveCompaniesForService(lateCampaign.getDate(), service);
        
        EquipmentReplacementWeightUsage equipmentReplacementWeightUsage = new EquipmentReplacementWeightUsage(lateCampaign.getDate());
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftUsageByArchType(squadronsForService);

        assert(aircraftUsageByArchType.containsKey("p47"));
    }

    @Test
    public void testBritishReplacementArchTypes() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(BoSServiceManager.RAF);
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> squadronsForService = squadronManager.getActiveCompaniesForService(lateCampaign.getDate(), service);
        
        EquipmentReplacementWeightUsage equipmentReplacementWeightUsage = new EquipmentReplacementWeightUsage(lateCampaign.getDate());
        Map<String, Integer> aircraftUsageByArchType = equipmentReplacementWeightUsage.getAircraftUsageByArchType(squadronsForService);

        assert(aircraftUsageByArchType.containsKey("spitfire"));
    }
}
