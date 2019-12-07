package pwcg.mission.flight.divebomb;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.builder.TargetBuilderGenerator;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class DiveBombingPackage implements IFlightPackage
{
    private FlightInformation flightInformation;

    public DiveBombingPackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public Flight createPackage () throws PWCGException 
	{
        IGroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
	    MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());	        
        DiveBombingFlight diveBombingFlight = new DiveBombingFlight(flightInformation, missionBeginUnit);
		diveBombingFlight.linkGroundUnitsToFlight(groundUnitCollection);
		
        diveBombingFlight.createUnitMission();

		return diveBombingFlight;
	}

    private IGroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetBuilderGenerator targetBuilder = new TargetBuilderGenerator(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }
}
