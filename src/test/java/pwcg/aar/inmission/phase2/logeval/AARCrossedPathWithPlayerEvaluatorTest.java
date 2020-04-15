package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.inmission.phase1.parse.event.AType17;
import pwcg.aar.inmission.phase1.parse.event.IAType17;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

@RunWith(MockitoJUnitRunner.class)
public class AARCrossedPathWithPlayerEvaluatorTest
{
    @Mock LogVictory logVictory;
    @Mock LogAIEntity logVictim;
    @Mock LogDamage logDamage;
    @Mock LogAIEntity logDamageVictim;
    @Mock LogAIEntity logDamageVictor;
    @Mock AARPlayerLocator aarPlayerLocator;

    List<IAType17> waypointEvents = new ArrayList<>();
    
    @Before
    public void setup () throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        waypointEvents = new ArrayList<>();
    }
    
    @Test
    public void testCrossedPathPlayerDamagedVictim () throws PWCGException
    {        
        AARCrossedPathWithPlayerEvaluator aarCrossedPathWithPlayerEvaluator = new AARCrossedPathWithPlayerEvaluator();
        
        
        boolean crossedPath = aarCrossedPathWithPlayerEvaluator.isCrossedPathWithPlayerFlight(logVictory, aarPlayerLocator, waypointEvents);
        
        assert(crossedPath == true);
    }

    @Test
    public void testCrossedPathVictorDamagedPlayer () throws PWCGException
    {        
        Mockito.when(aarPlayerLocator.closestPlayerDistance(Mockito.any())).thenReturn(AARCrossedPathWithPlayerEvaluator.DISTANCE_TO_CROSS_PLAYER_PATH);
        AARCrossedPathWithPlayerEvaluator aarCrossedPathWithPlayerEvaluator = new AARCrossedPathWithPlayerEvaluator();
        boolean crossedPath = aarCrossedPathWithPlayerEvaluator.isCrossedPathWithPlayerFlight(logVictory, aarPlayerLocator, waypointEvents);
        assert(crossedPath == true);
    }

    @Test
    public void testCrossedPathFail () throws PWCGException
    {        
        Mockito.when(aarPlayerLocator.closestPlayerDistance(Mockito.any())).thenReturn(AARCrossedPathWithPlayerEvaluator.DISTANCE_TO_CROSS_PLAYER_PATH + 1.0);
        AARCrossedPathWithPlayerEvaluator aarCrossedPathWithPlayerEvaluator = new AARCrossedPathWithPlayerEvaluator();
        boolean crossedPath = aarCrossedPathWithPlayerEvaluator.isCrossedPathWithPlayerFlight(logVictory, aarPlayerLocator, waypointEvents);
        assert(crossedPath == false);
    }
    
    @Test
    public void testCrossedPathPlayerFlightWaypoint () throws PWCGException
    {        
        IAType17 waypointEvent = new AType17("T:14605 AType:17 ID:11111 POS(100000.0,0.0,100000.0)");

        waypointEvents.add(waypointEvent);
        
        Mockito.when(logVictory.getLocation()).thenReturn(new Coordinate(100000.0, 0, 100000.0 + AARCrossedPathWithPlayerEvaluator.DISTANCE_TO_CROSS_PLAYER_PATH));

        AARCrossedPathWithPlayerEvaluator aarCrossedPathWithPlayerEvaluator = new AARCrossedPathWithPlayerEvaluator();
        boolean crossedPath = aarCrossedPathWithPlayerEvaluator.isCrossedPathWithPlayerFlight(logVictory, aarPlayerLocator, waypointEvents);
        
        assert(crossedPath == true);
    }
    
    @Test
    public void testCrossedPathPlayerFlightWaypointFailedNotCloseEnough () throws PWCGException
    {        
        IAType17 waypointEvent = new AType17("T:14605 AType:17 ID:11111 POS(100000.0,0.0,100000.0)");
        waypointEvents.add(waypointEvent);
        
        Mockito.when(logVictory.getLocation()).thenReturn(new Coordinate(100000.0, 0, 100000.0 + AARCrossedPathWithPlayerEvaluator.DISTANCE_TO_CROSS_PLAYER_PATH + 1.0));
        Mockito.when(aarPlayerLocator.closestPlayerDistance(Mockito.any())).thenReturn(AARCrossedPathWithPlayerEvaluator.DISTANCE_TO_CROSS_PLAYER_PATH + 1.0);

        AARCrossedPathWithPlayerEvaluator aarCrossedPathWithPlayerEvaluator = new AARCrossedPathWithPlayerEvaluator();
        boolean crossedPath = aarCrossedPathWithPlayerEvaluator.isCrossedPathWithPlayerFlight(logVictory, aarPlayerLocator, waypointEvents);
        
        assert(crossedPath == false);
    }

}
