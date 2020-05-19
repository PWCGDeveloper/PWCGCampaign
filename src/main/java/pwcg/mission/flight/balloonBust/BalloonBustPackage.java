package pwcg.mission.flight.balloonBust;

import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.builder.BalloonUnitBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderFactory;

public class BalloonBustPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;
    private TargetDefinition targetDefinition;
    private IGroundUnitCollection balloonUnit;

    public BalloonBustPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.BALLOON_BUST);
        this.targetDefinition = buildTargetDefintion();

        buildBalloon();
        BalloonBustFlight balloonBustFlight = buildBalloonBustFlight();
		buildOpposingFlights(balloonBustFlight);
		
		return balloonBustFlight;
	}

    private void buildBalloon() throws PWCGException
    {
        Side enemySide = flightInformation.getSquadron().determineEnemySide();
        Squadron enemyScoutSquadron = PWCGContext.getInstance().getSquadronManager().getSquadronByProximityAndRoleAndSide(
                        flightInformation.getCampaign(), 
                        flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate()), 
                        Role.ROLE_FIGHTER, 
                        flightInformation.getSquadron().determineSquadronCountry(flightInformation.getCampaign().getDate()).getSide().getOppositeSide());
        ICountry balloonCountry = determineBalloonCountry(enemySide, enemyScoutSquadron);
        balloonUnit = createBalloonUnit(targetDefinition.getPosition(), balloonCountry);
    }

    private BalloonBustFlight buildBalloonBustFlight() throws PWCGException
    {
        BalloonBustFlight balloonBust = new BalloonBustFlight (flightInformation, targetDefinition);
        balloonBust.addLinkedGroundUnit(balloonUnit);
        balloonBust.createFlight();
        return balloonBust;
    }

    private IGroundUnitCollection createBalloonUnit(Coordinate balloonPosition, ICountry balloonCountry) throws PWCGException
    {
        BalloonUnitBuilder groundUnitBuilderBalloonDefense = new BalloonUnitBuilder(flightInformation.getMission(), targetDefinition);
        IGroundUnitCollection balloonUnit = groundUnitBuilderBalloonDefense.createBalloonUnit(balloonCountry);
        if (balloonUnit == null)
        {
          System.out.println("oops");  
        }
        return balloonUnit;
    }

    private TargetDefinition buildTargetDefintion() throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = TargetDefinitionBuilderFactory.createFlightTargetDefinitionBuilder(flightInformation);
        return  targetDefinitionBuilder.buildTargetDefinition();
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

    private void buildOpposingFlights(IFlight balloonBustFlight) throws PWCGException
    {
        if (this.flightInformation.isPlayerFlight())
        {
            BalloonBustOpposingFlightBuilder opposingFlightBuilder = new BalloonBustOpposingFlightBuilder(flightInformation, balloonUnit);
            List<IFlight> opposingFlights = opposingFlightBuilder.buildOpposingFlights();
            for (IFlight opposingFlight: opposingFlights)
            {
                balloonBustFlight.getLinkedFlights().addLinkedFlight(opposingFlight);
            }
        }
    }
}
