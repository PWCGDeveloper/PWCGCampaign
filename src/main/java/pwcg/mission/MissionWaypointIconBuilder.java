package pwcg.mission;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGIOException;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.McuIcon;
import pwcg.mission.mcu.McuWaypoint;

public class MissionWaypointIconBuilder
{
    private ArrayList<McuIcon> waypointIcons = new ArrayList<McuIcon>();

    public void createWaypointIcons(Flight myFlight) 
    {        
    	waypointIcons.clear();
    	
        List<McuWaypoint> waypoints = myFlight.getAllWaypoints();

        McuIcon prevIcon = null;
        for (int i = 0; i < waypoints.size(); ++i)
        {
            McuWaypoint waypoint = waypoints.get(i);
            McuIcon icon = new McuIcon(waypoint);
            if (prevIcon != null)
            {
                prevIcon.setTarget(icon.getIndex());
            }
            prevIcon = icon;
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
