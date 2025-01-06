package pwcg.mission.flight.recon;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.mission.mcu.McuMedia;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerReconWaypoint {

	private McuWaypoint waypoint;
	private McuTimer entryTimer;
	private McuMedia photoMedia;
	private McuTimer exitTimer;

	public McuWaypoint getWaypoint() {
		return waypoint;
	}

	public void setWaypoint(McuWaypoint waypoint) {
		this.waypoint = waypoint;
	}

	public McuTimer getEntryTimer() {
		return entryTimer;
	}

	public void setEntryTimer(McuTimer entryTimer) {
		this.entryTimer = entryTimer;
	}

	public McuMedia getPhotoMedia() {
		return photoMedia;
	}

	public void setPhotoMedia(McuMedia photoMedia) {
		this.photoMedia = photoMedia;
	}

	public McuTimer getExitTimer() {
		return exitTimer;
	}

	public void setExitTimer(McuTimer exitTimer) {
		this.exitTimer = exitTimer;
	}

	public void write(BufferedWriter writer) throws PWCGException {
		entryTimer.write(writer);		
		waypoint.write(writer);		
		photoMedia.write(writer);		
		exitTimer.write(writer);		
	}
}
