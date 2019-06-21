package pwcg.mission.flight.attack;

import pwcg.campaign.target.unit.TargetBuilder;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.GroundUnitCollection;

public class GroundAttackPackage implements IFlightPackage
{
    private FlightInformation flightInformation;

    public GroundAttackPackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

	public Flight createPackage () throws PWCGException 
	{
        GroundUnitCollection groundUnits = createGroundUnitsForFlight();
        GroundAttackFlight groundAttackFlight = createFlight(groundUnits);
		return groundAttackFlight;
	}

    private GroundAttackFlight createFlight(GroundUnitCollection groundUnitCollection) throws PWCGException
    {
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());
        GroundAttackFlight groundAttackFlight = new GroundAttackFlight (flightInformation, missionBeginUnit);
		groundAttackFlight.linkGroundUnitsToFlight(groundUnitCollection);
		groundAttackFlight.createUnitMission();
        return groundAttackFlight;
    }

    private GroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }
}
