package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.flight.waypoint.virtual.VirtualWaypointConsolidator;
import pwcg.mission.flight.waypoint.virtual.VirtualWaypointStartFinder;

@ExtendWith(MockitoExtension.class)
public class VirtualWaypointConsolidatorTest
{
    @Mock
    private IFlight flight;
    VirtualWayPointCoordinate vwpCoordinate1 = new VirtualWayPointCoordinate(flight);
    VirtualWayPointCoordinate vwpCoordinate2 = new VirtualWayPointCoordinate(flight);
    VirtualWayPointCoordinate vwpCoordinate3 = new VirtualWayPointCoordinate(flight);
    VirtualWayPointCoordinate vwpCoordinate4 = new VirtualWayPointCoordinate(flight);
    VirtualWayPointCoordinate vwpCoordinate5 = new VirtualWayPointCoordinate(flight);
    VirtualWayPointCoordinate vwpCoordinate6 = new VirtualWayPointCoordinate(flight);

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        Coordinate coordinate1 = new Coordinate(99, 0, 101);
        Coordinate coordinate2 = new Coordinate(101, 0, 99);
        Coordinate coordinate3 = new Coordinate(101, 0, 101);
        Coordinate coordinate4 = new Coordinate(199, 0, 199);
        Coordinate coordinate5 = new Coordinate(201, 0, 199);
        Coordinate coordinate6 = new Coordinate(201, 0, 210);

        vwpCoordinate1.setCoordinate(coordinate1);
        vwpCoordinate2.setCoordinate(coordinate2);
        vwpCoordinate3.setCoordinate(coordinate3);
        vwpCoordinate4.setCoordinate(coordinate4);
        vwpCoordinate5.setCoordinate(coordinate5);
        vwpCoordinate6.setCoordinate(coordinate6);

        vwpCoordinate1.setWaypointWaitTimeSeconds(100);
        vwpCoordinate2.setWaypointWaitTimeSeconds(100);
        vwpCoordinate3.setWaypointWaitTimeSeconds(100);
        vwpCoordinate4.setWaypointWaitTimeSeconds(100);
        vwpCoordinate5.setWaypointWaitTimeSeconds(100);
        vwpCoordinate6.setWaypointWaitTimeSeconds(100);

    }

    @Test
    public void keepClosestToFrontToMinimizeVwps() throws PWCGException
    {
        List<VirtualWayPointCoordinate> plotCoordinates = new ArrayList<>();
        plotCoordinates.add(vwpCoordinate1);
        plotCoordinates.add(vwpCoordinate2);
        plotCoordinates.add(vwpCoordinate3);
        plotCoordinates.add(vwpCoordinate4);
        plotCoordinates.add(vwpCoordinate5);
        plotCoordinates.add(vwpCoordinate6);

        try (MockedStatic<VirtualWaypointStartFinder> mocked = Mockito.mockStatic(VirtualWaypointStartFinder.class))
        {
            mocked.when(() -> VirtualWaypointStartFinder.findStartVwpByBox(Mockito.any(), Mockito.any())).thenReturn(1);
            mocked.when(() -> VirtualWaypointStartFinder.findEndVwpByBox(Mockito.any(), Mockito.any())).thenReturn(4);
            mocked.when(() -> VirtualWaypointStartFinder.findStartVwpProximityToFront(Mockito.any(), Mockito.any())).thenReturn(2);
            mocked.when(() -> VirtualWaypointStartFinder.findEndVwpProximityToFront(Mockito.any(), Mockito.any())).thenReturn(3);

            VirtualWaypointConsolidator virtualWaypointConsolidator = new VirtualWaypointConsolidator(flight, plotCoordinates);
            List<VirtualWayPointCoordinate> afterConsolidation = virtualWaypointConsolidator.consolidatedVirtualWaypoints();

            assert (afterConsolidation.size() == 2);
            assert (afterConsolidation.get(0).getWaypointWaitTimeSeconds() == 300);
            assert (afterConsolidation.get(1).getWaypointWaitTimeSeconds() == 100);
        }
    }

    @Test
    public void keepClosestToBoxToMinimizeVwps() throws PWCGException
    {
        List<VirtualWayPointCoordinate> plotCoordinates = new ArrayList<>();
        plotCoordinates.add(vwpCoordinate1);
        plotCoordinates.add(vwpCoordinate2);
        plotCoordinates.add(vwpCoordinate3);
        plotCoordinates.add(vwpCoordinate4);
        plotCoordinates.add(vwpCoordinate5);
        plotCoordinates.add(vwpCoordinate6);

        try (MockedStatic<VirtualWaypointStartFinder> mocked = Mockito.mockStatic(VirtualWaypointStartFinder.class))
        {
            mocked.when(() -> VirtualWaypointStartFinder.findStartVwpByBox(Mockito.any(), Mockito.any())).thenReturn(2);
            mocked.when(() -> VirtualWaypointStartFinder.findEndVwpByBox(Mockito.any(), Mockito.any())).thenReturn(3);
            mocked.when(() -> VirtualWaypointStartFinder.findStartVwpProximityToFront(Mockito.any(), Mockito.any())).thenReturn(1);
            mocked.when(() -> VirtualWaypointStartFinder.findEndVwpProximityToFront(Mockito.any(), Mockito.any())).thenReturn(4);

            VirtualWaypointConsolidator virtualWaypointConsolidator = new VirtualWaypointConsolidator(flight, plotCoordinates);
            List<VirtualWayPointCoordinate> afterConsolidation = virtualWaypointConsolidator.consolidatedVirtualWaypoints();

            assert (afterConsolidation.size() == 2);

            assert (afterConsolidation.size() == 2);
            assert (afterConsolidation.get(0).getWaypointWaitTimeSeconds() == 300);
            assert (afterConsolidation.get(1).getWaypointWaitTimeSeconds() == 100);
        }
    }

    @Test
    public void keepClosestToBoxbecauseNoFront() throws PWCGException
    {
        List<VirtualWayPointCoordinate> plotCoordinates = new ArrayList<>();
        plotCoordinates.add(vwpCoordinate1);
        plotCoordinates.add(vwpCoordinate2);
        plotCoordinates.add(vwpCoordinate3);
        plotCoordinates.add(vwpCoordinate4);
        plotCoordinates.add(vwpCoordinate5);
        plotCoordinates.add(vwpCoordinate6);

        try (MockedStatic<VirtualWaypointStartFinder> mocked = Mockito.mockStatic(VirtualWaypointStartFinder.class))
        {
            mocked.when(() -> VirtualWaypointStartFinder.findStartVwpByBox(Mockito.any(), Mockito.any())).thenReturn(1);
            mocked.when(() -> VirtualWaypointStartFinder.findEndVwpByBox(Mockito.any(), Mockito.any())).thenReturn(4);
            mocked.when(() -> VirtualWaypointStartFinder.findStartVwpProximityToFront(Mockito.any(), Mockito.any()))
                    .thenReturn(VirtualWaypointStartFinder.IS_NOT_NEAR_AREA);
            mocked.when(() -> VirtualWaypointStartFinder.findEndVwpProximityToFront(Mockito.any(), Mockito.any()))
                    .thenReturn(VirtualWaypointStartFinder.IS_NOT_NEAR_AREA);

            VirtualWaypointConsolidator virtualWaypointConsolidator = new VirtualWaypointConsolidator(flight, plotCoordinates);
            List<VirtualWayPointCoordinate> afterConsolidation = virtualWaypointConsolidator.consolidatedVirtualWaypoints();

            assert (afterConsolidation.size() == 4);
            assert (afterConsolidation.get(0).getWaypointWaitTimeSeconds() == 200);
            assert (afterConsolidation.get(1).getWaypointWaitTimeSeconds() == 100);
        }
    }

    @Test
    public void keepClosestToFrontbecauseNoBox() throws PWCGException
    {
        List<VirtualWayPointCoordinate> plotCoordinates = new ArrayList<>();
        plotCoordinates.add(vwpCoordinate1);
        plotCoordinates.add(vwpCoordinate2);
        plotCoordinates.add(vwpCoordinate3);
        plotCoordinates.add(vwpCoordinate4);
        plotCoordinates.add(vwpCoordinate5);
        plotCoordinates.add(vwpCoordinate6);

        try (MockedStatic<VirtualWaypointStartFinder> mocked = Mockito.mockStatic(VirtualWaypointStartFinder.class))
        {
            mocked.when(() -> VirtualWaypointStartFinder.findStartVwpByBox(Mockito.any(), Mockito.any()))
                    .thenReturn(VirtualWaypointStartFinder.IS_NOT_NEAR_AREA);
            mocked.when(() -> VirtualWaypointStartFinder.findEndVwpByBox(Mockito.any(), Mockito.any())).thenReturn(VirtualWaypointStartFinder.IS_NOT_NEAR_AREA);
            mocked.when(() -> VirtualWaypointStartFinder.findStartVwpProximityToFront(Mockito.any(), Mockito.any())).thenReturn(0);
            mocked.when(() -> VirtualWaypointStartFinder.findEndVwpProximityToFront(Mockito.any(), Mockito.any())).thenReturn(5);

            VirtualWaypointConsolidator virtualWaypointConsolidator = new VirtualWaypointConsolidator(flight, plotCoordinates);
            List<VirtualWayPointCoordinate> afterConsolidation = virtualWaypointConsolidator.consolidatedVirtualWaypoints();

            assert (afterConsolidation.size() == 6);
            assert (afterConsolidation.get(0).getWaypointWaitTimeSeconds() == 100);
            assert (afterConsolidation.get(1).getWaypointWaitTimeSeconds() == 100);
        }
    }

    @Test
    public void keepOnlyOneBecauseNotlLoseTopAnything() throws PWCGException
    {
        List<VirtualWayPointCoordinate> plotCoordinates = new ArrayList<>();
        plotCoordinates.add(vwpCoordinate1);
        plotCoordinates.add(vwpCoordinate2);
        plotCoordinates.add(vwpCoordinate3);
        plotCoordinates.add(vwpCoordinate4);
        plotCoordinates.add(vwpCoordinate5);
        plotCoordinates.add(vwpCoordinate6);

        try (MockedStatic<VirtualWaypointStartFinder> mocked = Mockito.mockStatic(VirtualWaypointStartFinder.class))
        {
            mocked.when(() -> VirtualWaypointStartFinder.findStartVwpByBox(Mockito.any(), Mockito.any()))
                    .thenReturn(VirtualWaypointStartFinder.IS_NOT_NEAR_AREA);
            mocked.when(() -> VirtualWaypointStartFinder.findEndVwpByBox(Mockito.any(), Mockito.any())).thenReturn(VirtualWaypointStartFinder.IS_NOT_NEAR_AREA);
            mocked.when(() -> VirtualWaypointStartFinder.findStartVwpProximityToFront(Mockito.any(), Mockito.any()))
                    .thenReturn(VirtualWaypointStartFinder.IS_NOT_NEAR_AREA);
            mocked.when(() -> VirtualWaypointStartFinder.findEndVwpProximityToFront(Mockito.any(), Mockito.any()))
                    .thenReturn(VirtualWaypointStartFinder.IS_NOT_NEAR_AREA);

            VirtualWaypointConsolidator virtualWaypointConsolidator = new VirtualWaypointConsolidator(flight, plotCoordinates);
            List<VirtualWayPointCoordinate> afterConsolidation = virtualWaypointConsolidator.consolidatedVirtualWaypoints();

            assert (afterConsolidation.size() == 1);
        }
    }
}
