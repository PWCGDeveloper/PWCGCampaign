package pwcg.mission;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.McuIcon;
import pwcg.mission.mcu.McuLanding;
import pwcg.mission.mcu.McuTakeoff;
import pwcg.mission.mcu.McuWaypoint;

public class MissionWaypointIconBuilder
{
    private ArrayList<McuIcon> waypointIcons = new ArrayList<McuIcon>();

    public void createWaypointIcons(List<Flight> playerFlights) throws PWCGException 
    {
        for (Flight playerFlight : playerFlights)
        {
            createWaypointIconsForFlight(playerFlight);
        }
    }
    
    private void createWaypointIconsForFlight(Flight playerFlight) throws PWCGException 
    {        
    	waypointIcons.clear();
    	
        List<McuWaypoint> waypoints = playerFlight.getAllFlightWaypoints();

        McuIcon prevIcon = null;

        McuTakeoff takeoff = playerFlight.getTakeoff();
        if (takeoff != null) {
            McuIcon icon = new McuIcon(takeoff, playerFlight.getCountry().getSide());
            prevIcon = icon;
            waypointIcons.add(icon);
        }

        for (int i = 0; i < waypoints.size(); ++i)
        {
            McuWaypoint waypoint = waypoints.get(i);
            McuIcon icon = new McuIcon(waypoint, playerFlight.getCountry().getSide());
            if (prevIcon != null)
            {
                prevIcon.setTarget(icon.getIndex());
            }
            prevIcon = icon;
            waypointIcons.add(icon);
        }

        McuLanding landing = playerFlight.getLanding();
        if (landing != null) {
            McuIcon icon = new McuIcon(landing, playerFlight.getCountry().getSide());
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
