package pwcg.mission.flight.waypoint.virtual;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.group.virtual.IVirtualWaypoint;
import pwcg.mission.mcu.group.virtual.VirtualWaypoint;

public class VirtualWaypointGenerator
{
    private IFlight flight;
    
    public VirtualWaypointGenerator(IFlight flight)
    {
        this.flight = flight;
    }
    
    public List<VirtualWaypoint> createVirtualWaypoints() throws PWCGException 
    {
        VirtualWaypointPlotter virtualWaypointPlotter = new VirtualWaypointPlotter(flight);
        List<VirtualWayPointCoordinate> plotCoordinates = virtualWaypointPlotter.plotCoordinates();
        List<VirtualWaypoint> virtualWaypoints = buildVirtualWaypointsFromCoordinates(plotCoordinates);           
        return virtualWaypoints;
    }

    private List<VirtualWaypoint> buildVirtualWaypointsFromCoordinates(List<VirtualWayPointCoordinate> plotCoordinates) throws PWCGException
    {
        IVirtualWaypoint prevVirtualWaypoint = null;        

        List<VirtualWaypoint> virtualWaypoints = new ArrayList<VirtualWaypoint>();
        for (VirtualWayPointCoordinate plotCoordinate : plotCoordinates)
        {
            VirtualWaypoint virtualWaypoint = createVirtualWaypointFromPlot(plotCoordinate);
            linkVirtualWaypoint(prevVirtualWaypoint, virtualWaypoint);
            virtualWaypoints.add(virtualWaypoint);
            prevVirtualWaypoint = virtualWaypoint;
            addVwpFlightLeadToWaypoints(virtualWaypoint);
        }
        return virtualWaypoints;
    }

    private void addVwpFlightLeadToWaypoints(VirtualWaypoint virtualWaypoint)
    {
        flight.getWaypointPackage().addObjectToAllMissionPoints(virtualWaypoint.getVwpFlightLeader());
    }

    private VirtualWaypoint createVirtualWaypointFromPlot(VirtualWayPointCoordinate plotCoordinate) throws PWCGException
    {
        VirtualWaypoint virtualWaypoint = new VirtualWaypoint(flight, plotCoordinate);
        virtualWaypoint.build();
        return virtualWaypoint;
    }

    private void linkVirtualWaypoint(IVirtualWaypoint prevVirtualWaypoint, IVirtualWaypoint virtualWaypoint)
    {
        if (prevVirtualWaypoint != null)
        {
            prevVirtualWaypoint.linkToNextVirtualWaypoint(virtualWaypoint);
            prevVirtualWaypoint.linkKillToNextKill(virtualWaypoint);
            prevVirtualWaypoint.linkActivateToNextKill(virtualWaypoint);
        }
    }
}
