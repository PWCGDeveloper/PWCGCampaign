package pwcg.mission.flight.validate;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.recon.PlayerReconFlight;
import pwcg.mission.flight.recon.ReconPhotoMcuSet;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.mcu.McuCounter;
import pwcg.mission.mcu.McuMedia;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerReconFlightValidator 
{
	public void validateReconFlight(PlayerReconFlight flight) throws PWCGException
	{
		assert(flight.getFlightData().getWaypointPackage().getWaypointsForLeadPlane().size() > 0);
		validateWaypointLinkage(flight);
		validateWaypointTypes(flight);
	}

	private void validateWaypointLinkage(PlayerReconFlight reconFlight) 
	{
		int photoReconSetIndex = 0;
		McuWaypoint prevWaypoint = null;
		for (McuWaypoint waypoint : reconFlight.getFlightData().getWaypointPackage().getWaypointsForLeadPlane())
		{
			if (prevWaypoint != null)
			{
				boolean isNextWaypointLinked = isIndexInTargetList(waypoint.getIndex(), prevWaypoint.getTargets());
				if (!prevWaypoint.getWpAction().equals(WaypointAction.WP_ACTION_RECON))
				{
					assert(isNextWaypointLinked);
				}
				else
				{
					assert(!isNextWaypointLinked);
					
					ReconPhotoMcuSet reconPhotoMcuSet = reconFlight.getPhotoReconSets().get(photoReconSetIndex);
					++photoReconSetIndex;
					
					McuTimer cameraStartTimer = reconPhotoMcuSet.getCameraStartTimer();
					McuMedia startCamera = reconPhotoMcuSet.getStartCamera();
					McuCounter counter = reconPhotoMcuSet.getCounter();
					McuMedia stopCamera = reconPhotoMcuSet.getStopCamera();
					McuTimer nextWaypointTimer = reconPhotoMcuSet.getNextWaypointTimer();

					assert(isIndexInTargetList(cameraStartTimer.getIndex(), prevWaypoint.getTargets()));
					assert(isIndexInTargetList(startCamera.getIndex(), cameraStartTimer.getTargets()));
					assert(isIndexInTargetList(stopCamera.getIndex(), counter.getTargets()));
					assert(isIndexInTargetList(nextWaypointTimer.getIndex(), counter.getTargets()));
					assert(isIndexInTargetList(waypoint.getIndex(), nextWaypointTimer.getTargets()));
				}
			}
			
			prevWaypoint = waypoint;
		}
	}

	private boolean isIndexInTargetList(int index, List<String>targets) 
	{
		boolean isIndexInTargetList = false;
		for (String targetIndex : targets)
		{
			if (targetIndex.equals(new String("" + index)))
			{
				isIndexInTargetList = true;	
			}
		}
		return isIndexInTargetList;
	}

	private void validateWaypointTypes(PlayerReconFlight flight) 
	{
		WaypointPriorityValidator.validateWaypointTypes(flight);
		
        boolean reconFound = false;
		for (McuWaypoint waypoint : flight.getFlightData().getWaypointPackage().getWaypointsForLeadPlane())
		{
			if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_RECON))
			{
				reconFound = true;
			}
		}
		
		assert(reconFound);
	}
}
