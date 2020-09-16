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
public class VirtualWaypointStartNextVwpTest
{
    @Mock VirtualWayPointCoordinate vwpCoordinate;
    
    @Before
    public void setup()
    {
        Coordinate vwpPosition = new Coordinate(100.0, 10000.0, 100.0);
        Mockito.when(vwpCoordinate.getPosition()).thenReturn(vwpPosition);
    }

    @Test
    public void validateVwpBuildProcess() throws PWCGException
    {
        VirtualWaypointStartNextVwp vwpStartNext = new VirtualWaypointStartNextVwp(vwpCoordinate);
        vwpStartNext.build();
        
        assert(IndexLinkValidator.isIndexInTargetList(vwpStartNext.getStartNextWaypointTriggeredTimer().getIndex(), vwpStartNext.getStartNextWaypointTimer().getTargets()));

        int nextVwpIndex = 999;
        vwpStartNext.linkToNextVwp(nextVwpIndex);
        assert(IndexLinkValidator.isIndexInTargetList(nextVwpIndex, vwpStartNext.getStartNextWaypointTriggeredTimer().getTargets()));
    }

}
