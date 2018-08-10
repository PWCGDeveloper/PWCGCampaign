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
        VirtualWaypointPlotter virtualWaypointPlotter = new VirtualWaypointPlotter();
        List<VirtualWayPointCoordinate> plotCoordinates = virtualWaypointPlotter.plotCoordinatesByMinute(flight);

        
        // Reduce the number of VWPs by starting and ending the plot near the player
        // Enemy flights spawn closer to the players start
        // Friendly flights spawn after the players start        
        int startVWP = 0;
        int endVWP = plotCoordinates.size();
        
        // Determine which VWPs to keep
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int boxExpansionForFirstVWPToKeep = RandomNumberGenerator.getRandom(productSpecific.getMaxDistanceForVirtualFlightFromPlayerBox());
        CoordinateBox missionBorders = flight.getMission().getMissionFlightBuilder().getMissionBorders(boxExpansionForFirstVWPToKeep);
        for (int i = 0; i < plotCoordinates.size(); ++i)
        {
            VirtualWayPointCoordinate vwp = plotCoordinates.get(i);
            if (missionBorders.isInBox(vwp.getCoordinate()))
            {
                // Start at the first VWP in the box
                if (startVWP == 0)
                {
                    startVWP = i;
                    break;
                }
            }
        }

        List<VirtualWayPoint> virtualWaypoints = buildVirtualWaypointsFromActual(plotCoordinates, startVWP, endVWP);
        
        return virtualWaypoints;
    }

    private List<VirtualWayPoint> buildVirtualWaypointsFromActual(
            List<VirtualWayPointCoordinate> plotCoordinates,
            int startVWP, 
            int endVWP) throws PWCGException
    {
        VirtualWayPoint prevVirtualWaypoint = null;        
        int virtWPIndex = 0;
        int skippedVWPs = 0;

        List<VirtualWayPoint> virtualWaypoints = new ArrayList<VirtualWayPoint>();
        while (true)
        {
            // Last Virtual WP has already been processed.  Stop right now.
            if (virtWPIndex == plotCoordinates.size())
            {
                break;
            }
            
            
            VirtualWayPointCoordinate vwpCoordinate = plotCoordinates.get(virtWPIndex);

            // The next virtual WP
            VirtualWayPoint virtualWaypoint = null;

            virtualWaypoint = new VirtualWayPoint();
 
            // Initialize the WP
            Coalition enemyCoalition = Coalition.getEnemyCoalition(flight.getCountry());
            virtualWaypoint.initialize(flight, vwpCoordinate, enemyCoalition);

            // Link the last VWP to this VWP
            if (prevVirtualWaypoint != null)
            {
                prevVirtualWaypoint.linkToNextVirtualWaypoint(virtualWaypoint);
            }
            prevVirtualWaypoint = virtualWaypoint;
            
            if ((virtWPIndex >= startVWP) && (virtWPIndex <= endVWP))
            {
                int waitTime = VirtualWayPoint.VWP_WAIT_TIME + (skippedVWPs * VirtualWayPoint.VWP_WAIT_TIME);
                virtualWaypoint.getNextVwpTimer().setTimer(waitTime);
                virtualWaypoints.add(virtualWaypoint);
                
                skippedVWPs = 0;
            }
            else
            {
                ++skippedVWPs;
            }
            
            ++virtWPIndex;
        }
        return virtualWaypoints;
    }

}
