package pwcg.mission.flight.ferry;

import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.mcu.McuWaypoint;

public class FerryFlight extends Flight
{
    private boolean isTransfer = true;
    
    private IAirfield fromAirfield;
    private IAirfield toAirfield;
    
    public FerryFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit, boolean isTransfer) throws PWCGException
    {
        super (flightInformation, missionBeginUnit);
		this.isTransfer = isTransfer;
        fromAirfield = flightInformation.getCampaign().getSquadronMoveEvent().getLastAirfield();
        toAirfield = flightInformation.getSquadron().determineCurrentAirfieldCurrentMap(flightInformation.getCampaign().getDate());
	}

	@Override
	public int calcNumPlanes() 
	{
		return 1;

	}

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		FerryWaypoints waypointGenerator = new FerryWaypoints(
		                fromAirfield, 
		                toAirfield, 
		                this,
		                mission);

        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
        
        return waypointList;
	}

	public String getMissionObjective() 
	{
	    String objective = "";
	
	    if (isTransfer)
	    {
	        objective = "Join your new squadron " + " at " + toAirfield.getName(); 
	    }
	    else
	    {
            objective = "Move from " + fromAirfield.getName() + " to " + toAirfield.getName(); 
	    }
		
		return objective;
	}

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        this.createSimpleTargetAssociations();
    }
}
