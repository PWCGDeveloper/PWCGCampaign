package pwcg.aar.outofmission.phase2.resupply;

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
import pwcg.campaign.resupply.SquadronNeedFactory;
import pwcg.campaign.resupply.SquadronNeedFactory.SquadronNeedType;
import pwcg.campaign.resupply.depot.EquipmentNeedForSquadronsCalculator;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentReplacementWeightNoNeedTest
{
    private Campaign campaign;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaignForceCreation(SquadronTestProfile.JG_51_PROFILE_STALINGRAD);
    }
    
    
    @Test
    public void testGermanNeedNoLosses() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20101);
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getActiveSquadronsForService(campaign.getDate(), service);
        
        SquadronNeedFactory squadronNeedFactory = new SquadronNeedFactory(SquadronNeedType.EQUIPMENT);
        EquipmentNeedForSquadronsCalculator equipmentReplacementWeightNeed = new EquipmentNeedForSquadronsCalculator(campaign, squadronNeedFactory);
        Map<String, Integer> aircraftNeedByArchType = equipmentReplacementWeightNeed.getAircraftNeedByArchType(squadronsForService);

        assert(aircraftNeedByArchType.size() == 0);
    }
    
    @Test
    public void testRussianNeedNoLosses() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(10101);
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getActiveSquadronsForService(campaign.getDate(), service);
        
        SquadronNeedFactory squadronNeedFactory = new SquadronNeedFactory(SquadronNeedType.EQUIPMENT);
        EquipmentNeedForSquadronsCalculator equipmentReplacementWeightNeed = new EquipmentNeedForSquadronsCalculator(campaign, squadronNeedFactory);
        Map<String, Integer> aircraftNeedByArchType = equipmentReplacementWeightNeed.getAircraftNeedByArchType(squadronsForService);

        assert(aircraftNeedByArchType.size() == 0);
    }
    
    @Test
    public void testItalianNeedNoLosses() throws PWCGException
    {
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20202);
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getActiveSquadronsForService(campaign.getDate(), service);
        
        SquadronNeedFactory squadronNeedFactory = new SquadronNeedFactory(SquadronNeedType.EQUIPMENT);
        EquipmentNeedForSquadronsCalculator equipmentReplacementWeightNeed = new EquipmentNeedForSquadronsCalculator(campaign, squadronNeedFactory);
        Map<String, Integer> aircraftNeedByArchType = equipmentReplacementWeightNeed.getAircraftNeedByArchType(squadronsForService);

        assert(aircraftNeedByArchType.size() == 0);
    }
}
