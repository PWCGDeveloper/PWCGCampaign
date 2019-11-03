package pwcg.aar.inmission.phase2.logeval.equipmentstatus;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase1.parse.event.AType3;
import pwcg.aar.inmission.phase2.logeval.AARVehicleBuilder;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.plane.PlaneStatus;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class AAREquipmentStatusEvaluatorTest
{
    @Mock private AARLogEventData logEventData;
    @Mock private AARVehicleBuilder aarVehicleBuilder;
    @Mock private Campaign campaign;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
         
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420601"));

        Map <String, LogPlane> planeAiEntities = makePlaneEntities();
        Mockito.when(aarVehicleBuilder.getLogPlanes()).thenReturn(planeAiEntities);
    }

    @Test
    public void testPlaneDestroyed () throws PWCGException
    {
        AType3 atype3 = new AType3("T:54877 AType:3 AID:-1 TID:35839 POS(112150.266,93.277,111696.758)");
        Mockito.when(logEventData.getDestroyedEvent(Matchers.<String>any())).thenReturn(atype3);

        AAREquipmentStatusEvaluator aarEquipmentStatusEvaluator = new AAREquipmentStatusEvaluator(campaign, logEventData, aarVehicleBuilder);
        aarEquipmentStatusEvaluator.determineFateOfPlanesInMission();        
        runTestWithStatusCheck(aarEquipmentStatusEvaluator, PlaneStatus.STATUS_DESTROYED);
    }

    @Test
    public void testNotPlaneDestroyed () throws PWCGException
    {
        Mockito.when(logEventData.getDestroyedEvent(Matchers.<String>any())).thenReturn(null);

        AAREquipmentStatusEvaluator aarEquipmentStatusEvaluator = new AAREquipmentStatusEvaluator(campaign, logEventData, aarVehicleBuilder);
        aarEquipmentStatusEvaluator.determineFateOfPlanesInMission();        
        runTestWithStatusCheck(aarEquipmentStatusEvaluator, PlaneStatus.STATUS_DEPLOYED);
    }

    @Test
    public void testPlaneSurvivedBecauseNearAirfield () throws PWCGException
    {
        AType3 atype3 = new AType3("T:54877 AType:3 AID:-1 TID:35839 POS(230402.0,0.0,186710.0)");
        Mockito.when(logEventData.getDestroyedEvent(Matchers.<String>any())).thenReturn(atype3);

        AAREquipmentStatusEvaluator aarEquipmentStatusEvaluator = new AAREquipmentStatusEvaluator(campaign, logEventData, aarVehicleBuilder);
        aarEquipmentStatusEvaluator.determineFateOfPlanesInMission();        
        runTestWithStatusCheck(aarEquipmentStatusEvaluator, PlaneStatus.STATUS_DEPLOYED);
    }

    private void runTestWithStatusCheck(AAREquipmentStatusEvaluator AAREquipmentStatusEvaluator, int expectedStatus) throws PWCGException
    {
        AAREquipmentStatusEvaluator.determineFateOfPlanesInMission();
        for (LogPlane resultPlaneAfter : aarVehicleBuilder.getLogPlanes().values())
        {
            assert (resultPlaneAfter.getPlaneStatus() == expectedStatus);
        }
    }

    private Map <String, LogPlane> makePlaneEntities() throws PWCGException
    {
        ICountry country = CountryFactory.makeCountryByCountry(Country.RUSSIA);

        LogPlane resultPlane = new LogPlane(1);
        resultPlane.setLandAt(new Coordinate());
        resultPlane.setCountry(country);
        resultPlane.intializePilot(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        resultPlane.setSquadronId(10131132);
        resultPlane.setPlaneStatus(PlaneStatus.STATUS_DEPLOYED);

        Map <String, LogPlane> planeAiEntities = new HashMap <>();
        planeAiEntities.put("11111", resultPlane);        
        return planeAiEntities;
    }
}
