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
import pwcg.campaign.resupply.SquadronNeedFactory;
import pwcg.campaign.resupply.SquadronNeedFactory.SquadronNeedType;
import pwcg.campaign.resupply.depot.EquipmentNeedForSquadronsCalculator;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EquipmentReplacementWeightNoNeedTest
{
    private Campaign campaign;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.GROSS_DEUTSCHLAND_PROFILE);
    }
    
    
    @Test
    public void testGermanNeedNoLosses() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20101);
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> squadronsForService = squadronManager.getActiveCompaniesForService(campaign.getDate(), service);
        
        SquadronNeedFactory squadronNeedFactory = new SquadronNeedFactory(SquadronNeedType.EQUIPMENT);
        EquipmentNeedForSquadronsCalculator equipmentReplacementWeightNeed = new EquipmentNeedForSquadronsCalculator(campaign, squadronNeedFactory);
        Map<String, Integer> aircraftNeedByArchType = equipmentReplacementWeightNeed.getAircraftNeedByArchType(squadronsForService);

        assert(aircraftNeedByArchType.size() == 0);
    }
    
    @Test
    public void testRussianNeedNoLosses() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(10101);
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> squadronsForService = squadronManager.getActiveCompaniesForService(campaign.getDate(), service);
        
        SquadronNeedFactory squadronNeedFactory = new SquadronNeedFactory(SquadronNeedType.EQUIPMENT);
        EquipmentNeedForSquadronsCalculator equipmentReplacementWeightNeed = new EquipmentNeedForSquadronsCalculator(campaign, squadronNeedFactory);
        Map<String, Integer> aircraftNeedByArchType = equipmentReplacementWeightNeed.getAircraftNeedByArchType(squadronsForService);

        assert(aircraftNeedByArchType.size() == 0);
    }
    
    @Test
    public void testItalianNeedNoLosses() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20202);
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> squadronsForService = squadronManager.getActiveCompaniesForService(campaign.getDate(), service);
        
        SquadronNeedFactory squadronNeedFactory = new SquadronNeedFactory(SquadronNeedType.EQUIPMENT);
        EquipmentNeedForSquadronsCalculator equipmentReplacementWeightNeed = new EquipmentNeedForSquadronsCalculator(campaign, squadronNeedFactory);
        Map<String, Integer> aircraftNeedByArchType = equipmentReplacementWeightNeed.getAircraftNeedByArchType(squadronsForService);

        assert(aircraftNeedByArchType.size() == 0);
    }
}
