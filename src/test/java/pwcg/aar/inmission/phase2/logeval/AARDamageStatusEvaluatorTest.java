package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase1.parse.event.IAType2;
import pwcg.aar.inmission.phase1.parse.event.rof.AType2;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class AARDamageStatusEvaluatorTest
{
    @Mock
    LogDamage damagedEntity;

    @Mock
    LogAIEntity damagedVictim1;

    @Mock
    LogAIEntity damagedVictim2;

    @Mock
    LogAIEntity damagedVictor1;

    @Mock
    private AARLogEventData logEventData;
    
    @Mock
    AARVehicleBuilder aarVehicleBuilder;
    
    @Mock
    AType2 logDamageEvent1;
    
    @Mock
    AType2 logDamageEvent2;
    
    @Mock
    AType2 logDamageEvent3;
    
    @Before
    public void setup () throws PWCGException
    {
        PWCGContextManager.setRoF(true);
    }

    @Test
    public void testSetVehiclesDamagedByPlayer () throws PWCGException
    {        
        Mockito.when(logDamageEvent1.getVictim()).thenReturn("100");
        Mockito.when(logDamageEvent2.getVictim()).thenReturn("101");
        List<IAType2> logParserDamagedEvents = new ArrayList<>();
        logParserDamagedEvents.add(logDamageEvent1);
        logParserDamagedEvents.add(logDamageEvent2);
        Mockito.when(logEventData.getDamageEvents()).thenReturn(logParserDamagedEvents);
        Mockito.when(logEventData.isVehicle("99")).thenReturn(true);
        Mockito.when(logEventData.isVehicle("100")).thenReturn(true);
        Mockito.when(logEventData.isVehicle("101")).thenReturn(true);

        Mockito.when(aarVehicleBuilder.getVehicle("100")).thenReturn(damagedVictim1);
        Mockito.when(aarVehicleBuilder.getVehicle("99")).thenReturn(damagedVictor1);

        
        AARDamageStatusEvaluator aarDamageStatusEvaluator = new AARDamageStatusEvaluator(
                        logEventData,
                        aarVehicleBuilder);

        aarDamageStatusEvaluator.buildDamagedList();
        List<LogDamage> vehiclesDamagedByPlayer = aarDamageStatusEvaluator.getVehiclesDamaged();

        assert(vehiclesDamagedByPlayer.size() == 2);
    }

    @Test
    public void testSetVehiclesDamagedByPlayerWithDuplicates () throws PWCGException
    {        
        Mockito.when(logDamageEvent1.getVictim()).thenReturn("100");
        Mockito.when(logDamageEvent2.getVictim()).thenReturn("101");
        Mockito.when(logDamageEvent3.getVictim()).thenReturn("101");
        List<IAType2> logParserDamagedEvents = new ArrayList<>();
        logParserDamagedEvents.add(logDamageEvent1);
        logParserDamagedEvents.add(logDamageEvent2);
        logParserDamagedEvents.add(logDamageEvent3);
        Mockito.when(logEventData.getDamageEvents()).thenReturn(logParserDamagedEvents);
        Mockito.when(logEventData.isVehicle("99")).thenReturn(true);
        Mockito.when(logEventData.isVehicle("100")).thenReturn(true);
        Mockito.when(logEventData.isVehicle("101")).thenReturn(true);

        Mockito.when(aarVehicleBuilder.getVehicle("100")).thenReturn(damagedVictim1);
        Mockito.when(aarVehicleBuilder.getVehicle("99")).thenReturn(damagedVictor1);

        
        AARDamageStatusEvaluator aarDamageStatusEvaluator = new AARDamageStatusEvaluator(
                        logEventData,
                        aarVehicleBuilder);

        aarDamageStatusEvaluator.buildDamagedList();
        List<LogDamage> vehiclesDamagedByPlayer = aarDamageStatusEvaluator.getVehiclesDamaged();

        assert(vehiclesDamagedByPlayer.size() == 2);
    }

    @Test
    public void testSetVehiclesDamagedByPlayerWithNoVehicle () throws PWCGException
    {        
        Mockito.when(logDamageEvent1.getVictim()).thenReturn("100");
        Mockito.when(logDamageEvent2.getVictim()).thenReturn("101");
        Mockito.when(logDamageEvent3.getVictim()).thenReturn("101");
        List<IAType2> logParserDamagedEvents = new ArrayList<>();
        logParserDamagedEvents.add(logDamageEvent1);
        logParserDamagedEvents.add(logDamageEvent2);
        logParserDamagedEvents.add(logDamageEvent3);
        Mockito.when(logEventData.getDamageEvents()).thenReturn(logParserDamagedEvents);
        Mockito.when(logEventData.isVehicle("99")).thenReturn(true);
        Mockito.when(logEventData.isVehicle("100")).thenReturn(false);
        Mockito.when(logEventData.isVehicle("101")).thenReturn(true);

        Mockito.when(aarVehicleBuilder.getVehicle("100")).thenReturn(damagedVictim1);
        Mockito.when(aarVehicleBuilder.getVehicle("99")).thenReturn(damagedVictor1);

        
        AARDamageStatusEvaluator aarDamageStatusEvaluator = new AARDamageStatusEvaluator(
                        logEventData,
                        aarVehicleBuilder);

        aarDamageStatusEvaluator.buildDamagedList();
        List<LogDamage> vehiclesDamagedByPlayer = aarDamageStatusEvaluator.getVehiclesDamaged();

        assert(vehiclesDamagedByPlayer.size() == 1);
    }

}
