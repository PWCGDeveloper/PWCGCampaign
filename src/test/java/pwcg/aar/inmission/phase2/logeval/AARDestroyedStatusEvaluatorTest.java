package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase1.parse.event.AType3;
import pwcg.aar.inmission.phase1.parse.event.IAType17;
import pwcg.aar.inmission.phase1.parse.event.IAType3;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class AARDestroyedStatusEvaluatorTest
{
    @Mock private AARDamageStatusEvaluator aarDamageStatusEvaluator;
    @Mock private LogAIEntity DestroyedVictim1;
    @Mock private LogAIEntity DestroyedVictor1;
    @Mock private AARLogEventData logEventData;
    @Mock private AARVehicleBuilder aarVehicleBuilder;
    @Mock private AType3 logDestroyedEvent1;
    @Mock private AType3 logDestroyedEvent2;
    @Mock private AType3 logDestroyedEvent3;
    @Mock private AARCrossedPathWithPlayerEvaluator aarCrossedPathWithPlayerEvaluator;

    @Before
    public void setup () throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
    }

    /**
     * Test Case: Three vehicles are listed as destroyed.  None is listed as crossing paths with
     * the player
     */
    @Test
    public void testSetVehiclesDestroyed () throws PWCGException
    {        
        Mockito.when(logDestroyedEvent1.getVictim()).thenReturn("100");
        Mockito.when(logDestroyedEvent2.getVictim()).thenReturn("101");
        Mockito.when(logDestroyedEvent3.getVictim()).thenReturn("102");
        List<IAType3> logParserDestroyedEvents = new ArrayList<>();
        logParserDestroyedEvents.add(logDestroyedEvent1);
        logParserDestroyedEvents.add(logDestroyedEvent2);
        logParserDestroyedEvents.add(logDestroyedEvent3);
        Mockito.when(logEventData.getDestroyedEvents()).thenReturn(logParserDestroyedEvents);

        Mockito.when(aarVehicleBuilder.getVehicle("99")).thenReturn(DestroyedVictor1);
        Mockito.when(aarVehicleBuilder.getVehicle("100")).thenReturn(DestroyedVictim1);
        Mockito.when(aarVehicleBuilder.getVehicle("101")).thenReturn(DestroyedVictim1);
        Mockito.when(aarVehicleBuilder.getVehicle("102")).thenReturn(DestroyedVictim1);

        AARDestroyedStatusEvaluator aarDestroyedStatusEvaluator = new AARDestroyedStatusEvaluator(
                        logEventData,
                        aarVehicleBuilder,
                        aarDamageStatusEvaluator);

        aarDestroyedStatusEvaluator.buildDeadLists();
        List<LogVictory> vehiclesDestroyed = aarDestroyedStatusEvaluator.getDeadLogVehicleList();

        assert(vehiclesDestroyed.size() == 3);
        for (LogVictory vehicleDestroyed : vehiclesDestroyed)
        {
            assert(vehicleDestroyed.isCrossedPlayerPath() == false);
        }
    }

    /**
     * Test Case: Only two vehicles are listed as destroyed because  one has no victor
     */
    @Test
    public void testSetVehiclesDestroyedWithNullVictim () throws PWCGException
    {        
        Mockito.when(logDestroyedEvent1.getVictim()).thenReturn("100");
        Mockito.when(logDestroyedEvent2.getVictim()).thenReturn("101");
        Mockito.when(logDestroyedEvent3.getVictim()).thenReturn("102");
        List<IAType3> logParserDestroyedEvents = new ArrayList<>();
        logParserDestroyedEvents.add(logDestroyedEvent1);
        logParserDestroyedEvents.add(logDestroyedEvent2);
        logParserDestroyedEvents.add(logDestroyedEvent3);
        Mockito.when(logEventData.getDestroyedEvents()).thenReturn(logParserDestroyedEvents);

        Mockito.when(aarVehicleBuilder.getVehicle("99")).thenReturn(DestroyedVictor1);
        Mockito.when(aarVehicleBuilder.getVehicle("100")).thenReturn(DestroyedVictim1);
        Mockito.when(aarVehicleBuilder.getVehicle("101")).thenReturn(DestroyedVictim1);
        Mockito.when(aarVehicleBuilder.getVehicle("102")).thenReturn(null);

        AARDestroyedStatusEvaluator aarDestroyedStatusEvaluator = new AARDestroyedStatusEvaluator(
                        logEventData,
                        aarVehicleBuilder,
                        aarDamageStatusEvaluator);

        aarDestroyedStatusEvaluator.buildDeadLists();
        List<LogVictory> vehiclesDestroyed = aarDestroyedStatusEvaluator.getDeadLogVehicleList();

        assert(vehiclesDestroyed.size() == 2);
        for (LogVictory vehicleDestroyed : vehiclesDestroyed)
        {
            assert(vehicleDestroyed.isCrossedPlayerPath() == false);
        }
    }
    

    /**
     * Test Case: Three vehicles are listed as destroyed.  One is listed as crossing paths with
     * the player
     */
    @Test
    public void testSetVehiclesDestroyedWithCrossedPath () throws PWCGException
    {        
        Mockito.when(logDestroyedEvent1.getVictim()).thenReturn("100");
        Mockito.when(logDestroyedEvent2.getVictim()).thenReturn("101");
        Mockito.when(logDestroyedEvent3.getVictim()).thenReturn("102");
        List<IAType3> logParserDestroyedEvents = new ArrayList<>();
        logParserDestroyedEvents.add(logDestroyedEvent1);
        logParserDestroyedEvents.add(logDestroyedEvent2);
        logParserDestroyedEvents.add(logDestroyedEvent3);
        Mockito.when(logEventData.getDestroyedEvents()).thenReturn(logParserDestroyedEvents);

        Mockito.when(aarVehicleBuilder.getVehicle("99")).thenReturn(DestroyedVictor1);
        Mockito.when(aarVehicleBuilder.getVehicle("100")).thenReturn(DestroyedVictim1);
        Mockito.when(aarVehicleBuilder.getVehicle("101")).thenReturn(DestroyedVictim1);
        Mockito.when(aarVehicleBuilder.getVehicle("102")).thenReturn(DestroyedVictim1);

        Mockito.when(aarCrossedPathWithPlayerEvaluator.isCrossedPathWithPlayerFlight(
                        Matchers.<LogVictory>any(),
                        Matchers.<AARPlayerLocator>any(),
                        Matchers.<List<IAType17>>any())).thenReturn(true);
        
        
        AARDestroyedStatusEvaluator aarDestroyedStatusEvaluator = new AARDestroyedStatusEvaluator(
                        logEventData,
                        aarVehicleBuilder,
                        aarDamageStatusEvaluator);

        aarDestroyedStatusEvaluator.setAarCrossedPathWithPlayerEvaluator(aarCrossedPathWithPlayerEvaluator);

        aarDestroyedStatusEvaluator.buildDeadLists();
        List<LogVictory> vehiclesDestroyed = aarDestroyedStatusEvaluator.getDeadLogVehicleList();

        assert(vehiclesDestroyed.size() == 3);
        for (LogVictory vehicleDestroyed : vehiclesDestroyed)
        {
            assert(vehicleDestroyed.isCrossedPlayerPath() == true);
        }
    }
   
}
