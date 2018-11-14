package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase1.parse.event.IAType17;
import pwcg.aar.inmission.phase1.parse.event.rof.AType17;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

@RunWith(MockitoJUnitRunner.class)
public class AARCrossedPathWithPlayerEvaluatorTest
{
    @Mock
    private Campaign campaign;

    @Mock
    LogVictory logVictory;

    @Mock
    LogAIEntity logVictim;

    @Mock
    LogDamage logDamage;

    @Mock
    LogPlane logDamageVictim;

    @Mock
    LogPlane logDamageVictor;

    List<LogDamage> vehiclesDamagedByPlayer = new ArrayList<>();
    List<IAType17> waypointEvents = new ArrayList<>();
    
    @Before
    public void setup () throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        vehiclesDamagedByPlayer = new ArrayList<>();
        waypointEvents = new ArrayList<>();
    }
    
    @Test
    public void testCrossedPathPlayerDamagedVictim () throws PWCGException
    {        
        AARCrossedPathWithPlayerEvaluator aarCrossedPathWithPlayerEvaluator = new AARCrossedPathWithPlayerEvaluator(campaign);
        Mockito.when(logVictory.getVictim()).thenReturn(logVictim);
        Mockito.when(logDamage.getVictim()).thenReturn(logDamageVictim);
        Mockito.when(logDamage.getVictor()).thenReturn(logDamageVictor);
        
        Mockito.when(logVictim.getId()).thenReturn("100");
        Mockito.when(logDamageVictim.getId()).thenReturn("100");
        Mockito.when(logDamageVictor.getId()).thenReturn("99");
        Mockito.when(logDamageVictor.isLogPlaneFromPlayerSquadron(campaign)).thenReturn(true);
        
        vehiclesDamagedByPlayer.add(logDamage);
        boolean crossedPath = aarCrossedPathWithPlayerEvaluator.isCrossedPathWithPlayerFlight(logVictory, vehiclesDamagedByPlayer, waypointEvents);
        
        assert(crossedPath == true);
    }

    @Test
    public void testCrossedPathVictorDamagedPlayer () throws PWCGException
    {        
        AARCrossedPathWithPlayerEvaluator aarCrossedPathWithPlayerEvaluator = new AARCrossedPathWithPlayerEvaluator(campaign);
        Mockito.when(logVictory.getVictim()).thenReturn(logVictim);
        Mockito.when(logDamage.getVictim()).thenReturn(logDamageVictim);
        Mockito.when(logDamage.getVictor()).thenReturn(logDamageVictor);
        
        Mockito.when(logVictim.getId()).thenReturn("100");
        Mockito.when(logDamageVictim.getId()).thenReturn("99");
        Mockito.when(logDamageVictim.isLogPlaneFromPlayerSquadron(campaign)).thenReturn(true);
        Mockito.when(logDamageVictor.getId()).thenReturn("100");

        vehiclesDamagedByPlayer.add(logDamage);
        boolean crossedPath = aarCrossedPathWithPlayerEvaluator.isCrossedPathWithPlayerFlight(logVictory, vehiclesDamagedByPlayer, waypointEvents);
        
        assert(crossedPath == true);
    }

    @Test
    public void testCrossedPathFail () throws PWCGException
    {        
        AARCrossedPathWithPlayerEvaluator aarCrossedPathWithPlayerEvaluator = new AARCrossedPathWithPlayerEvaluator(campaign);
        Mockito.when(logVictory.getVictim()).thenReturn(logVictim);
        Mockito.when(logDamage.getVictim()).thenReturn(logDamageVictim);
        Mockito.when(logDamage.getVictor()).thenReturn(logDamageVictor);
        
        Mockito.when(logVictim.getId()).thenReturn("100");
        Mockito.when(logDamageVictim.getId()).thenReturn("99");
        Mockito.when(logDamageVictor.getId()).thenReturn("101");
        vehiclesDamagedByPlayer.add(logDamage);
        boolean crossedPath = aarCrossedPathWithPlayerEvaluator.isCrossedPathWithPlayerFlight(logVictory, vehiclesDamagedByPlayer, waypointEvents);
        
        assert(crossedPath == false);
    }

    @Test
    public void testCrossedPathFailOtherDamagedVictim () throws PWCGException
    {
        AARCrossedPathWithPlayerEvaluator aarCrossedPathWithPlayerEvaluator = new AARCrossedPathWithPlayerEvaluator(campaign);
        Mockito.when(logVictory.getVictim()).thenReturn(logVictim);
        Mockito.when(logDamage.getVictim()).thenReturn(logDamageVictim);
        Mockito.when(logDamage.getVictor()).thenReturn(logDamageVictor);

        Mockito.when(logVictim.getId()).thenReturn("100");
        Mockito.when(logDamageVictim.getId()).thenReturn("100");
        Mockito.when(logDamageVictor.getId()).thenReturn("101");
        Mockito.when(logDamageVictor.isLogPlaneFromPlayerSquadron(campaign)).thenReturn(false);
        vehiclesDamagedByPlayer.add(logDamage);
        boolean crossedPath = aarCrossedPathWithPlayerEvaluator.isCrossedPathWithPlayerFlight(logVictory, vehiclesDamagedByPlayer, waypointEvents);

        assert(crossedPath == false);
    }

    @Test
    public void testCrossedPathFailVictimDamagedOther () throws PWCGException
    {
        AARCrossedPathWithPlayerEvaluator aarCrossedPathWithPlayerEvaluator = new AARCrossedPathWithPlayerEvaluator(campaign);
        Mockito.when(logVictory.getVictim()).thenReturn(logVictim);
        Mockito.when(logDamage.getVictim()).thenReturn(logDamageVictim);
        Mockito.when(logDamage.getVictor()).thenReturn(logDamageVictor);

        Mockito.when(logVictim.getId()).thenReturn("100");
        Mockito.when(logDamageVictim.getId()).thenReturn("101");
        Mockito.when(logDamageVictim.isLogPlaneFromPlayerSquadron(campaign)).thenReturn(false);
        Mockito.when(logDamageVictor.getId()).thenReturn("100");
        vehiclesDamagedByPlayer.add(logDamage);
        boolean crossedPath = aarCrossedPathWithPlayerEvaluator.isCrossedPathWithPlayerFlight(logVictory, vehiclesDamagedByPlayer, waypointEvents);

        assert(crossedPath == false);
    }

    @Test
    public void testCrossedPathFailBecauseOfMissingEntity () throws PWCGException
    {        
        AARCrossedPathWithPlayerEvaluator aarCrossedPathWithPlayerEvaluator = new AARCrossedPathWithPlayerEvaluator(campaign);
        Mockito.when(logVictory.getVictim()).thenReturn(logVictim);
        Mockito.when(logDamage.getVictim()).thenReturn(logDamageVictim);
        
        Mockito.when(logVictim.getId()).thenReturn("100");
        Mockito.when(logDamageVictim.getId()).thenReturn("99");
        vehiclesDamagedByPlayer.add(logDamage);
        boolean crossedPath = aarCrossedPathWithPlayerEvaluator.isCrossedPathWithPlayerFlight(logVictory, vehiclesDamagedByPlayer, waypointEvents);
        
        assert(crossedPath == false);
    }
    
    @Test
    public void testCrossedPathPlayerFlightWaypoint () throws PWCGException
    {        
        IAType17 waypointEvent = new AType17("T:14605 AType:17 ID:11111 POS(100000.0,0.0,100000.0)");

        waypointEvents.add(waypointEvent);
        
        Mockito.when(logVictory.getLocation()).thenReturn(new Coordinate(100000.0, 0, 100000.0 + AARCrossedPathWithPlayerEvaluator.DISTANCE_TO_CROSS_PLAYER_PATH));

        AARCrossedPathWithPlayerEvaluator aarCrossedPathWithPlayerEvaluator = new AARCrossedPathWithPlayerEvaluator(campaign);
        boolean crossedPath = aarCrossedPathWithPlayerEvaluator.isCrossedPathWithPlayerFlight(logVictory, vehiclesDamagedByPlayer, waypointEvents);
        
        assert(crossedPath == true);
    }
    
    @Test
    public void testCrossedPathPlayerFlightWaypointFailedNotCloseEnough () throws PWCGException
    {        
        IAType17 waypointEvent = new AType17("T:14605 AType:17 ID:11111 POS(100000.0,0.0,100000.0)");
        waypointEvents.add(waypointEvent);
        
        Mockito.when(logVictory.getLocation()).thenReturn(new Coordinate(100000.0, 0, 100000.0 + AARCrossedPathWithPlayerEvaluator.DISTANCE_TO_CROSS_PLAYER_PATH + 1.0));

        AARCrossedPathWithPlayerEvaluator aarCrossedPathWithPlayerEvaluator = new AARCrossedPathWithPlayerEvaluator(campaign);
        boolean crossedPath = aarCrossedPathWithPlayerEvaluator.isCrossedPathWithPlayerFlight(logVictory, vehiclesDamagedByPlayer, waypointEvents);
        
        assert(crossedPath == false);
    }

}
