package pwcg.mission.flight.virtual;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.VirtualWayPointCoordinate;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.CoalitionFactory;
import pwcg.mission.mcu.group.VirtualWayPoint;

public class VirtualWaypointGenerator
{
    private IFlight flight;
    
    public VirtualWaypointGenerator(IFlight flight)
    {
        this.flight = flight;
    }
    
    public List<VirtualWayPoint> createVirtualWaypoints() throws PWCGException 
    {
        VirtualWaypointPlotter virtualWaypointPlotter = new VirtualWaypointPlotter(flight);
        List<VirtualWayPointCoordinate> plotCoordinates = virtualWaypointPlotter.plotCoordinates();
        List<VirtualWayPoint> virtualWaypoints = buildVirtualWaypointsFromCoordinates(plotCoordinates);        
        return virtualWaypoints;
    }

    private List<VirtualWayPoint> buildVirtualWaypointsFromCoordinates(List<VirtualWayPointCoordinate> plotCoordinates) throws PWCGException
    {
        VirtualWayPoint prevVirtualWaypoint = null;        

        List<VirtualWayPoint> virtualWaypoints = new ArrayList<VirtualWayPoint>();
        for (VirtualWayPointCoordinate plotCoordinate : plotCoordinates)
        {
            VirtualWayPoint virtualWaypoint = createVirtualWaypointFromPlot(plotCoordinate);
            prevVirtualWaypoint = linkVirtualWaypoint(prevVirtualWaypoint, virtualWaypoint);
            virtualWaypoints.add(virtualWaypoint);
        }
        return virtualWaypoints;
    }

    private VirtualWayPoint createVirtualWaypointFromPlot(VirtualWayPointCoordinate plotCoordinate) throws PWCGException
    {
        Coalition enemyCoalition = CoalitionFactory.getEnemyCoalition(flight.getFlightData().getFlightInformation().getCountry());
        VirtualWayPoint virtualWaypoint = new VirtualWayPoint();
        virtualWaypoint.initialize(flight, plotCoordinate, enemyCoalition);
        return virtualWaypoint;
    }

    private VirtualWayPoint linkVirtualWaypoint(VirtualWayPoint prevVirtualWaypoint, VirtualWayPoint virtualWaypoint)
    {
        if (prevVirtualWaypoint != null)
        {
            prevVirtualWaypoint.linkToNextVirtualWaypoint(virtualWaypoint);
        }
        prevVirtualWaypoint = virtualWaypoint;
        return prevVirtualWaypoint;
    }

}
