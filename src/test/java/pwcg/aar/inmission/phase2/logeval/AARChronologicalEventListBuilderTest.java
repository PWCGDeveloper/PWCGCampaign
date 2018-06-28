package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase2.logeval.AARChronologicalEventListBuilder;
import pwcg.aar.inmission.phase2.logeval.AARDamageStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.AAREvaluator;
import pwcg.aar.inmission.phase2.logeval.AARVehicleBuilder;
import pwcg.aar.inmission.phase2.logeval.AARWaypointBuilder;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBalloon;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBase;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogWaypoint;
import pwcg.aar.inmission.phase2.logeval.victory.AARVictoryEvaluator;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;


@RunWith(MockitoJUnitRunner.class)
public class AARChronologicalEventListBuilderTest
{
    @Mock
    private AAREvaluator evaluator;

    @Mock
    private AARLogEventData logEventData;

    @Mock
    private AARWaypointBuilder aarWaypointBuilder;
    
    @Mock
    AARVehicleBuilder aarVehicleBuilder;
    
    @Mock
    AARVictoryEvaluator aarVictoryEvaluator;
    
    @Mock
    AARDamageStatusEvaluator aarDamageStatusEvaluator;

    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
    }

    @Test
    public void testChronologicalEventBuild () throws PWCGException
    {     
        int lastSequenceNumber = 1;
        lastSequenceNumber = mockDamage(lastSequenceNumber);
        lastSequenceNumber = mockBalloonSpawn(lastSequenceNumber);
        lastSequenceNumber = mockPlaneSpawn(lastSequenceNumber);
        lastSequenceNumber = mockVictory(lastSequenceNumber);
        lastSequenceNumber = mockWaypoints(lastSequenceNumber);

        Mockito.when(evaluator.getAarVehicleBuilder()).thenReturn(aarVehicleBuilder);
        Mockito.when(evaluator.getAarVictoryEvaluator()).thenReturn(aarVictoryEvaluator);
        Mockito.when(evaluator.getAarDamageStatusEvaluator()).thenReturn(aarDamageStatusEvaluator);
        
        AARChronologicalEventListBuilder aarChronologicalEventListBuilder = new AARChronologicalEventListBuilder(evaluator, aarWaypointBuilder);
        aarChronologicalEventListBuilder.buildChronologicalList();
        List<LogBase> chronologicalEvents = aarChronologicalEventListBuilder.getChronologicalEvents();
        int priorSequenceNumber = -1;
        for (LogBase chronologicalEvent : chronologicalEvents)
        {
            assert((priorSequenceNumber < chronologicalEvent.getSequenceNum()) == true);
            priorSequenceNumber = chronologicalEvent.getSequenceNum();

        }
    }

    private int mockPlaneSpawn(int lastSequenceNumber)
    {
        Map<String, LogPlane> planeAiEntities = new HashMap <>();
        int i = lastSequenceNumber;
        for (; i < lastSequenceNumber+3; ++i)
        {
            LogPlane planeSpawn = new LogPlane();
            planeSpawn.setSequenceNum(i);
        }
        Mockito.when(aarVehicleBuilder.getLogPlanes()).thenReturn(planeAiEntities);
        
        return i;
    }

    private int mockBalloonSpawn(int lastSequenceNumber)
    {
        Map<String, LogBalloon> missionResultBalloons = new HashMap <>();

        int i = lastSequenceNumber;
        for (; i < lastSequenceNumber+2; ++i)
        {
            LogBalloon balloonSpawn = new LogBalloon();
            balloonSpawn.setSequenceNum(i);
        }
        Mockito.when(aarVehicleBuilder.getLogBalloons()).thenReturn(missionResultBalloons);
        
        return i;
    }

    private int mockVictory(int lastSequenceNumber)
    {
        List<LogVictory> missionResultVictoryList = new ArrayList<>();
        int i = lastSequenceNumber;
        for (; i < lastSequenceNumber+1; ++i)
        {
            LogVictory victory = new LogVictory();
            victory.setSequenceNum(i);
        }
        Mockito.when(aarVictoryEvaluator.getVictoryResults()).thenReturn(missionResultVictoryList);
        
        return i;
    }

    private int mockDamage(int lastSequenceNumber)
    {
        List<LogDamage> missionResultDamageList = new ArrayList<>();
        int i = lastSequenceNumber;
        for (; i < lastSequenceNumber+8; ++i)
        {
            LogDamage damage = new LogDamage();
            damage.setSequenceNum(i);
        }
        Mockito.when(aarDamageStatusEvaluator.getVehiclesDamagedByPlayer()).thenReturn(missionResultDamageList);
        
        return i;
    }

    private int mockWaypoints(int lastSequenceNumber)
    {
        List<LogWaypoint> missionResultWaypointList = new ArrayList<>();
        int i = lastSequenceNumber;
        for (; i < lastSequenceNumber+5; ++i)
        {
            LogWaypoint missionResultWaypoint = new LogWaypoint();
            missionResultWaypoint.setSequenceNum(i);
        }
        Mockito.when(aarWaypointBuilder.buildWaypointEvents()).thenReturn(missionResultWaypointList);
        
        return i;
    }

}
