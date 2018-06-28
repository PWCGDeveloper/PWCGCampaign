package pwcg.mission.flight.ferry;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.mcu.McuWaypoint;

public class FerryFlight extends Flight
{
    private boolean isTransfer = true;
    
    private IAirfield fromAirfield;
    private IAirfield toAirfield;
    
	public FerryFlight(boolean isTransfer) 
	{
		super ();
		
		this.isTransfer = isTransfer;
	}

	public void initialize(
				Mission mission, 
				Campaign campaign, 
				Squadron squad, 
	            MissionBeginUnit missionBeginUnit) throws PWCGException 
	{
        fromAirfield = campaign.getSquadronMoveEvent().getLastAirfield();
        toAirfield = squad.determineCurrentAirfieldCurrentMap(campaign.getDate());

		super.initialize (mission, campaign, FlightTypes.FERRY, toAirfield.getPosition().copy(), squad, missionBeginUnit, true);
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
