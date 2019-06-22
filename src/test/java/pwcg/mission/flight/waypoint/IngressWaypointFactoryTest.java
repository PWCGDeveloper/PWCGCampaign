package pwcg.mission.flight.waypoint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

@RunWith(MockitoJUnitRunner.class)
public class IngressWaypointFactoryTest
{
    @Mock
    private Flight flight;
    
    @Mock
    private Coordinate lastPosition;
    
    @Mock
    private Coordinate targetCoords;
    
    @Test
    public void testBombingMissionForPlayer() throws PWCGException, PWCGException
    {
        Mockito.when(flight.isPlayerFlight()).thenReturn(true);
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.BOMB);
        
        IIngressWaypoint ingressWaypointGenerator = IngressWaypointFactory.getIngressGenerator(flight, lastPosition, targetCoords, 320, 3000);
        assert (ingressWaypointGenerator instanceof IngressWaypointEscortedFlight);
    }

    @Test
    public void testBombingMissionFoAI() throws PWCGException, PWCGException
    {
        Mockito.when(flight.isPlayerFlight()).thenReturn(false);
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.STRATEGIC_BOMB);

        IIngressWaypoint ingressWaypointGenerator = IngressWaypointFactory.getIngressGenerator(flight, lastPosition, targetCoords, 320, 3000);
        assert (ingressWaypointGenerator instanceof IngressWaypointNearTarget);
    }

    @Test
    public void testPlayerFighterPatrolMissionNearFront() throws PWCGException, PWCGException
    {
        Mockito.when(flight.isPlayerFlight()).thenReturn(true);
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.PATROL);

        IIngressWaypoint ingressWaypointGenerator = IngressWaypointFactory.getIngressGenerator(flight, lastPosition, targetCoords, 320, 3000);
        assert (ingressWaypointGenerator instanceof IngressWaypointNearFront);
    }

    @Test
    public void testAiOffensivePatrolMissionNearFront() throws PWCGException, PWCGException
    {
        Mockito.when(flight.isPlayerFlight()).thenReturn(false);
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.OFFENSIVE);

        IIngressWaypoint ingressWaypointGenerator = IngressWaypointFactory.getIngressGenerator(flight, lastPosition, targetCoords, 320, 3000);
        assert (ingressWaypointGenerator instanceof IngressWaypointNearFront);
    }

    @Test
    public void testAiGroundAttackMissionNearFront() throws PWCGException, PWCGException
    {
        Mockito.when(flight.isPlayerFlight()).thenReturn(false);
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.GROUND_ATTACK);

        IIngressWaypoint ingressWaypointGenerator = IngressWaypointFactory.getIngressGenerator(flight, lastPosition, targetCoords, 320, 3000);
        assert (ingressWaypointGenerator instanceof IngressWaypointNearFront);
    }

    @Test
    public void testHomeDefense() throws PWCGException, PWCGException
    {
        Mockito.when(flight.isPlayerFlight()).thenReturn(false);
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.HOME_DEFENSE);

        IIngressWaypoint ingressWaypointGenerator = IngressWaypointFactory.getIngressGenerator(flight, lastPosition, targetCoords, 320, 3000);
        assert (ingressWaypointGenerator instanceof IngressWaypointNearTarget);
    }

    @Test
    public void testScrambleOppose() throws PWCGException, PWCGException
    {
        Mockito.when(flight.isPlayerFlight()).thenReturn(false);
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.SCRAMBLE_OPPOSE);

        IIngressWaypoint ingressWaypointGenerator = IngressWaypointFactory.getIngressGenerator(flight, lastPosition, targetCoords, 320, 3000);
        assert (ingressWaypointGenerator instanceof IngressWaypointScrambleOpposition);
    }
}
