package pwcg.mission.flight.waypoint.virtual;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.IFlight;

public class VirtualWaypointStartFinder
{
    public static int determineStartVWP(IFlight flight, List<VirtualWayPointCoordinate> plotCoordinates) throws PWCGException 
    {
        int startInBoxIndex = findFirstVwpInBox(flight, plotCoordinates);

        int low = startInBoxIndex / 2;
        int high = startInBoxIndex;
        int variance = high - low;
        int selectedWaypoint = low + RandomNumberGenerator.getRandom(variance);
        return selectedWaypoint;
    }

    public static int findFirstVwpInBox(IFlight flight, List<VirtualWayPointCoordinate> plotCoordinates) throws PWCGException 
    {
        CoordinateBox missionBorders = flight.getMission().getMissionBorders();

        for (int startVWP = 0; startVWP < plotCoordinates.size(); ++startVWP)
        {
            VirtualWayPointCoordinate vwpCoordinate = plotCoordinates.get(startVWP);
            if (missionBorders.isInBox(vwpCoordinate.getPosition()))
            {
                return startVWP;
            }
        }
        return 0;
    }
}
