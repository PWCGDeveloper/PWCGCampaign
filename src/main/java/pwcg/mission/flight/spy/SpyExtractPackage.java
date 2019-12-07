package pwcg.mission.flight.spy;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.builder.TargetBuilderGenerator;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class SpyExtractPackage implements IFlightPackage
{
    private FlightInformation flightInformation;

    public SpyExtractPackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public Flight createPackage () throws PWCGException 
	{
        IGroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());            
		SpyExtractFlight spyFlight = new SpyExtractFlight (flightInformation, missionBeginUnit);
		spyFlight.linkGroundUnitsToFlight(groundUnitCollection);
		
		spyFlight.createUnitMission();
		
		return spyFlight;
	}

    private IGroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetBuilderGenerator targetBuilder = new TargetBuilderGenerator(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }
}
