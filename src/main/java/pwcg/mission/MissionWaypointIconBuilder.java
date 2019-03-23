package pwcg.mission;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGIOException;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.McuIcon;
import pwcg.mission.mcu.McuLanding;
import pwcg.mission.mcu.McuTakeoff;
import pwcg.mission.mcu.McuWaypoint;

public class MissionWaypointIconBuilder
{
    private ArrayList<McuIcon> waypointIcons = new ArrayList<McuIcon>();

    // TODO COOP Icons tied to planes not coalitions
    public void createWaypointIcons(Flight playerFlight) 
    {        
    	waypointIcons.clear();
    	
        List<McuWaypoint> waypoints = playerFlight.getAllWaypoints();

        McuIcon prevIcon = null;

        McuTakeoff takeoff = playerFlight.getTakeoff();
        if (takeoff != null) {
            McuIcon icon = new McuIcon(takeoff);
            prevIcon = icon;
            waypointIcons.add(icon);
        }

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

        McuLanding landing = playerFlight.getLanding();
        if (landing != null) {
            McuIcon icon = new McuIcon(landing);
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
