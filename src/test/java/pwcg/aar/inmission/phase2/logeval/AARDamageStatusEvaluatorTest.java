package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase1.parse.AARLogParser;
import pwcg.aar.inmission.phase1.parse.event.AType2;
import pwcg.aar.inmission.phase1.parse.event.IAType2;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class AARDamageStatusEvaluatorTest
{
    @Mock private LogDamage damagedEntity;
    
    @Mock private LogAIEntity damagedVictim1;
    @Mock private LogAIEntity damagedVictim2;

    @Mock private LogAIEntity damagedVictor1;
    @Mock private LogAIEntity damagedVictor2;
    
    @Mock private AARLogEventData logEventData;
    
    @Mock private AARVehicleBuilder aarVehicleBuilder;
    
    @Mock private AType2 logDamageEvent1;
    @Mock private AType2 logDamageEvent2;
    @Mock private AType2 logDamageEvent3;
    @Mock private AType2 logDamageEvent4;
    
    @Before
    public void setup () throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        
        Mockito.when(logDamageEvent1.getVictim()).thenReturn("100");
        Mockito.when(logDamageEvent1.getVictor()).thenReturn("99");

        Mockito.when(logDamageEvent2.getVictim()).thenReturn("101");
        Mockito.when(logDamageEvent2.getVictor()).thenReturn("98");

        Mockito.when(logDamageEvent3.getVictim()).thenReturn("101");
        Mockito.when(logDamageEvent3.getVictor()).thenReturn("99");

        Mockito.when(logDamageEvent4.getVictim()).thenReturn("101");
        Mockito.when(logDamageEvent4.getVictor()).thenReturn("98");

        Mockito.when(damagedVictor1.getId()).thenReturn("98");
        Mockito.when(damagedVictor2.getId()).thenReturn("99");
        Mockito.when(aarVehicleBuilder.getVehicle("98")).thenReturn(damagedVictor1);
        Mockito.when(aarVehicleBuilder.getVehicle("99")).thenReturn(damagedVictor2);

        Mockito.when(aarVehicleBuilder.getVehicle("100")).thenReturn(damagedVictim1);
        Mockito.when(aarVehicleBuilder.getVehicle("101")).thenReturn(damagedVictim2);

        Mockito.when(logDamageEvent1.getDamageLevel()).thenReturn(5.0);
        Mockito.when(logDamageEvent2.getDamageLevel()).thenReturn(4.0);
        Mockito.when(logDamageEvent3.getDamageLevel()).thenReturn(6.0);
        Mockito.when(logDamageEvent4.getDamageLevel()).thenReturn(3.0);

        Mockito.when(logEventData.isVehicle("100")).thenReturn(true);
        Mockito.when(logEventData.isVehicle("101")).thenReturn(true);
    }

    @Test
    public void testSetVehiclesDamaged () throws PWCGException
    {        
        List<IAType2> logParserDamagedEvents = new ArrayList<>();
        logParserDamagedEvents.add(logDamageEvent1);
        logParserDamagedEvents.add(logDamageEvent2);
        Mockito.when(logEventData.getDamageEvents()).thenReturn(logParserDamagedEvents);

        Mockito.when(aarVehicleBuilder.getVehicle("100")).thenReturn(damagedVictim1);
        
        AARDamageStatusEvaluator aarDamageStatusEvaluator = new AARDamageStatusEvaluator(
                        logEventData,
                        aarVehicleBuilder);

        aarDamageStatusEvaluator.buildDamagedList();
        List<LogDamage> vehiclesDamaged = aarDamageStatusEvaluator.getAllDamageEvents();

        assert(vehiclesDamaged.size() == 2);
    }

    @Test
    public void testSetVehiclesDamagedWithDuplicates () throws PWCGException
    {        
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

        assert(vehiclesDamaged.size() == 3);
    }

    @Test
    public void testSetVehiclesDamagedWithNoVehicle () throws PWCGException
    {        
        List<IAType2> logParserDamagedEvents = new ArrayList<>();
        logParserDamagedEvents.add(logDamageEvent1);
        logParserDamagedEvents.add(logDamageEvent2);
        logParserDamagedEvents.add(logDamageEvent3);
        Mockito.when(logEventData.getDamageEvents()).thenReturn(logParserDamagedEvents);

        Mockito.when(logEventData.isVehicle("100")).thenReturn(false);

        AARDamageStatusEvaluator aarDamageStatusEvaluator = new AARDamageStatusEvaluator(
                        logEventData,
                        aarVehicleBuilder);

        aarDamageStatusEvaluator.buildDamagedList();
        List<LogDamage> vehiclesDamaged = aarDamageStatusEvaluator.getAllDamageEvents();

        assert(vehiclesDamaged.size() == 2);
    }

    @Test
    public void testSetVehiclesDamagedWithUnknownVictor () throws PWCGException
    {        
        Mockito.when(logDamageEvent1.getVictor()).thenReturn(AARLogParser.UNKNOWN_MISSION_LOG_ENTITY);
        Mockito.when(logDamageEvent3.getVictor()).thenReturn(AARLogParser.UNKNOWN_MISSION_LOG_ENTITY);

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

        assert(vehiclesDamaged.size() == 1);
    }

    @Test
    public void testSetVehiclesDamagedWithUnknownVictim() throws PWCGException
    {        
        Mockito.when(logDamageEvent1.getVictim()).thenReturn(AARLogParser.UNKNOWN_MISSION_LOG_ENTITY);
        Mockito.when(logDamageEvent3.getVictim()).thenReturn(AARLogParser.UNKNOWN_MISSION_LOG_ENTITY);

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

        assert(vehiclesDamaged.size() == 1);
    }

    @Test
    public void testVictorByDamageLevel() throws PWCGException
    {        
        List<IAType2> logParserDamagedEvents = new ArrayList<>();
        logParserDamagedEvents.add(logDamageEvent1);
        logParserDamagedEvents.add(logDamageEvent2);
        logParserDamagedEvents.add(logDamageEvent3);
        logParserDamagedEvents.add(logDamageEvent4);
        Mockito.when(logEventData.getDamageEvents()).thenReturn(logParserDamagedEvents);

        AARDamageStatusEvaluator aarDamageStatusEvaluator = new AARDamageStatusEvaluator(
                        logEventData,
                        aarVehicleBuilder);

        aarDamageStatusEvaluator.buildDamagedList();
        List<LogDamage> vehiclesDamaged = aarDamageStatusEvaluator.getAllDamageEvents();

        assert(vehiclesDamaged.size() == 4);
        
        LogAIEntity victor100 = aarDamageStatusEvaluator.getVictorByDamage(damagedVictim1);
        assert(victor100.getId().equals("99"));
        
        LogAIEntity victor101 = aarDamageStatusEvaluator.getVictorByDamage(damagedVictim2);
        assert(victor101.getId().equals("98"));
    }

}
