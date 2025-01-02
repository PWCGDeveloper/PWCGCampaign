package pwcg.mission.flight.recon;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.mcu.McuMedia;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class ReconPlayerWaypointBuilder {

	public static ReconPlayerWaypoint buildReconPlayerWaypoint(Campaign campaign, McuWaypoint waypoint) throws PWCGException
	{		
		McuTimer entryTimer = buildEntryTimer(waypoint);
		McuTimer exitTimer  = buildExitTimer(waypoint);
		McuMedia photoMedia  = buildPhotoMedia(waypoint);
		
		ReconPlayerWaypoint reconPlayerWaypoint = new ReconPlayerWaypoint();
		reconPlayerWaypoint.setWaypoint(waypoint);
		reconPlayerWaypoint.setEntryTimer(entryTimer);
		reconPlayerWaypoint.setExitTimer(exitTimer);
		reconPlayerWaypoint.setPhotoMedia(photoMedia);
		
		// The media is the widget actually on top of the recon point where the photo must be taken.
		// Move the WP 1 km away to allow a clean path to the photo location.
		resetReconWaypontPositions(campaign, waypoint);
		
		return reconPlayerWaypoint;		
	}

	private static void resetReconWaypontPositions(Campaign campaign, McuWaypoint waypoint) throws PWCGException {
		double wpAngle = waypoint.getOrientation().getyOri();
		double angleAway = MathUtils.adjustAngle(wpAngle, 180);
		Coordinate oneKmAwayFromPhoto = MathUtils.calcNextCoord(campaign.getCampaignMap(), waypoint.getPosition(), angleAway, 1000);
		oneKmAwayFromPhoto.setYPos(waypoint.getPosition().getYPos());
		waypoint.setPosition(oneKmAwayFromPhoto);
	}

	private static McuMedia buildPhotoMedia(McuWaypoint waypoint) {
		McuMedia photoMedia = new McuMedia();
		photoMedia.setName("Photo Recon Media");
		photoMedia.setDesc("Photo Recon Media");
		photoMedia.setConfig("swf\\extensions\\photorecon");
		photoMedia.setMediaType(2);
		photoMedia.setTotalTime(30);
		photoMedia.setPosition(waypoint.getPosition());
		photoMedia.setOrientation(waypoint.getOrientation());
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
