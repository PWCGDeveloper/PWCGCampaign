package pwcg.mission.mcu;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public class ReconPhotoMcuSet 
{
	private McuTimer cameraStartTimer = new McuTimer();
	private McuMedia startCamera = new McuMedia(McuMedia.MEDIA_TYPE_START);
	private McuCounter counter = new McuCounter();
	private McuMedia stopCamera = new McuMedia(McuMedia.MEDIA_TYPE_STOP);
	private McuTimer nextWaypointTimer = new McuTimer();
	
	public ReconPhotoMcuSet ()
	{
	}
	
	public void createPhotography(Coordinate coordinate) throws PWCGException
	{
		createCameraStartTimer(coordinate);
		createCameraStart(coordinate);
		createCameraStop(coordinate);
		createNextWaypointTimer(coordinate);
	}
	
	public void createTargetAssociationsForPhotography(McuWaypoint prevWP, McuWaypoint nextWP) throws PWCGException
	{
		prevWP.setTarget(cameraStartTimer.getIndex());
		cameraStartTimer.setTarget(startCamera.getIndex());

		counter.setTarget(stopCamera.getIndex());
		counter.setTarget(nextWaypointTimer.getIndex());
		nextWaypointTimer.setTarget(nextWP.getIndex());
	}

	public void write(BufferedWriter writer) throws PWCGException 
	{
		cameraStartTimer.write(writer);
		startCamera.write(writer);
		counter.write(writer);
		stopCamera.write(writer);
		nextWaypointTimer.write(writer);			
	}
	

	private void createCameraStartTimer(Coordinate coordinate) 
	{
		cameraStartTimer.setName("Photo Check Zone Timer");		
		cameraStartTimer.setDesc("Photo Check Zone Timer");
		cameraStartTimer.setPosition(coordinate.copy());
	}

	private void createCameraStart(Coordinate coordinate) 
	{
		startCamera.setName("Start Camera");		
		startCamera.setDesc("Start Camera");
		startCamera.setPosition(coordinate.copy());
		
		counter.setName("Camera Click Counter");
		counter.setDesc("Camera Click Counter");
		counter.setPosition(coordinate.copy());

		McuEvent cameraEvent = new McuEvent();
		cameraEvent.setType(14);
		cameraEvent.setTarId(counter.getIndex());
		startCamera.addEvent(cameraEvent);
	}
	

	private void createCameraStop(Coordinate coordinate) 
	{
		stopCamera.setName("Stop Camera");		
		stopCamera.setDesc("Stop Camera");
		stopCamera.setPosition(coordinate.copy());
	}
	

	private void createNextWaypointTimer(Coordinate coordinate) 
	{
		nextWaypointTimer.setName("Next Waypoint Timer");		
		nextWaypointTimer.setDesc("Next Waypoint Timer");
		nextWaypointTimer.setPosition(coordinate.copy());
	}

	public McuTimer getCameraStartTimer() 
	{
		return cameraStartTimer;
	}

	public McuMedia getStartCamera() 
	{
		return startCamera;
	}

	public McuCounter getCounter() 
	{
		return counter;
	}

	public McuMedia getStopCamera() 
	{
		return stopCamera;
	}

	public McuTimer getNextWaypointTimer() 
	{
		return nextWaypointTimer;
	}
}
