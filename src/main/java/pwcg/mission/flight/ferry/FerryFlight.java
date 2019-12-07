package pwcg.mission.flight.ferry;

import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.mcu.McuWaypoint;

public class FerryFlight extends Flight
{    
    private IAirfield fromAirfield;
    private IAirfield toAirfield;
    
    public FerryFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit) throws PWCGException
    {
        super (flightInformation, missionBeginUnit);
        fromAirfield = PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfield(flightInformation.getCampaign().getSquadronMoveEvent().getLastAirfield());
        toAirfield = flightInformation.getSquadron().determineCurrentAirfieldCurrentMap(flightInformation.getCampaign().getDate());
	}

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		FerryWaypoints waypointGenerator = new FerryWaypoints(this, fromAirfield, toAirfield);

        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
        
        return waypointList;
	}

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        this.createSimpleTargetAssociations();
    }
}
