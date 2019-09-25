package pwcg.mission.flight.spy;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class SpyExtractFlight extends Flight
{
	protected McuTimer spyDropTimer = null;

    public SpyExtractFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
    {
        super (flightInformation, missionBeginUnit);
    }
    

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		SpyExtractWaypoints waypointGenerator = new SpyExtractWaypoints(this);

		List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
		
		McuWaypoint targetWP = null;
		for (McuWaypoint wp : waypointList)
		{
			if (wp.getName().equals(WaypointType.SPY_EXTRACT_WAYPOINT.getName()))
			{
				targetWP = wp;
				break;
			}
		}

		createSpyDrop(targetWP);
		
		return waypointList;
	}

	public void createSpyDrop(McuWaypoint targetWP) throws PWCGException 
	{
		Coordinate landCoords = getTargetCoords().copy();
		landCoords.setYPos(0.0);
		
		spyDropTimer = new McuTimer();
		spyDropTimer.setName(getName() + ": Activation Timer");		
		spyDropTimer.setDesc("Activation Timer for " + getName());
		spyDropTimer.setPosition(landCoords.copy());	
		spyDropTimer.setTimer(120);
	}

	@Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
	{
        linkWPToPlane(getLeadPlane(), waypointPackage.getWaypointsForLeadPlane());

        McuWaypoint prevWP = null;
        for (McuWaypoint nextWP : waypointPackage.getWaypointsForLeadPlane())
        {
            if (prevWP != null)
            {
                if (prevWP.getName().equals(WaypointType.RECON_WAYPOINT.getName()))
                {
                    prevWP.setTarget(spyDropTimer.getIndex());
                    spyDropTimer.setTarget(nextWP.getIndex());
                }
                else
                {
                    prevWP.setTarget(nextWP.getIndex());
                }
            }
            
			prevWP = nextWP;
		}
	}

	@Override
	public void write(BufferedWriter writer) throws PWCGException 
	{
		super.write(writer);
		spyDropTimer.write(writer);
	}

	public String getMissionObjective() throws PWCGException 
	{
        String objective = "Extract our spy at the specified location" + formMissionObjectiveLocation(getTargetCoords().copy()) + ".  Don't get caught!";       
		
		return objective;
	}

}
