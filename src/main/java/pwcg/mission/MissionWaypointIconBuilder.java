package pwcg.mission;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.mcu.McuIcon;
import pwcg.mission.mcu.McuIconFactory;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.playerunit.PlayerUnit;

public class MissionWaypointIconBuilder
{
    private List<McuIcon> waypointIcons = new ArrayList<>();

    public void createWaypointIcons(List<PlayerUnit> list) throws PWCGException
    {
        for (PlayerUnit playerUnit : list)
        {
            createWaypointIconsForUnit(playerUnit);
        }
    }

    private void createWaypointIconsForUnit(PlayerUnit playerUnit) throws PWCGException
    {
        List<McuWaypoint> waypoints = playerUnit.getWaypointPackage().getAllWaypoints();

        McuIcon firstIcon = null;
        McuIcon prevIcon = null;

        for (int i = 0; i < waypoints.size(); ++i)
        {
            McuWaypoint waypoint = waypoints.get(i);
            McuIcon icon = McuIconFactory.buildWaypointIcon(waypoint, playerUnit.getUnitInformation().getCountry().getSide());
            if (firstIcon == null)
            {
                firstIcon = icon;
            }
            if (prevIcon != null)
            {
                prevIcon.setTarget(icon.getIndex());
            }
            prevIcon = icon;
            waypointIcons.add(icon);
        }
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        for (McuIcon icon : waypointIcons)
        {
            icon.write(writer);
        }
    }

    public void removeWaypointIcons() throws PWCGException
    {
        waypointIcons.clear();
    }

}
