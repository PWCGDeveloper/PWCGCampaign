package pwcg.mission.mcu.group.virtual;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightPlanes;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.validate.IndexLinkValidator;
import pwcg.mission.flight.waypoint.IWaypointPackage;
import pwcg.mission.flight.waypoint.WaypointPackage;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;

@RunWith(MockitoJUnitRunner.class)
public class VirtualWaypointActivateTest
{
    @Mock private IFlight flight;
    @Mock private IFlightPlanes flightPlanes;
    private PlaneMcu plane1;
    private PlaneMcu plane2;
    @Mock private IWaypointPackage waypointPackage = new WaypointPackage(flight);
    @Mock VirtualWayPointCoordinate vwpCoordinate;
    @Mock VirtualWaypointPlanes vwpPlanes;
    
    @Before
    public void setup()
    {        
        plane1 = new PlaneMcu();
        plane1.setName("Plane 1");
        plane2 = new PlaneMcu();
        plane2.setName("Plane 2");

        List<PlaneMcu> planes = new ArrayList<>();
        planes.add(plane1);
        planes.add(plane2);

        Coordinate vwpPosition = new Coordinate(100.0, 10000.0, 100.0);
        Mockito.when(vwpCoordinate.getPosition()).thenReturn(vwpPosition);

        Orientation vwpOrientation = new Orientation(270.0);
        Mockito.when(vwpCoordinate.getOrientation()).thenReturn(vwpOrientation);        

        Mockito.when(vwpPlanes.getLeadActivatePlane()).thenReturn(plane1);        
        Mockito.when(vwpPlanes.getAllPlanes()).thenReturn(planes);        
    }
    
    @Test
    public void validateVwpBuildProcess() throws PWCGException
    {
        VirtualWaypointActivate activate = new VirtualWaypointActivate(flight, vwpCoordinate, vwpPlanes);
        activate.build();
        
        assert(IndexLinkValidator.isIndexInTargetList(activate.getFormationTimer().getIndex(), activate.getActivateTimer().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(activate.getWaypointTimer().getIndex(), activate.getFormationTimer().getTargets()));        

        assert(IndexLinkValidator.isIndexInTargetList(activate.getActivate().getIndex(), activate.getActivateTimer().getTargets()));
        assert(IndexLinkValidator.isIndexInTargetList(activate.getFormation().getIndex(), activate.getFormationTimer().getTargets()));
        assert(activate.getWaypointTimer().getTargets().size() == 2);
        assert(IndexLinkValidator.isIndexInTargetList(activate.getFormation().getIndex(), activate.getFormationTimer().getTargets()));
        assert(activate.getFormation().getObjects().size() == 1);
    }

}
