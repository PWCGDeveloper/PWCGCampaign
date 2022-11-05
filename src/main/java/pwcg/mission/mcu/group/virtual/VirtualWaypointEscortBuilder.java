package pwcg.mission.mcu.group.virtual;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;

public class VirtualWaypointEscortBuilder
{
    public static VirtualWaypointEscort buildVirtualEscort(
            FlightInformation vwpEscortFlightInformation, 
            VirtualWayPointCoordinate vwpCoordinate, 
            VirtualWaypointPlanes vwpPlanes, 
            VirtualWaypointTriggered vwpActivate) throws PWCGException
    {
        try
        {
            VirtualWaypointEscort vwpEscort = new VirtualWaypointEscort(vwpEscortFlightInformation.getCampaign(), vwpCoordinate, vwpEscortFlightInformation.getSquadron(), vwpPlanes, vwpActivate);
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
