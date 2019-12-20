package pwcg.mission.flight.virtual;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.VirtualWayPointCoordinate;

public class VirtualWaypointStartFinder
{
    public static int determineStartVWP(Flight flight, List<VirtualWayPointCoordinate> plotCoordinates) throws PWCGException 
    {
        int startInBoxIndex = findFirstVwpInBox(flight, plotCoordinates);

        int low = startInBoxIndex / 2;
        int high = startInBoxIndex;
        int variance = high - low;
        int selectedWaypoint = low + RandomNumberGenerator.getRandom(variance);
        return selectedWaypoint;
    }

    public static int findFirstVwpInBox(Flight flight, List<VirtualWayPointCoordinate> plotCoordinates) throws PWCGException 
    {
        CoordinateBox missionBorders = flight.getMission().getMissionBorders();

        for (int startVWP = 0; startVWP < plotCoordinates.size(); ++startVWP)
        {
            VirtualWayPointCoordinate vwpCoordinate = plotCoordinates.get(startVWP);
            if (missionBorders.isInBox(vwpCoordinate.getCoordinate()))
            {
                return startVWP;
            }
        }
        return 0;
    }
}
