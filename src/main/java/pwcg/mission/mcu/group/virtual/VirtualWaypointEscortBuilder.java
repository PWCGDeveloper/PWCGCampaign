package pwcg.mission.mcu.group.virtual;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;

public class VirtualWaypointEscortBuilder
{
    public static VirtualWaypointEscort buildVirtualEscort(IFlightInformation vwpEscortFlightInformation, VirtualWayPointCoordinate vwpCoordinate, VirtualWaypointPlanes vwpPlanes, VirtualWaypointActivate vwpActivate) throws PWCGException
    {
        try
        {
            VirtualWaypointEscort vwpEscort = new VirtualWaypointEscort(vwpCoordinate, vwpEscortFlightInformation.getSquadron(), vwpPlanes, vwpActivate);
            vwpEscort.buildEscort(vwpEscortFlightInformation);
            return vwpEscort;
        }
        catch (PWCGException e)
        {
            e.printStackTrace();
        }
        
        return null;
    }

}
