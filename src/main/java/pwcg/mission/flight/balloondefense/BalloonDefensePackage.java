 package pwcg.mission.flight.balloondefense;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.builder.BalloonUnitBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class BalloonDefensePackage implements IFlightPackage
{
    private FlightInformation flightInformation;

    public BalloonDefensePackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

	public Flight createPackage () throws PWCGException 
	{
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
        IGroundUnitCollection balloonUnit = createGroundUnitsForFlight();
        Flight balloonDefenseFlight = createFlight(startCoords, balloonUnit);
		return balloonDefenseFlight;
	}

    private IGroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        BalloonUnitBuilder groundUnitBuilderBalloonDefense = new BalloonUnitBuilder(flightInformation.getCampaign(), flightInformation.getTargetDefinition());
        IGroundUnitCollection balloonUnit = groundUnitBuilderBalloonDefense.createBalloonUnit(flightInformation.getSquadron().getCountry());
        return balloonUnit;
    }

    private Flight createFlight(Coordinate startCoords, IGroundUnitCollection balloonUnit) throws PWCGException
    {
		Flight balloonDefenseFlight = createBalloonDefenseFlight(startCoords, balloonUnit);
		balloonDefenseFlight.linkGroundUnitsToFlight(balloonUnit);
		balloonDefenseFlight.createUnitMission();
        return balloonDefenseFlight;
    }

    private Flight createBalloonDefenseFlight(Coordinate startCoords, IGroundUnitCollection balloonUnit) throws PWCGException
    {
        Flight balloonDefenseFlight = null;
		if(flightInformation.isPlayerFlight())
		{
	        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());
            PlayerBalloonDefenseFlight playerCoverUnit = new PlayerBalloonDefenseFlight(flightInformation, missionBeginUnit, balloonUnit);          
			balloonDefenseFlight = playerCoverUnit;
		}
		else
		{
	        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());
			AiBalloonDefenseFlight nonPlayerCoverUnit = new AiBalloonDefenseFlight(flightInformation, missionBeginUnit, balloonUnit);
			balloonDefenseFlight = nonPlayerCoverUnit;
		}

        return balloonDefenseFlight;
    }

}
