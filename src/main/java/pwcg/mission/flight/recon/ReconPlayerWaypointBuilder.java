package pwcg.mission.flight.recon;

import pwcg.mission.mcu.McuMedia;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class ReconPlayerWaypointBuilder {

	public static ReconPlayerWaypoint buildReconPlayerWaypoint(McuWaypoint waypoint)
	{		
		McuTimer entryTimer = buildEntryTimer(waypoint);
		McuTimer exitTimer  = buildExitTimer(waypoint);
		McuMedia photoMedia  = buildPhotoMedia(waypoint);
		
		ReconPlayerWaypoint reconPlayerWaypoint = new ReconPlayerWaypoint();
		reconPlayerWaypoint.setWaypoint(waypoint);
		reconPlayerWaypoint.setEntryTimer(entryTimer);
		reconPlayerWaypoint.setExitTimer(exitTimer);
		reconPlayerWaypoint.setPhotoMedia(photoMedia);
		
		return reconPlayerWaypoint;		
	}

	private static McuMedia buildPhotoMedia(McuWaypoint waypoint) {
		McuMedia photoMedia = new McuMedia();
		return photoMedia;
	}

	private static McuTimer buildEntryTimer(McuWaypoint waypoint) 
	{
		McuTimer entryTimer = new McuTimer();
		entryTimer.setName("Player Recon Entry Timer");      
		entryTimer.setDesc("Player Recon Entry Timer");       
		entryTimer.setPosition(waypoint.getPosition());
		return entryTimer;
	}

	private static McuTimer buildExitTimer(McuWaypoint waypoint) 
	{
		McuTimer exitTimer = new McuTimer();
		exitTimer.setName("Player Recon Exit Timer");      
		exitTimer.setDesc("Player Recon Exit Timer");       
		exitTimer.setPosition(waypoint.getPosition());
		return exitTimer;
	}
}
