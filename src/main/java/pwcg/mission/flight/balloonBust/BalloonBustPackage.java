package pwcg.mission.flight.balloonBust;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.builder.BalloonUnitBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class BalloonBustPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;

    public BalloonBustPackage(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public IFlight createPackage () throws PWCGException 
    {
        Side enemySide = flightInformation.getSquadron().determineEnemySide();

        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
        Squadron enemyScoutSquadron = PWCGContext.getInstance().getSquadronManager().getSquadronByProximityAndRoleAndSide(
                        flightInformation.getCampaign(), 
                        flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate()), 
                        Role.ROLE_FIGHTER, 
                        flightInformation.getSquadron().determineSquadronCountry(flightInformation.getCampaign().getDate()).getSide().getOppositeSide());
        ICountry balloonCountry = determineBalloonCountry(enemySide, enemyScoutSquadron);
        IGroundUnitCollection balloonUnit = createBalloonUnit(flightInformation.getTargetPosition(), balloonCountry);
		BalloonBustFlight balloonBust = createFlight(startCoords, balloonUnit);

		return balloonBust;
	}

    private BalloonBustFlight createFlight(Coordinate startCoords, IGroundUnitCollection balloonUnit) throws PWCGException
    {
        BalloonBustFlight balloonBust = new BalloonBustFlight (flightInformation);
        balloonBust.getFlightData().getLinkedGroundUnits().addLinkedGroundUnit(balloonUnit);
		balloonBust.createFlight();
        return balloonBust;
    }

    private IGroundUnitCollection createBalloonUnit(Coordinate balloonPosition, ICountry balloonCountry) throws PWCGException
    {
        BalloonUnitBuilder groundUnitBuilderBalloonDefense = new BalloonUnitBuilder(flightInformation.getMission(), flightInformation.getTargetDefinition());
        IGroundUnitCollection balloonUnit = groundUnitBuilderBalloonDefense.createBalloonUnit(balloonCountry);
        return balloonUnit;
    }

    private ICountry determineBalloonCountry(Side enemySide, Squadron enemyScoutSquadron) throws PWCGException
    {
        ICountry balloonCountry;
        if (enemyScoutSquadron != null)
        {
            int enemyCountryCode = enemyScoutSquadron.determineSquadronCountry(flightInformation.getCampaign().getDate()).getCountryCode();
            balloonCountry = CountryFactory.makeCountryByCode(enemyCountryCode);
        }
        else
        {
            balloonCountry = CountryFactory.makeMapReferenceCountry(enemySide);
        }
        return balloonCountry;
    }
}
