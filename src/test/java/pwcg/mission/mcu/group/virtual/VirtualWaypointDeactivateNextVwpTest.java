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
public class VirtualWaypointDeactivateNextVwpTest
{
    @Mock VirtualWayPointCoordinate vwpCoordinate;
    @Mock VirtualWaypointStartNextVwp vwpNextVwpStart;
    @Mock VirtualWaypointDeactivateThisVwp vwpDeactivateThisVwp;
    
    @Before
    public void setup()
    {
        Coordinate vwpPosition = new Coordinate(100.0, 10000.0, 100.0);
        Mockito.when(vwpCoordinate.getPosition()).thenReturn(vwpPosition);
    }

    @Test
    public void validateVwpBuildProcess() throws PWCGException
    {
        VirtualWaypointDeactivateNextVwp vwpDeactivate = new VirtualWaypointDeactivateNextVwp(vwpCoordinate);
        vwpDeactivate.build();
        
        assert(IndexLinkValidator.isIndexInTargetList(vwpDeactivate.getStartNextVwpTimerDeactivate().getIndex(), vwpDeactivate.getDeactivateNextVwpTimer().getTargets()));

        Mockito.when(vwpNextVwpStart.getEntryPoint()).thenReturn(99);
        Mockito.when(vwpDeactivateThisVwp.getEntryPoint()).thenReturn(98);
        vwpDeactivate.link(vwpNextVwpStart);
        
        assert(IndexLinkValidator.isIndexInTargetList(99, vwpDeactivate.getStartNextVwpTimerDeactivate().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(98, vwpDeactivate.getStartNextVwpTimerDeactivate().getTargets()));
    }
}
