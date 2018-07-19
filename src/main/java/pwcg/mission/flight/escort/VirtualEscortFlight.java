package pwcg.mission.flight.escort;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightPositionHelperAirStart;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.mcu.McuWaypoint;

public class VirtualEscortFlight extends Flight
{
    private Flight escortedFlight = null;

    public VirtualEscortFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit, Flight escortingFlight)
    {
        super (flightInformation, missionBeginUnit);
        this.escortedFlight = escortingFlight;
    }

	public void createEscortPositionCloseToFirstWP() throws PWCGException 
	{
		// Different logic for escort air start
		Coordinate escortedFlightCoords = escortedFlight.getPlanes().get(0).getPosition().copy();
		Orientation escortedFlightOrient = escortedFlight.getPlanes().get(0).getOrientation().copy();
		
        // Air start for other flights
        Coordinate escortFlightCoords = new Coordinate();
        
        // Since we always face east, subtract from z to get your mates behind you              
        escortFlightCoords.setXPos(escortedFlightCoords.getXPos() + 100);
        escortFlightCoords.setZPos(escortedFlightCoords.getZPos()+ 100);
        escortFlightCoords.setYPos(escortedFlightCoords.getYPos() + 300);

        FlightPositionHelperAirStart flightPositionHelperAirStart = new FlightPositionHelperAirStart(getCampaign(), this);
        flightPositionHelperAirStart.createPlanePositionAirStart(escortFlightCoords.copy(), escortedFlightOrient.copy());
	}

	@Override
	public int calcNumPlanes() throws PWCGException 
	{
		ConfigManagerCampaign configManager = getCampaign().getCampaignConfigManager();
		
		int escortMinimum = configManager.getIntConfigParam(ConfigItemKeys.PatrolMinimumKey);
		int escortAdditional = configManager.getIntConfigParam(ConfigItemKeys.PatrolAdditionalKey) + 1;
		numPlanesInFlight = escortMinimum + RandomNumberGenerator.getRandom(escortAdditional);
		
        return modifyNumPlanes(numPlanesInFlight);

	}

	public String getMissionObjective() 
	{
		String objective = "Escort our flight to the specified location and accompany them until they cross our lines.";
		return objective;
	}

    @Override
    protected List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) 
    {
        List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();
        for (McuWaypoint escortedWaypoint : escortedFlight.getAllWaypoints())
        {
            double altitude = escortedWaypoint.getPosition().getYPos() + 400.0;

            McuWaypoint escortWP = escortedWaypoint.copy();
            escortWP.getPosition().setYPos(altitude);
            escortWP.setPriority(WaypointPriority.PRIORITY_LOW);
            
            waypoints.add(escortWP);
        }
        
        return waypoints;
    }

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        this.createSimpleTargetAssociations();
    }
}	

