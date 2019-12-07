package pwcg.mission.flight.artySpot;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.builder.TargetBuilderGenerator;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class ArtillerySpotPackage implements IFlightPackage
{
    private FlightInformation flightInformation;

    public ArtillerySpotPackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public Flight createPackage () throws PWCGException 
    {
        Flight artySpot = createFlight();
		return artySpot;
	}

    private Flight createFlight() throws PWCGException
    {        
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
        IGroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(flightInformation.getSquadron().determineEnemySide());
		Flight artySpot = null;
		if (flightInformation.isPlayerFlight())
		{
		    throw new PWCGException("Player artillery spot not supported");
		}
		else
		{
            artySpot = createAiFlight(targetCoordinates, startCoords);
		}

        artySpot.linkGroundUnitsToFlight(groundUnitCollection);
        return artySpot;
    }

    private IGroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetBuilderGenerator targetBuilder = new TargetBuilderGenerator(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }

    private Flight createAiFlight(Coordinate targetCoordinates, Coordinate startCoords) throws PWCGException
    {
        Flight artySpot;
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());
        
        ArtillerySpotFlight artySpotAI = new ArtillerySpotFlight (flightInformation, missionBeginUnit);
        artySpotAI.createUnitMission();
        artySpot = artySpotAI;
        return artySpot;
    }
}
