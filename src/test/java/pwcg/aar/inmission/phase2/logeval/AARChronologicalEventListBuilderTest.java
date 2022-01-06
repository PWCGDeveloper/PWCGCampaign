package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBalloon;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBase;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogWaypoint;
import pwcg.aar.inmission.phase2.logeval.victory.AARVictoryEvaluator;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;


@ExtendWith(MockitoExtension.class)
public class AARChronologicalEventListBuilderTest
{
    @Mock private AAREvaluator evaluator;
    @Mock private LogEventData logEventData;
    @Mock private AARWaypointBuilder aarWaypointBuilder;
    @Mock private AARVehicleBuilder aarVehicleBuilder;
    @Mock private AARVictoryEvaluator aarVictoryEvaluator;
    @Mock private AARDamageStatusEvaluator aarDamageStatusEvaluator;

    public AARChronologicalEventListBuilderTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
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
            LogPlane planeSpawn = new LogPlane(i);
            planeAiEntities.put("" + i, planeSpawn);
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
            LogBalloon balloonSpawn = new LogBalloon(i);
            missionResultBalloons.put("" + i, balloonSpawn);
        }
        Mockito.when(aarVehicleBuilder.getLogBalloons()).thenReturn(missionResultBalloons);
        
        return i;
    }

    private int mockVictory(int lastSequenceNumber)
    {
        List<LogVictory> missionResultVictories = new ArrayList<>();
        int i = lastSequenceNumber;
        for (; i < lastSequenceNumber+1; ++i)
        {
            LogVictory victory = new LogVictory(i);
            missionResultVictories.add(victory);
        }
        Mockito.when(aarVictoryEvaluator.getVictoryResults()).thenReturn(missionResultVictories);
        
        return i;
    }

    private int mockDamage(int lastSequenceNumber)
    {
        List<LogDamage> missionResultDamageList = new ArrayList<>();
        int i = lastSequenceNumber;
        for (; i < lastSequenceNumber+8; ++i)
        {
            LogDamage damage = new LogDamage(i);
            missionResultDamageList.add(damage);
        }
        Mockito.when(aarDamageStatusEvaluator.getAllDamageEvents()).thenReturn(missionResultDamageList);
        
        return i;
    }

    private int mockWaypoints(int lastSequenceNumber)
    {
        List<LogWaypoint> missionResultWaypointList = new ArrayList<>();
        int i = lastSequenceNumber;
        for (; i < lastSequenceNumber+5; ++i)
        {
            LogWaypoint missionResultWaypoint = new LogWaypoint(i);
            missionResultWaypointList.add(missionResultWaypoint);
        }
        Mockito.when(aarWaypointBuilder.buildWaypointEvents()).thenReturn(missionResultWaypointList);
        
        return i;
    }

}
