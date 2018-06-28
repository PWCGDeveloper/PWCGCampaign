package pwcg.mission.flight.recon;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.mcu.McuWaypoint;

public abstract class ReconWaypoints extends WaypointGeneratorBase
{
	public ReconWaypoints(Coordinate startCoords, 
					  	  Coordinate targetCoords, 
					  	  Flight flight,
					  	  Mission mission) throws PWCGException 
	{
		super(startCoords, targetCoords, flight, mission);
	}

    @Override
    public List<McuWaypoint> createWaypoints() throws PWCGException 
    {
        super.createWaypoints();
        setWaypointsNonFighterPriority();

        return waypoints;
    }

	protected void createTargetWaypoints(Coordinate startPosition) throws PWCGException  
	{
        createReconWaypoints();
	}

	protected abstract void createReconWaypoints() throws PWCGException ;

	protected McuWaypoint createWP(Coordinate coord) throws PWCGException 
	{
		coord.setYPos(getFlightAlt());

		McuWaypoint wp = WaypointFactory.createReconWaypointType();
		wp.setTriggerArea(McuWaypoint.TARGET_AREA);
		wp.setSpeed(waypointSpeed);
		wp.setPosition(coord);
		wp.getPosition().setYPos(getFlightAlt());

		return wp;
	}
}
