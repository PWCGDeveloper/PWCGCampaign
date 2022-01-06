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
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.AType2;
import pwcg.core.logfiles.event.IAType2;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AARDamageStatusEvaluatorTest
{
    private static final String VICTOR_ID_2 = "99";
    private static final String VICTOR_ID_1 = "98";
    private static final String VICTIM_ID_2 = "101";
    private static final String VICTIM_ID_1 = "100";
    
    private LogPlane damagedVictim1 = new LogPlane(1);
    private LogPlane damagedVictim2 = new LogPlane(1);

    private LogPlane damagedVictor1 = new LogPlane(1);
    private LogPlane damagedVictor2 = new LogPlane(1);
    
    @Mock private LogEventData logEventData;
    @Mock private AARVehicleBuilder aarVehicleBuilder;
    
    @BeforeEach
    public void setup () throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        
        damagedVictim1.setId(VICTIM_ID_1);
        damagedVictim2.setId(VICTIM_ID_2);
        
        damagedVictor1.setId(VICTOR_ID_1);
        damagedVictor2.setId(VICTOR_ID_2);

        Mockito.when(aarVehicleBuilder.getVehicle(VICTOR_ID_1)).thenReturn(damagedVictor1);
        Mockito.when(aarVehicleBuilder.getVehicle(VICTOR_ID_2)).thenReturn(damagedVictor2);

        Mockito.when(aarVehicleBuilder.getVehicle(VICTIM_ID_1)).thenReturn(damagedVictim1);
        Mockito.when(aarVehicleBuilder.getVehicle(VICTIM_ID_2)).thenReturn(damagedVictim2);

        Mockito.when(logEventData.isVehicle(VICTIM_ID_1)).thenReturn(true);
        Mockito.when(logEventData.isVehicle(VICTIM_ID_2)).thenReturn(true);
        Mockito.when(logEventData.isVehicle(VICTOR_ID_1)).thenReturn(true);
        Mockito.when(logEventData.isVehicle(VICTOR_ID_2)).thenReturn(true);
    }

    @Test
    public void testSetVehiclesDamaged () throws PWCGException
    {        
        
        AType2 logDamageEvent1 = new AType2("T:80867 AType:2 DMG:0.5 AID:99 TID:100 POS(155995.594,172.822,21908.119)");
        AType2 logDamageEvent2 = new AType2("T:80867 AType:2 DMG:0.4 AID:98 TID:101 POS(155995.594,172.822,21908.119)");

        List<IAType2> logParserDamagedEvents = new ArrayList<>();
        logParserDamagedEvents.add(logDamageEvent1);
        logParserDamagedEvents.add(logDamageEvent2);
        Mockito.when(logEventData.getDamageEvents()).thenReturn(logParserDamagedEvents);

        Mockito.when(aarVehicleBuilder.getVehicle(VICTIM_ID_1)).thenReturn(damagedVictim1);
        
        AARDamageStatusEvaluator aarDamageStatusEvaluator = new AARDamageStatusEvaluator(
                        logEventData,
                        aarVehicleBuilder);

        aarDamageStatusEvaluator.buildDamagedList();
        List<LogDamage> vehiclesDamaged = aarDamageStatusEvaluator.getAllDamageEvents();

        Assertions.assertTrue(vehiclesDamaged.size() == 2);
    }

    @Test
    public void testSetVehiclesDamagedWithDuplicates () throws PWCGException
    {        
        
        AType2 logDamageEvent1 = new AType2("T:80867 AType:2 DMG:0.5 AID:99 TID:100 POS(155995.594,172.822,21908.119)");
        AType2 logDamageEvent2 = new AType2("T:80867 AType:2 DMG:0.4 AID:98 TID:101 POS(155995.594,172.822,21908.119)");
        AType2 logDamageEvent3 = new AType2("T:80867 AType:2 DMG:0.6 AID:99 TID:101 POS(155995.594,172.822,21908.119)");

        List<IAType2> logParserDamagedEvents = new ArrayList<>();
        logParserDamagedEvents.add(logDamageEvent1);
        logParserDamagedEvents.add(logDamageEvent2);
        logParserDamagedEvents.add(logDamageEvent3);
        Mockito.when(logEventData.getDamageEvents()).thenReturn(logParserDamagedEvents);

        AARDamageStatusEvaluator aarDamageStatusEvaluator = new AARDamageStatusEvaluator(
                        logEventData,
                        aarVehicleBuilder);

        aarDamageStatusEvaluator.buildDamagedList();
        List<LogDamage> vehiclesDamaged = aarDamageStatusEvaluator.getAllDamageEvents();

        Assertions.assertTrue(vehiclesDamaged.size() == 3);
    }

    @Test
    public void testSetVehiclesDamagedWithNoVehicle () throws PWCGException
    {        
        
        AType2 logDamageEvent1 = new AType2("T:80867 AType:2 DMG:0.5 AID:99 TID:100 POS(155995.594,172.822,21908.119)");
        AType2 logDamageEvent2 = new AType2("T:80867 AType:2 DMG:0.4 AID:98 TID:101 POS(155995.594,172.822,21908.119)");
        AType2 logDamageEvent3 = new AType2("T:80867 AType:2 DMG:0.6 AID:99 TID:101 POS(155995.594,172.822,21908.119)");

        List<IAType2> logParserDamagedEvents = new ArrayList<>();
        logParserDamagedEvents.add(logDamageEvent1);
        logParserDamagedEvents.add(logDamageEvent2);
        logParserDamagedEvents.add(logDamageEvent3);
        Mockito.when(logEventData.getDamageEvents()).thenReturn(logParserDamagedEvents);

        Mockito.when(logEventData.isVehicle(VICTIM_ID_1)).thenReturn(false);

        AARDamageStatusEvaluator aarDamageStatusEvaluator = new AARDamageStatusEvaluator(
                        logEventData,
                        aarVehicleBuilder);

        aarDamageStatusEvaluator.buildDamagedList();
        List<LogDamage> vehiclesDamaged = aarDamageStatusEvaluator.getAllDamageEvents();

        Assertions.assertTrue(vehiclesDamaged.size() == 2);
    }

    @Test
    public void testSetVehiclesDamagedWithUnknownVictor () throws PWCGException
    {        
        AType2 logDamageEvent1 = new AType2("T:80867 AType:2 DMG:0.5 AID:-1 TID:100 POS(155995.594,172.822,21908.119)");
        AType2 logDamageEvent2 = new AType2("T:80867 AType:2 DMG:0.4 AID:98 TID:101 POS(155995.594,172.822,21908.119)");
        AType2 logDamageEvent3 = new AType2("T:80867 AType:2 DMG:0.6 AID:-1 TID:101 POS(155995.594,172.822,21908.119)");

        List<IAType2> logParserDamagedEvents = new ArrayList<>();
        logParserDamagedEvents.add(logDamageEvent1);
        logParserDamagedEvents.add(logDamageEvent2);
        logParserDamagedEvents.add(logDamageEvent3);
        Mockito.when(logEventData.getDamageEvents()).thenReturn(logParserDamagedEvents);

        AARDamageStatusEvaluator aarDamageStatusEvaluator = new AARDamageStatusEvaluator(
                        logEventData,
                        aarVehicleBuilder);

        aarDamageStatusEvaluator.buildDamagedList();
        List<LogDamage> vehiclesDamaged = aarDamageStatusEvaluator.getAllDamageEvents();

        Assertions.assertTrue(vehiclesDamaged.size() == 1);
    }

    @Test
    public void testSetVehiclesDamagedWithUnknownVictim() throws PWCGException
    {        
        AType2 logDamageEvent1 = new AType2("T:80867 AType:2 DMG:0.5 AID:-1 TID:100 POS(155995.594,172.822,21908.119)");
        AType2 logDamageEvent2 = new AType2("T:80867 AType:2 DMG:0.4 AID:98 TID:101 POS(155995.594,172.822,21908.119)");
        AType2 logDamageEvent3 = new AType2("T:80867 AType:2 DMG:0.6 AID:-1 TID:101 POS(155995.594,172.822,21908.119)");

        List<IAType2> logParserDamagedEvents = new ArrayList<>();
        logParserDamagedEvents.add(logDamageEvent1);
        logParserDamagedEvents.add(logDamageEvent2);
        logParserDamagedEvents.add(logDamageEvent3);
        Mockito.when(logEventData.getDamageEvents()).thenReturn(logParserDamagedEvents);


        AARDamageStatusEvaluator aarDamageStatusEvaluator = new AARDamageStatusEvaluator(
                        logEventData,
                        aarVehicleBuilder);

        aarDamageStatusEvaluator.buildDamagedList();
        List<LogDamage> vehiclesDamaged = aarDamageStatusEvaluator.getAllDamageEvents();

        Assertions.assertTrue(vehiclesDamaged.size() == 1);
    }

    @Test
    public void testVictorByDamageLevel() throws PWCGException
    {        
        AType2 logDamageEvent1 = new AType2("T:80867 AType:2 DMG:0.5 AID:98 TID:100 POS(155995.594,172.822,21908.119)");
        AType2 logDamageEvent2 = new AType2("T:80867 AType:2 DMG:0.6 AID:99 TID:100 POS(155995.594,172.822,21908.119)");
        AType2 logDamageEvent3 = new AType2("T:80867 AType:2 DMG:0.4 AID:99 TID:101 POS(155995.594,172.822,21908.119)");
        AType2 logDamageEvent4 = new AType2("T:80867 AType:2 DMG:0.3 AID:98 TID:101 POS(155995.594,172.822,21908.119)");
        AType2 logDamageEvent5 = new AType2("T:80867 AType:2 DMG:0.3 AID:98 TID:101 POS(155995.594,172.822,21908.119)");

        List<IAType2> logParserDamagedEvents = new ArrayList<>();
        logParserDamagedEvents.add(logDamageEvent1);
        logParserDamagedEvents.add(logDamageEvent2);
        logParserDamagedEvents.add(logDamageEvent3);
        logParserDamagedEvents.add(logDamageEvent4);
        logParserDamagedEvents.add(logDamageEvent5);
        Mockito.when(logEventData.getDamageEvents()).thenReturn(logParserDamagedEvents);

        AARDamageStatusEvaluator aarDamageStatusEvaluator = new AARDamageStatusEvaluator(
                        logEventData,
                        aarVehicleBuilder);

        aarDamageStatusEvaluator.buildDamagedList();
        List<LogDamage> chronologicalDamageEvents = aarDamageStatusEvaluator.getAllDamageEvents();

        Assertions.assertTrue(chronologicalDamageEvents.size() == 5);
        
        LogAIEntity victor100 = aarDamageStatusEvaluator.getVictorByDamage(damagedVictim1);
        Assertions.assertTrue(victor100.getId().equals(VICTOR_ID_2));
        
        LogAIEntity victor101 = aarDamageStatusEvaluator.getVictorByDamage(damagedVictim2);
        Assertions.assertTrue(victor101.getId().equals(VICTOR_ID_1));
    }

}
