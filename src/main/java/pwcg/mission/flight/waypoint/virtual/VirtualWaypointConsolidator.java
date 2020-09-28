package pwcg.mission.flight.waypoint.virtual;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;

public class VirtualWaypointConsolidator
{
    private IFlight flight;
    private List<VirtualWayPointCoordinate> beforeConsolidation;
    
    public VirtualWaypointConsolidator(IFlight flight, List<VirtualWayPointCoordinate> beforeConsolidation)
    {
        this.flight = flight;
        this.beforeConsolidation = beforeConsolidation;
    }

    
    public List<VirtualWayPointCoordinate> consolidatedVirtualWaypoints() throws PWCGException
    {
        List<VirtualWayPointCoordinate> afterBeginEndConsolidation = consolidatedBeginningAndEndVirtualWaypoints();
        List<VirtualWayPointCoordinate> afterConsolidation = consolidatedVeryCloseWaypoints(afterBeginEndConsolidation);
        return afterConsolidation;
        
    }
    
    private List<VirtualWayPointCoordinate> consolidatedVeryCloseWaypoints(List<VirtualWayPointCoordinate> vwpCoordinates)
    {
        if (vwpCoordinates.size() < 8)
        {
            return vwpCoordinates;
        }
                
        int consolidationIndex = findConsolidationPoint(vwpCoordinates);
        if (consolidationIndex > 1)
        {
            VirtualWayPointCoordinate previousVwpCoordinate = vwpCoordinates.get(consolidationIndex-1);
            VirtualWayPointCoordinate vwpCoordinate = vwpCoordinates.get(consolidationIndex);
            int combinedWaitTime = previousVwpCoordinate.getWaypointWaitTimeSeconds() + vwpCoordinate.getWaypointWaitTimeSeconds();
            vwpCoordinate.setWaypointWaitTimeSeconds(combinedWaitTime);
            vwpCoordinates.remove(consolidationIndex-1);
            vwpCoordinates = consolidatedVeryCloseWaypoints(vwpCoordinates);
        }
        else
        {
            return vwpCoordinates;
        }

        return vwpCoordinates;
    }
    
    private int findConsolidationPoint(List<VirtualWayPointCoordinate> vwpCoordinates)
    {
        for (int i = vwpCoordinates.size() - 1; i > 1; --i)
        {
            VirtualWayPointCoordinate previousVwpCoordinate = vwpCoordinates.get(i-1);
            VirtualWayPointCoordinate vwpCoordinate = vwpCoordinates.get(i);
            
            double distance = MathUtils.calcDist(previousVwpCoordinate.getPosition(), vwpCoordinate.getPosition());
            if (distance < 5000)
            {
                return i;
            }            
        }
        
        return -1;
    }


    private List<VirtualWayPointCoordinate> consolidatedBeginningAndEndVirtualWaypoints() throws PWCGException
    {
        int firstVwpNearBox = VirtualWaypointStartFinder.findStartVwpByBox(flight, beforeConsolidation);
        int lastVwpNearBox = VirtualWaypointStartFinder.findEndVwpByBox(flight, beforeConsolidation);

        int firstVwpNearFront = VirtualWaypointStartFinder.findStartVwpProximityToFront(flight, beforeConsolidation);
        int lastVwpNearFront = VirtualWaypointStartFinder.findEndVwpProximityToFront(flight, beforeConsolidation);
        
        int firstVwpToKeep = findFirstVwpToKeep(firstVwpNearBox, firstVwpNearFront);
        int lastVwpToKeep = findLastVwpToKeep(lastVwpNearBox, lastVwpNearFront);
        
        int timeUntilFirst = rollUpimeAtFront(firstVwpToKeep);
        
        List<VirtualWayPointCoordinate> afterConsolidation = consolidateVirtualWaypoints(firstVwpToKeep, lastVwpToKeep, timeUntilFirst);
        return afterConsolidation;
    }

    private List<VirtualWayPointCoordinate> consolidateVirtualWaypoints(int firstVwpToKeep, int lastVwpToKeep, int timeUntilFirst)
    {
        List<VirtualWayPointCoordinate> afterConsolidation = new ArrayList<>();
        for (int i = firstVwpToKeep; i <= lastVwpToKeep; ++i)
        {
            afterConsolidation.add(beforeConsolidation.get(i));
        }
        afterConsolidation.get(0).setWaypointWaitTimeSeconds(timeUntilFirst);
        return afterConsolidation;
    }

    private int findFirstVwpToKeep(int firstVwpNearBox, int firstVwpNearFront)
    {
        int firstVwpToKeep = 0;
        if (firstVwpNearBox == VirtualWaypointStartFinder.IS_NOT_NEAR_AREA && firstVwpNearFront == VirtualWaypointStartFinder.IS_NOT_NEAR_AREA)
        {
            firstVwpToKeep = 0;
        }
        else if (firstVwpNearBox == VirtualWaypointStartFinder.IS_NOT_NEAR_AREA)
        {
            firstVwpToKeep = firstVwpNearFront;
        }
        else if (firstVwpNearFront == VirtualWaypointStartFinder.IS_NOT_NEAR_AREA)
        {
            firstVwpToKeep = firstVwpNearBox;
        }
        else
        {
            if (firstVwpNearFront >  firstVwpNearBox)
            {
                firstVwpToKeep = firstVwpNearFront;
            }
            else
            {
                firstVwpToKeep = firstVwpNearBox;
            }
        }
        return firstVwpToKeep;
    }
    

    private int findLastVwpToKeep(int lastVwpNearBox, int lastVwpNearFront)
    {
        int lastVwpToKeep = beforeConsolidation.size();
        if (lastVwpNearBox == VirtualWaypointStartFinder.IS_NOT_NEAR_AREA && lastVwpNearFront == VirtualWaypointStartFinder.IS_NOT_NEAR_AREA)
        {
            // If last VWP to keep is not in the box or the front then this flight isn't close to anything.
            // Just establish one VWP to minimize resource usage.  Future: eliminate this flight.
            lastVwpToKeep = 0;
        }
        else if (lastVwpNearBox == VirtualWaypointStartFinder.IS_NOT_NEAR_AREA)
        {
            lastVwpToKeep = lastVwpNearFront;
        }
        else if (lastVwpNearFront == VirtualWaypointStartFinder.IS_NOT_NEAR_AREA)
        {
            lastVwpToKeep = lastVwpNearBox;
        }
        else
        {
            if (lastVwpNearFront <  lastVwpNearBox)
            {
                lastVwpToKeep = lastVwpNearFront;
            }
            else
            {
                lastVwpToKeep = lastVwpNearBox;
            }
        }
        return lastVwpToKeep;
    }
    
    private int rollUpimeAtFront(int firstVwpToKeep)
    {
        int timeUntilFirst = 0;
        for (int i = 0; i <= firstVwpToKeep; ++i)
        {
            VirtualWayPointCoordinate vwpCoordinate = beforeConsolidation.get(i);
            timeUntilFirst += vwpCoordinate.getWaypointWaitTimeSeconds();
        }
        return timeUntilFirst;
    }
}
