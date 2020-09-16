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
    
    @Before
    public void setup()
    {
        Coordinate vwpPosition = new Coordinate(100.0, 10000.0, 100.0);
        Mockito.when(vwpCoordinate.getPosition()).thenReturn(vwpPosition);
    }

    @Test
    public void validateVwpBuildProcess() throws PWCGException
    {
        VirtualWaypointCheckZone vwpCheckZone = new VirtualWaypointCheckZone(vwpCoordinate);
        vwpCheckZone.build();
        
        assert(IndexLinkValidator.isIndexInTargetList(vwpCheckZone.getCheckZone().getActivateEntryPoint(), vwpCheckZone.getVwpStartTimer().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(vwpCheckZone.getTriggeredDisableNextVwpTimer().getIndex(), vwpCheckZone.getCheckZone().getCheckZone().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(vwpCheckZone.getTriggeredActivateContainerTimer().getIndex(), vwpCheckZone.getTriggeredDisableNextVwpTimer().getTargets()));

        int deactivateNextVwp = 999;
        int activatenextContainer = 9999;
        
        vwpCheckZone.link(deactivateNextVwp, activatenextContainer);
        
        assert(IndexLinkValidator.isIndexInTargetList(deactivateNextVwp, vwpCheckZone.getTriggeredDisableNextVwpTimer().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(activatenextContainer, vwpCheckZone.getTriggeredActivateContainerTimer().getTargets()));

    }
}
