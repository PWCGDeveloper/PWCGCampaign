package pwcg.mission.flight.attack;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.builder.TargetBuilderGenerator;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class GroundAttackPackage implements IFlightPackage
{
    private FlightInformation flightInformation;

    public GroundAttackPackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

	public Flight createPackage () throws PWCGException 
	{
	    IGroundUnitCollection groundUnits = createGroundUnitsForFlight();
        GroundAttackFlight groundAttackFlight = createFlight(groundUnits);
		return groundAttackFlight;
	}

    private GroundAttackFlight createFlight(IGroundUnitCollection groundUnitCollection) throws PWCGException
    {
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());
        GroundAttackFlight groundAttackFlight = new GroundAttackFlight (flightInformation, missionBeginUnit);
		groundAttackFlight.linkGroundUnitsToFlight(groundUnitCollection);
		groundAttackFlight.createUnitMission();
        return groundAttackFlight;
    }

    private IGroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetBuilderGenerator targetBuilder = new TargetBuilderGenerator(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }
}
