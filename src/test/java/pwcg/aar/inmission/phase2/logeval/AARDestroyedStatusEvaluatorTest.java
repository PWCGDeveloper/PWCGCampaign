package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.AType3;
import pwcg.core.logfiles.event.IAType3;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AARDestroyedStatusEvaluatorTest
{
    private static final String VICTOR = "500";
    private static final String VICTIM_1 = "100";
    private static final String VICTIM_2 = "101";
    private static final String VICTIM_3 = "102";
    @Mock private AARDamageStatusEvaluator aarDamageStatusEvaluator;
    @Mock private LogAIEntity destroyedVictim1;
    @Mock private LogEventData logEventData;
    @Mock private AARVehicleBuilder aarVehicleBuilder;
    @Mock private AType3 logDestroyedEvent1;
    @Mock private AType3 logDestroyedEvent2;
    @Mock private AType3 logDestroyedEvent3;

    public AARDestroyedStatusEvaluatorTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @BeforeEach
    public void setup() throws PWCGException
    {
   }
    
    /**
     * Test Case: Three vehicles are listed as destroyed.  
     * Victor is known
     * No damage events added because victor is known
     */
    @Test
    public void testSetVehiclesDestroyedVictorKnown () throws PWCGException
    {
        Mockito.when(logDestroyedEvent1.getVictim()).thenReturn(VICTIM_1);
        Mockito.when(logDestroyedEvent2.getVictim()).thenReturn(VICTIM_2);
        Mockito.when(logDestroyedEvent3.getVictim()).thenReturn(VICTIM_3);
        
        Mockito.when(logDestroyedEvent1.getVictor()).thenReturn(VICTOR);
        Mockito.when(logDestroyedEvent2.getVictor()).thenReturn("501");
        Mockito.when(logDestroyedEvent3.getVictor()).thenReturn("502");

        List<IAType3> logParserDestroyedEvents = new ArrayList<>();
        logParserDestroyedEvents.add(logDestroyedEvent1);
        logParserDestroyedEvents.add(logDestroyedEvent2);
        logParserDestroyedEvents.add(logDestroyedEvent3);
        Mockito.when(logEventData.getDestroyedEvents()).thenReturn(logParserDestroyedEvents);

        Mockito.when(aarVehicleBuilder.getVehicle(VICTIM_1)).thenReturn(destroyedVictim1);
        Mockito.when(aarVehicleBuilder.getVehicle(VICTIM_2)).thenReturn(destroyedVictim1);
        Mockito.when(aarVehicleBuilder.getVehicle(VICTIM_3)).thenReturn(destroyedVictim1);

        AARDestroyedStatusEvaluator aarDestroyedStatusEvaluator = new AARDestroyedStatusEvaluator(
                        logEventData,
                        aarVehicleBuilder,
                        aarDamageStatusEvaluator);

        aarDestroyedStatusEvaluator.buildDeadLists();
        List<LogVictory> vehiclesDestroyed = aarDestroyedStatusEvaluator.getDeadLogVehicleList();

        Assertions.assertTrue(vehiclesDestroyed.size() == 3);
        for (LogVictory victory : vehiclesDestroyed)
        {
            Assertions.assertFalse(victory.didCrewMemberDamagePlane(VICTIM_1));
        }
    }

    /**
     * Test Case: Only two vehicles are listed as destroyed because  one has no victor
     */
    @Test
    public void testSetVehiclesDestroyedWithNullVictim () throws PWCGException
    {        
        Mockito.when(logDestroyedEvent1.getVictim()).thenReturn(VICTIM_1);
        Mockito.when(logDestroyedEvent2.getVictim()).thenReturn(VICTIM_2);
        Mockito.when(logDestroyedEvent3.getVictim()).thenReturn(VICTIM_3);
        List<IAType3> logParserDestroyedEvents = new ArrayList<>();
        logParserDestroyedEvents.add(logDestroyedEvent1);
        logParserDestroyedEvents.add(logDestroyedEvent2);
        logParserDestroyedEvents.add(logDestroyedEvent3);
        Mockito.when(logEventData.getDestroyedEvents()).thenReturn(logParserDestroyedEvents);

        Mockito.when(aarVehicleBuilder.getVehicle(VICTIM_1)).thenReturn(destroyedVictim1);
        Mockito.when(aarVehicleBuilder.getVehicle(VICTIM_2)).thenReturn(destroyedVictim1);
        Mockito.when(aarVehicleBuilder.getVehicle(VICTIM_3)).thenReturn(null);

        AARDestroyedStatusEvaluator aarDestroyedStatusEvaluator = new AARDestroyedStatusEvaluator(
                        logEventData,
                        aarVehicleBuilder,
                        aarDamageStatusEvaluator);

        aarDestroyedStatusEvaluator.buildDeadLists();
        List<LogVictory> vehiclesDestroyed = aarDestroyedStatusEvaluator.getDeadLogVehicleList();

        Assertions.assertTrue(vehiclesDestroyed.size() == 2);
    }
    

    /**
     * Test Case: Three vehicles are listed as destroyed.  
     * Victor is not known
     * Damage events added because no victor
     */
    @Test
    public void testSetVehiclesDestroyedWithDamageEventsAdded () throws PWCGException
    {        
        Mockito.when(logDestroyedEvent1.getVictim()).thenReturn(VICTIM_1);
        Mockito.when(logDestroyedEvent2.getVictim()).thenReturn(VICTIM_2);
        Mockito.when(logDestroyedEvent3.getVictim()).thenReturn(VICTIM_3);
        
        List<IAType3> logParserDestroyedEvents = new ArrayList<>();
        logParserDestroyedEvents.add(logDestroyedEvent1);
        logParserDestroyedEvents.add(logDestroyedEvent2);
        logParserDestroyedEvents.add(logDestroyedEvent3);
        Mockito.when(logEventData.getDestroyedEvents()).thenReturn(logParserDestroyedEvents);

        Mockito.when(aarVehicleBuilder.getVehicle(VICTIM_1)).thenReturn(destroyedVictim1);
        Mockito.when(aarVehicleBuilder.getVehicle(VICTIM_2)).thenReturn(destroyedVictim1);
        Mockito.when(aarVehicleBuilder.getVehicle(VICTIM_3)).thenReturn(destroyedVictim1);

        Mockito.when(destroyedVictim1.getId()).thenReturn(VICTIM_1);
        
        AARDamageStatus damageStatus = new AARDamageStatus(VICTIM_1);
        LogDamage damageRecord = new LogDamage(1);
        damageRecord.setVictim(destroyedVictim1);
        damageStatus.addDamage(VICTOR, damageRecord);
        Mockito.when(aarDamageStatusEvaluator.getDamageStatusForVehicle(VICTIM_1)).thenReturn(damageStatus);
 
        AARDestroyedStatusEvaluator aarDestroyedStatusEvaluator = new AARDestroyedStatusEvaluator(
                        logEventData,
                        aarVehicleBuilder,
                        aarDamageStatusEvaluator);

        aarDestroyedStatusEvaluator.buildDeadLists();
        List<LogVictory> vehiclesDestroyed = aarDestroyedStatusEvaluator.getDeadLogVehicleList();

        Assertions.assertTrue(vehiclesDestroyed.size() == 3);

        Assertions.assertTrue(vehiclesDestroyed.size() == 3);
        for (LogVictory victory : vehiclesDestroyed)
        {
            Assertions.assertTrue(victory.didCrewMemberDamagePlane(VICTOR));
        }
    }
}
