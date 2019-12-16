package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.waypoint.VirtualWayPointCoordinate;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.CoalitionFactory;
import pwcg.mission.mcu.group.VirtualWayPoint;

public class VirtualWaypointGenerator
{
    private Flight flight;
    
    public VirtualWaypointGenerator(Flight flight)
    {
        this.flight = flight;
    }
    
    public List<VirtualWayPoint> createVirtualWaypoints() throws PWCGException 
    {
        VirtualWaypointPlotter virtualWaypointPlotter = new VirtualWaypointPlotter(flight);
        List<VirtualWayPointCoordinate> plotCoordinates = virtualWaypointPlotter.plotCoordinates();

        int startVWP = 0;
        int endVWP = plotCoordinates.size();
        
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int boxExpansionForFirstVWPToKeep = RandomNumberGenerator.getRandom(productSpecific.getMaxDistanceForVirtualFlightFromPlayerBox());
        CoordinateBox missionBorders = flight.getMission().getMissionBorders().expandBox(boxExpansionForFirstVWPToKeep);
        for (int i = 0; i < plotCoordinates.size(); ++i)
        {
            VirtualWayPointCoordinate vwpCoordinate = plotCoordinates.get(i);
            if (missionBorders.isInBox(vwpCoordinate.getCoordinate()))
            {
                // Start at the first VWP in the box
                if (startVWP == 0)
                {
                    startVWP = i;
                    break;
                }
            }
        }

        List<VirtualWayPoint> virtualWaypoints = buildVirtualWaypointsFromCoordinates(plotCoordinates, startVWP, endVWP);
        
        return virtualWaypoints;
    }

    private List<VirtualWayPoint> buildVirtualWaypointsFromCoordinates(
            List<VirtualWayPointCoordinate> plotCoordinates,
            int startVWP, 
            int endVWP) throws PWCGException
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
        Coalition enemyCoalition = CoalitionFactory.getEnemyCoalition(flight.getCountry());
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
