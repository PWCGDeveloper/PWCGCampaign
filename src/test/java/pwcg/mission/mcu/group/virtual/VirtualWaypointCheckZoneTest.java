package pwcg.mission.mcu.group.virtual;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.validate.IndexLinkValidator;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;

@RunWith(MockitoJUnitRunner.class)
public class VirtualWaypointCheckZoneTest
{
    @Mock VirtualWayPointCoordinate vwpCoordinate;
    @Mock VirtualWaypointTriggered vwpTriggered;
    @Mock IVirtualWaypoint vwpNextVWP;

    @Before
    public void setup()
    {
        Coordinate vwpPosition = new Coordinate(100.0, 10000.0, 100.0);
        Mockito.when(vwpCoordinate.getPosition()).thenReturn(vwpPosition);
    }

    @Test
    public void validateVwpBuildProcess() throws PWCGException
    {
        VirtualWaypointCheckZone vwpCheckZone = new VirtualWaypointCheckZone(vwpCoordinate, 1);
        vwpCheckZone.build();
        
        Mockito.when(vwpTriggered.getEntryPoint()).thenReturn(99);
        vwpCheckZone.linkToTriggered(vwpTriggered);
        
        Mockito.when(vwpNextVWP.getEntryPoint()).thenReturn(1000);
        vwpCheckZone.linkToTimedOut(vwpNextVWP);
        
        assert(IndexLinkValidator.isIndexInTargetList(vwpCheckZone.getTimedCheckZone().getTriggeredExternal().getTargets(), 99));
        assert(IndexLinkValidator.isIndexInTargetList(vwpCheckZone.getTimedCheckZone().getTimedOutExternal().getTargets(), 1000));

    }
}
