package pwcg.aar.campaign.update;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentArchtypeFinderTest
{
    @Mock private Campaign campaign;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
    }
    
    @Test
    public void testGermanReplacementArchTypes() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19411101"));

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20101);
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getActiveSquadronsForService(campaign.getDate(), service);
        
        EquipmentArchtypeFinder equipmentArchtypeFinder = new EquipmentArchtypeFinder(campaign);
        Map<String, Integer> aircraftUsageByArchType = equipmentArchtypeFinder.getAircraftUsageByArchType();

        String archTypeName =equipmentArchtypeFinder.getArchTypeForReplacementPlane(squadronsForService);
        assert(aircraftUsageByArchType.containsKey(archTypeName));
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
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19411101"));

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(10101);
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getActiveSquadronsForService(campaign.getDate(), service);
        
        EquipmentArchtypeFinder equipmentArchtypeFinder = new EquipmentArchtypeFinder(campaign);
        Map<String, Integer> aircraftUsageByArchType = equipmentArchtypeFinder.getAircraftUsageByArchType();

        String archTypeName =equipmentArchtypeFinder.getArchTypeForReplacementPlane(squadronsForService);
        assert(aircraftUsageByArchType.containsKey(archTypeName));
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
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));

        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20202);
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getActiveSquadronsForService(campaign.getDate(), service);
        
        EquipmentArchtypeFinder equipmentArchtypeFinder = new EquipmentArchtypeFinder(campaign);
        Map<String, Integer> aircraftUsageByArchType = equipmentArchtypeFinder.getAircraftUsageByArchType();

        String archTypeName =equipmentArchtypeFinder.getArchTypeForReplacementPlane(squadronsForService);
        assert(aircraftUsageByArchType.containsKey(archTypeName));
        assert(aircraftUsageByArchType.containsKey("mc200"));
        assert(aircraftUsageByArchType.size() == 1);
    }
}
