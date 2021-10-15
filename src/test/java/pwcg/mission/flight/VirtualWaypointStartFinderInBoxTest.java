package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.Mission;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.flight.waypoint.virtual.VirtualWaypointStartFinder;

@ExtendWith(MockitoExtension.class)
public class VirtualWaypointStartFinderInBoxTest
{
    @Mock private IFlight flight;
    @Mock private Mission mission;
    @Mock VirtualWayPointCoordinate vwpCoordinate1;
    @Mock VirtualWayPointCoordinate vwpCoordinate2;
    @Mock VirtualWayPointCoordinate vwpCoordinate3;
    @Mock VirtualWayPointCoordinate vwpCoordinate4;
    @Mock VirtualWayPointCoordinate vwpCoordinate5;
    @Mock VirtualWayPointCoordinate vwpCoordinate6;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        Mockito.when(flight.getMission()).thenReturn(mission);

        CoordinateBox missionBox;
        missionBox = CoordinateBox.coordinateBoxFromCorners(new Coordinate(100, 0, 100), new Coordinate(200, 0, 200));
        Mockito.when(mission.getMissionBorders()).thenReturn(missionBox);        
    }

    @Test
    public void inBoxBeginAndEnd() throws PWCGException
    {
        Coordinate coordinate1 = new Coordinate(99, 0, 101);
        Coordinate coordinate2 = new Coordinate(101, 0, 99);
        Coordinate coordinate3 = new Coordinate(101, 0, 101);
        Coordinate coordinate4 = new Coordinate(199, 0, 199);
        Coordinate coordinate5 = new Coordinate(201, 0, 199);
        Coordinate coordinate6 = new Coordinate(201, 0, 210);
        
        Mockito.when(vwpCoordinate1.getPosition()).thenReturn(coordinate1);        
        Mockito.when(vwpCoordinate2.getPosition()).thenReturn(coordinate2);        
        Mockito.when(vwpCoordinate3.getPosition()).thenReturn(coordinate3);        
        Mockito.when(vwpCoordinate4.getPosition()).thenReturn(coordinate4);        
        Mockito.when(vwpCoordinate5.getPosition()).thenReturn(coordinate5);  
        Mockito.when(vwpCoordinate6.getPosition()).thenReturn(coordinate6);  
        
        List<VirtualWayPointCoordinate> plotCoordinates = new ArrayList<>();
        plotCoordinates.add(vwpCoordinate1);
        plotCoordinates.add(vwpCoordinate2);
        plotCoordinates.add(vwpCoordinate3);
        plotCoordinates.add(vwpCoordinate4);
        plotCoordinates.add(vwpCoordinate5);
        plotCoordinates.add(vwpCoordinate6);

        int startPositionInBox = VirtualWaypointStartFinder.findStartVwpByBox(flight, plotCoordinates);
        assert(startPositionInBox == 2);
        int endPositionInBox = VirtualWaypointStartFinder.findEndVwpByBox(flight, plotCoordinates);
        assert(endPositionInBox == 3);
    }

    @Test
    public void inAndOutOfBoxBoxBeginAndEnd() throws PWCGException
    {
        Coordinate coordinate1 = new Coordinate(99, 0, 101);
        Coordinate coordinate2 = new Coordinate(101, 0, 99);
        Coordinate coordinate3 = new Coordinate(101, 0, 101);
        Coordinate coordinate4 = new Coordinate(201, 0, 210);
        Coordinate coordinate5 = new Coordinate(199, 0, 199);
        Coordinate coordinate6 = new Coordinate(201, 0, 199);
        
        Mockito.when(vwpCoordinate1.getPosition()).thenReturn(coordinate1);        
        Mockito.when(vwpCoordinate2.getPosition()).thenReturn(coordinate2);        
        Mockito.when(vwpCoordinate3.getPosition()).thenReturn(coordinate3);        
        Mockito.when(vwpCoordinate4.getPosition()).thenReturn(coordinate4);        
        Mockito.when(vwpCoordinate5.getPosition()).thenReturn(coordinate5);  
        Mockito.when(vwpCoordinate6.getPosition()).thenReturn(coordinate6);  
        
        List<VirtualWayPointCoordinate> plotCoordinates = new ArrayList<>();
        plotCoordinates.add(vwpCoordinate1);
        plotCoordinates.add(vwpCoordinate2);
        plotCoordinates.add(vwpCoordinate3);
        plotCoordinates.add(vwpCoordinate4);
        plotCoordinates.add(vwpCoordinate5);
        plotCoordinates.add(vwpCoordinate6);

        int startPositionInBox = VirtualWaypointStartFinder.findStartVwpByBox(flight, plotCoordinates);
        assert(startPositionInBox == 2);
        int endPositionInBox = VirtualWaypointStartFinder.findEndVwpByBox(flight, plotCoordinates);
        assert(endPositionInBox == 4);
    }

    @Test
    public void notInBox() throws PWCGException
    {
        Coordinate coordinate1 = new Coordinate(99, 0, 101);
        Coordinate coordinate2 = new Coordinate(101, 0, 99);
        Coordinate coordinate3 = new Coordinate(199, 0, 201);
        Coordinate coordinate4 = new Coordinate(201, 0, 199);
        
        Mockito.when(vwpCoordinate1.getPosition()).thenReturn(coordinate1);        
        Mockito.when(vwpCoordinate2.getPosition()).thenReturn(coordinate2);        
        Mockito.when(vwpCoordinate3.getPosition()).thenReturn(coordinate3);        
        Mockito.when(vwpCoordinate4.getPosition()).thenReturn(coordinate4);        
        
        List<VirtualWayPointCoordinate> plotCoordinates = new ArrayList<>();
        plotCoordinates.add(vwpCoordinate1);
        plotCoordinates.add(vwpCoordinate2);
        plotCoordinates.add(vwpCoordinate4);
        plotCoordinates.add(vwpCoordinate3);

        int startPositionInBox = VirtualWaypointStartFinder.findStartVwpByBox(flight, plotCoordinates);
        assert(startPositionInBox == VirtualWaypointStartFinder.IS_NOT_NEAR_AREA);
        int endPositionInBox = VirtualWaypointStartFinder.findEndVwpByBox(flight, plotCoordinates);
        assert(endPositionInBox == VirtualWaypointStartFinder.IS_NOT_NEAR_AREA);
    }
}
