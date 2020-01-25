package pwcg.mission;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.mcu.McuIcon;
import pwcg.mission.mcu.McuWaypoint;

public class MissionWaypointIconBuilder
{
    private ArrayList<McuIcon> waypointIcons = new ArrayList<McuIcon>();

    public void createWaypointIcons(List<IFlight> playerFlights) throws PWCGException
    {
        for (IFlight playerFlight : playerFlights)
        {
            createWaypointIconsForFlight(playerFlight);
        }
    }

    private void createWaypointIconsForFlight(IFlight playerFlight) throws PWCGException
    {
        waypointIcons.clear();

        List<McuWaypoint> waypoints = playerFlight.getWaypointPackage().getAllWaypoints();

        McuIcon prevIcon = null;

        MissionPoint takeoff = playerFlight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_TAKEOFF);
        if (takeoff != null)
        {
            McuIcon icon = new McuIcon(WaypointAction.WP_ACTION_TAKEOFF, takeoff, playerFlight.getFlightInformation().getCountry().getSide());
            prevIcon = icon;
            waypointIcons.add(icon);
        }

        for (int i = 0; i < waypoints.size(); ++i)
        {
            McuWaypoint waypoint = waypoints.get(i);
            McuIcon icon = new McuIcon(waypoint, playerFlight.getFlightInformation().getCountry().getSide());
            if (prevIcon != null)
            {
                prevIcon.setTarget(icon.getIndex());
            }
            prevIcon = icon;
            waypointIcons.add(icon);
        }

        MissionPoint landing = playerFlight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_LANDING);
        if (landing != null)
        {
            McuIcon icon = new McuIcon(WaypointAction.WP_ACTION_LANDING, landing, playerFlight.getFlightInformation().getCountry().getSide());
            if (prevIcon != null)
                prevIcon.setTarget(icon.getIndex());
            waypointIcons.add(icon);
        }
    }

    public void write(BufferedWriter writer) throws PWCGIOException
    {
        for (McuIcon icon : waypointIcons)
        {
            icon.write(writer);
        }
    }

}
