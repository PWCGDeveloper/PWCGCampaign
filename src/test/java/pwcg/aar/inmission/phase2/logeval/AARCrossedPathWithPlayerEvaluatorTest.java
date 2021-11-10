package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.logfiles.event.AType17;
import pwcg.core.logfiles.event.IAType17;

@ExtendWith(MockitoExtension.class)
public class AARCrossedPathWithPlayerEvaluatorTest
{
    @Mock LogVictory logVictory;
    @Mock LogAIEntity logVictim;
    @Mock LogDamage logDamage;
    @Mock LogAIEntity logDamageVictim;
    @Mock LogAIEntity logDamageVictor;
    @Mock AARPlayerLocator aarPlayerLocator;

    private static List<IAType17> waypointEvents = new ArrayList<>();
    
    public AARCrossedPathWithPlayerEvaluatorTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        waypointEvents = new ArrayList<>();
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        waypointEvents.clear();
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
