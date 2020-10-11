package pwcg.mission.mcu.group.virtual;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.validate.IndexLinkValidator;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;

@RunWith(MockitoJUnitRunner.class)
public class VirtualWaypointCheckZoneTest
{
    @Mock VirtualWayPointCoordinate vwpCoordinate;
    @Mock VirtualWaypointStartNextVwp vwpNextVwpStart;
    @Mock VirtualWaypointDeactivateNextVwp vwpDeactivateNextVwp;
    @Mock VirtualWaypointActivate vwpActivate;
    @Mock VirtualWaypointPlanes vwpPlanes;
    private PlaneMcu plane1;

    @Before
    public void setup()
    {
        Coordinate vwpPosition = new Coordinate(100.0, 10000.0, 100.0);
        Mockito.when(vwpCoordinate.getPosition()).thenReturn(vwpPosition);
    }

    @Test
    public void validateVwpBuildProcess() throws PWCGException
    {
        plane1 = new PlaneMcu();
        plane1.setName("Plane 1");
        Mockito.when(vwpPlanes.getLeadActivatePlane()).thenReturn(plane1);        

        VirtualWaypointCheckZone vwpCheckZone = new VirtualWaypointCheckZone(vwpCoordinate, vwpPlanes, 1);
        vwpCheckZone.build();
        
        assert(IndexLinkValidator.isIndexInTargetList(vwpCheckZone.getCheckZone().getActivateEntryPoint(), vwpCheckZone.getVwpStartTimer().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(vwpCheckZone.getTriggeredDisableNextVwpTimer().getIndex(), vwpCheckZone.getCheckZone().getCheckZone().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(vwpCheckZone.getTriggeredActivateTimer().getIndex(), vwpCheckZone.getTriggeredDisableNextVwpTimer().getTargets()));

        Mockito.when(vwpNextVwpStart.getEntryPoint()).thenReturn(97);
        Mockito.when(vwpDeactivateNextVwp.getEntryPoint()).thenReturn(98);
        Mockito.when(vwpActivate.getEntryPoint()).thenReturn(99);

        vwpCheckZone.link(vwpNextVwpStart, vwpDeactivateNextVwp, vwpActivate);
        
        assert(IndexLinkValidator.isIndexInTargetList(97, vwpCheckZone.getVwpStartTimer().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(98, vwpCheckZone.getTriggeredDisableNextVwpTimer().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(99, vwpCheckZone.getTriggeredActivateTimer().getTargets()));

    }
}
