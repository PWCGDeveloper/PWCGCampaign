package pwcg.mission.flight.ferry;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;

public class FerryPackage
{
	public Flight createPackage (Mission mission, 
	                             Campaign campaign,
	                             Squadron squad) throws PWCGException 
	{
	    IAirfield fromAirfield = PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirfield(campaign.getSquadronMoveEvent().getLastAirfield());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(fromAirfield.getPosition().copy());        
        IAirfield toAirfield = squad.determineCurrentAirfieldCurrentMap(campaign.getDate());
        FlightInformation flightInformation = FlightInformationFactory.buildPlayerFlightInformation(squad, mission, FlightTypes.FERRY, toAirfield.getPosition().copy());
		FerryFlight ferry = new FerryFlight (flightInformation, missionBeginUnit, false);
		ferry.createUnitMission();
		return ferry;
	}
}
