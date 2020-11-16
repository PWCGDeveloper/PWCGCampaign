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
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPlanes;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.validate.IndexLinkValidator;
import pwcg.mission.flight.waypoint.IWaypointPackage;
import pwcg.mission.flight.waypoint.WaypointPackage;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.McuFormation;

@RunWith(MockitoJUnitRunner.class)
public class VirtualWaypointTriggeredTest
{
    @Mock private IFlight flight;
    @Mock private IFlightInformation flightInformation;
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

        Mockito.when(vwpCoordinate.getWaypointIdentifier()).thenReturn(99999);        

        Mockito.when(vwpPlanes.getLeadActivatePlane()).thenReturn(plane1);        
        Mockito.when(vwpPlanes.getAllPlanes()).thenReturn(planes);        

        Mockito.when(flight.getFlightInformation()).thenReturn(flightInformation);        
        Mockito.when(flightInformation.getFormationType()).thenReturn(McuFormation.FORMATION_V);        
    }
    
    @Test
    public void validateVwpBuildProcessNoEscort() throws PWCGException
    {
        VirtualWaypointTriggered activate = new VirtualWaypointTriggered(flight, vwpCoordinate, vwpPlanes, 1);
        activate.build();
        
        assert(IndexLinkValidator.isIndexInTargetList(activate.getActivateTimer().getTargets(), activate.getFormationTimer().getIndex()));
        assert(IndexLinkValidator.isIndexInTargetList(activate.getActivateTimer().getTargets(), activate.getActivate().getIndex()));

        assert(IndexLinkValidator.isIndexInTargetList(activate.getFormationTimer().getTargets(), activate.getWaypointTimer().getIndex()));   
        assert(IndexLinkValidator.isIndexInTargetList(activate.getFormationTimer().getTargets(), activate.getFormation().getIndex()));
        assert(activate.getFormation().getObjects().size() == 1);

        assert(IndexLinkValidator.isIndexInTargetList(activate.getWaypointTimer().getTargets(), activate.getEscortTimer().getIndex()));   
        assert(IndexLinkValidator.isIndexInTargetList(activate.getWaypointTimer().getTargets(), 99999));   

        assert(activate.getWaypointTimer().getTargets().size() == 2);
        assert(activate.getEscortTimer().getTargets().size() == 0);
    }

}
