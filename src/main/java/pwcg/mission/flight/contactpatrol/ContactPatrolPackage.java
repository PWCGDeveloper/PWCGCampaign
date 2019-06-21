package pwcg.mission.flight.contactpatrol;

import pwcg.campaign.target.unit.TargetBuilder;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.GroundUnitCollection;

public class ContactPatrolPackage implements IFlightPackage
{
    private FlightInformation flightInformation;

    public ContactPatrolPackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public Flight createPackage () throws PWCGException 
	{
        GroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        ContactPatrolFlight contactPatrol = createFlight(groundUnitCollection);
		return contactPatrol;
	}

    private ContactPatrolFlight createFlight(GroundUnitCollection groundUnitCollection) throws PWCGException
    {
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
	    MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());	        
        ContactPatrolFlight contactPatrol = new ContactPatrolFlight (flightInformation, missionBeginUnit);
        contactPatrol.createUnitMission();
        contactPatrol.linkGroundUnitsToFlight(groundUnitCollection);
        return contactPatrol;
    }

    private GroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }
}
