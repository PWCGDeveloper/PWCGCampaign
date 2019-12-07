package pwcg.mission.flight.contactpatrol;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.builder.TargetBuilderGenerator;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class ContactPatrolPackage implements IFlightPackage
{
    private FlightInformation flightInformation;

    public ContactPatrolPackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public Flight createPackage () throws PWCGException 
	{
        IGroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        ContactPatrolFlight contactPatrol = createFlight(groundUnitCollection);
		return contactPatrol;
	}

    private ContactPatrolFlight createFlight(IGroundUnitCollection groundUnitCollection) throws PWCGException
    {
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
	    MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());	        
        ContactPatrolFlight contactPatrol = new ContactPatrolFlight (flightInformation, missionBeginUnit);
        contactPatrol.createUnitMission();
        contactPatrol.linkGroundUnitsToFlight(groundUnitCollection);
        return contactPatrol;
    }

    private IGroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetBuilderGenerator targetBuilder = new TargetBuilderGenerator(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }
}
