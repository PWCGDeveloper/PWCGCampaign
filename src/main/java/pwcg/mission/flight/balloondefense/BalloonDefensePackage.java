 package pwcg.mission.flight.balloondefense;

import java.util.List;

import pwcg.core.exception.PWCGException;
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

public class BalloonDefensePackage implements IFlightPackage
{
    private IFlightInformation flightInformation;
    private TargetDefinition targetDefinition;
    private IGroundUnitCollection balloonUnit;

    public BalloonDefensePackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.BALLOON_DEFENSE);
        this.targetDefinition = buildTargetDefintion();

        buildBalloon();        
        IFlight balloonDefenseFlight = buildBalloonDefenseFllght();
        createOpposingFlights(balloonDefenseFlight);
		return balloonDefenseFlight;
	}

    private void buildBalloon() throws PWCGException
    {
        BalloonUnitBuilder groundUnitBuilderBalloonDefense = new BalloonUnitBuilder(flightInformation.getMission(), targetDefinition);
        balloonUnit = groundUnitBuilderBalloonDefense.createBalloonUnit(flightInformation.getSquadron().getCountry());
    }

    private IFlight buildBalloonDefenseFllght() throws PWCGException
    {
        IFlight balloonDefenseFlight = new BalloonDefenseFlight(flightInformation, targetDefinition);
        balloonDefenseFlight.addLinkedGroundUnit(balloonUnit);
        balloonDefenseFlight.createFlight();
        return balloonDefenseFlight;
    }

    private TargetDefinition buildTargetDefintion() throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = TargetDefinitionBuilderFactory.createFlightTargetDefinitionBuilder(flightInformation);
        return  targetDefinitionBuilder.buildTargetDefinition();
    }

    private void createOpposingFlights(IFlight balloonDefenseFlight) throws PWCGException
    {
        BalloonDefenseOpposingFlightBuilder opposingFlightBuilder = new BalloonDefenseOpposingFlightBuilder(balloonDefenseFlight.getFlightInformation(), balloonUnit);
        List<IFlight> opposingFlights = opposingFlightBuilder.buildOpposingFlights();
        for (IFlight opposingFlight: opposingFlights)
        {
            balloonDefenseFlight.getLinkedFlights().addLinkedFlight(opposingFlight);
        }
    }
}
