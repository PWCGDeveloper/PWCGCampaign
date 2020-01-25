 package pwcg.mission.flight.balloondefense;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.builder.BalloonUnitBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class BalloonDefensePackage implements IFlightPackage
{
    private IFlightInformation flightInformation;

    public BalloonDefensePackage(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

	public IFlight createPackage () throws PWCGException 
	{
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
        IGroundUnitCollection balloonUnit = createGroundUnitsForFlight();
        IFlight balloonDefenseFlight = createFlight(startCoords, balloonUnit);
		return balloonDefenseFlight;
	}

    private IGroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        BalloonUnitBuilder groundUnitBuilderBalloonDefense = new BalloonUnitBuilder(flightInformation.getMission(), flightInformation.getTargetDefinition());
        IGroundUnitCollection balloonUnit = groundUnitBuilderBalloonDefense.createBalloonUnit(flightInformation.getSquadron().getCountry());
        return balloonUnit;
    }

    private IFlight createFlight(Coordinate startCoords, IGroundUnitCollection balloonUnit) throws PWCGException
    {
		IFlight balloonDefenseFlight = createBalloonDefenseFlight(startCoords, balloonUnit);
		balloonDefenseFlight.getLinkedGroundUnits().addLinkedGroundUnit(balloonUnit);
		balloonDefenseFlight.createFlight();
        return balloonDefenseFlight;
    }

    private IFlight createBalloonDefenseFlight(Coordinate startCoords, IGroundUnitCollection balloonUnit) throws PWCGException
    {
        IFlight balloonDefenseFlight = null;
        BalloonDefenseFlight nonPlayerCoverUnit = new BalloonDefenseFlight(flightInformation, balloonUnit);
        balloonDefenseFlight = nonPlayerCoverUnit;

        return balloonDefenseFlight;
    }
}
