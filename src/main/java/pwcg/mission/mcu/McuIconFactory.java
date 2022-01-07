package pwcg.mission.mcu;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public class McuIconFactory
{
    public static McuIcon buildLocationToIcon (String iconName, String iconText, Side side)
    {
        return new McuIcon(iconName, iconText, side);
    }

    public static McuIcon buildWaypointIcon (McuWaypoint waypoint, Side side)
    {
        return new McuIcon(waypoint, side);
    }
    
    public static McuIcon buildAssaultIcon (Coordinate arrowPosition, double angle, Side side) throws PWCGException
    {
        return new McuIcon(arrowPosition, angle, side);
    }
}
