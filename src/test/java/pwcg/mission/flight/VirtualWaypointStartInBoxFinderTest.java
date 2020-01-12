package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.Mission;
import pwcg.mission.flight.virtual.VirtualWaypointStartFinder;
import pwcg.mission.flight.waypoint.VirtualWayPointCoordinate;

@RunWith(MockitoJUnitRunner.class)
public class VirtualWaypointStartInBoxFinderTest
{
    @Mock private IFlight flight;
    @Mock private Mission mission;
    @Mock VirtualWayPointCoordinate vwpCoordinate1;
    @Mock VirtualWayPointCoordinate vwpCoordinate2;
    @Mock VirtualWayPointCoordinate vwpCoordinate3;
    @Mock VirtualWayPointCoordinate vwpCoordinate4;
    @Mock VirtualWayPointCoordinate vwpCoordinate5;
    List<VirtualWayPointCoordinate> plotCoordinates = new ArrayList<>();
    CoordinateBox missionBox;

    @Before
    public void setup() throws PWCGException
    {
        missionBox = CoordinateBox.coordinateBoxFromCorners(new Coordinate(100, 0, 100), new Coordinate(200, 0, 200));

        Mockito.when(flight.getMission()).thenReturn(mission);
        Mockito.when(mission.getMissionBorders()).thenReturn(missionBox);        
    }

    @Test
    public void inBoxEnd() throws PWCGException
    {
        Coordinate coordinate1 = new Coordinate(99, 0, 101);
        Coordinate coordinate2 = new Coordinate(101, 0, 99);
        Coordinate coordinate3 = new Coordinate(199, 0, 201);
        Coordinate coordinate4 = new Coordinate(201, 0, 199);
        Coordinate coordinate5 = new Coordinate(199, 0, 199);
        
        Mockito.when(vwpCoordinate1.getCoordinate()).thenReturn(coordinate1);        
        Mockito.when(vwpCoordinate2.getCoordinate()).thenReturn(coordinate2);        
        Mockito.when(vwpCoordinate3.getCoordinate()).thenReturn(coordinate3);        
        Mockito.when(vwpCoordinate4.getCoordinate()).thenReturn(coordinate4);        
        Mockito.when(vwpCoordinate5.getCoordinate()).thenReturn(coordinate5);  
        
        plotCoordinates.add(vwpCoordinate1);
        plotCoordinates.add(vwpCoordinate2);
        plotCoordinates.add(vwpCoordinate3);
        plotCoordinates.add(vwpCoordinate4);
        plotCoordinates.add(vwpCoordinate5);

        int positionInBox = VirtualWaypointStartFinder.findFirstVwpInBox(flight, plotCoordinates);
        assert(positionInBox == 4);
        int chosenIndex = VirtualWaypointStartFinder.determineStartVWP(flight, plotCoordinates);
        assert(chosenIndex <= positionInBox);
    }

    @Test
    public void inBoxMiddle() throws PWCGException
    {
        Coordinate coordinate1 = new Coordinate(99, 0, 101);
        Coordinate coordinate2 = new Coordinate(101, 0, 99);
        Coordinate coordinate3 = new Coordinate(199, 0, 201);
        Coordinate coordinate4 = new Coordinate(201, 0, 199);
        Coordinate coordinate5 = new Coordinate(199, 0, 199);
        
        Mockito.when(vwpCoordinate1.getCoordinate()).thenReturn(coordinate1);        
        Mockito.when(vwpCoordinate2.getCoordinate()).thenReturn(coordinate2);        
        Mockito.when(vwpCoordinate3.getCoordinate()).thenReturn(coordinate3);        
        Mockito.when(vwpCoordinate4.getCoordinate()).thenReturn(coordinate4);        
        Mockito.when(vwpCoordinate5.getCoordinate()).thenReturn(coordinate5);  
        
        plotCoordinates.add(vwpCoordinate1);
        plotCoordinates.add(vwpCoordinate2);
        plotCoordinates.add(vwpCoordinate5);
        plotCoordinates.add(vwpCoordinate4);
        plotCoordinates.add(vwpCoordinate3);

        int positionInBox = VirtualWaypointStartFinder.findFirstVwpInBox(flight, plotCoordinates);
        assert(positionInBox == 2);
        int chosenIndex = VirtualWaypointStartFinder.determineStartVWP(flight, plotCoordinates);
        assert(chosenIndex <= positionInBox);
    }

    @Test
    public void notInBox() throws PWCGException
    {
        Coordinate coordinate1 = new Coordinate(99, 0, 101);
        Coordinate coordinate2 = new Coordinate(101, 0, 99);
        Coordinate coordinate3 = new Coordinate(199, 0, 201);
        Coordinate coordinate4 = new Coordinate(201, 0, 199);
        
        Mockito.when(vwpCoordinate1.getCoordinate()).thenReturn(coordinate1);        
        Mockito.when(vwpCoordinate2.getCoordinate()).thenReturn(coordinate2);        
        Mockito.when(vwpCoordinate3.getCoordinate()).thenReturn(coordinate3);        
        Mockito.when(vwpCoordinate4.getCoordinate()).thenReturn(coordinate4);        
        
        plotCoordinates.add(vwpCoordinate1);
        plotCoordinates.add(vwpCoordinate2);
        plotCoordinates.add(vwpCoordinate4);
        plotCoordinates.add(vwpCoordinate3);

        int positionInBox = VirtualWaypointStartFinder.findFirstVwpInBox(flight, plotCoordinates);
        assert(positionInBox == 0);
        int chosenIndex = VirtualWaypointStartFinder.determineStartVWP(flight, plotCoordinates);
        assert(chosenIndex <= positionInBox);
    }

}
