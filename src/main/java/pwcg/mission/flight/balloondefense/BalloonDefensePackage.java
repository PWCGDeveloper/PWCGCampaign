 package pwcg.mission.flight.balloondefense;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToAir;

public class BalloonDefensePackage implements IFlightPackage
{
    private FlightInformation flightInformation;
    private TargetDefinition targetDefinition;

    public BalloonDefensePackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.BALLOON_DEFENSE);
        this.targetDefinition = buildTargetDefintion();

        IFlight balloonDefenseFlight = buildBalloonDefenseFllght();
        createOpposingFlights(balloonDefenseFlight);
		return balloonDefenseFlight;
	}

    private IFlight buildBalloonDefenseFllght() throws PWCGException
    {
        IFlight balloonDefenseFlight = new BalloonDefenseFlight(flightInformation, targetDefinition);
        balloonDefenseFlight.createFlight();
        return balloonDefenseFlight;
    }

    private TargetDefinition buildTargetDefintion() throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderAirToAir(flightInformation);
        return  targetDefinitionBuilder.buildTargetDefinition();
    }

    private void createOpposingFlights(IFlight balloonDefenseFlight) throws PWCGException
    {
        BalloonDefenseOpposingFlightBuilder opposingFlightBuilder = new BalloonDefenseOpposingFlightBuilder(balloonDefenseFlight.getFlightInformation(), targetDefinition.getPosition());
        List<IFlight> opposingFlights = opposingFlightBuilder.buildOpposingFlights();
        for (IFlight opposingFlight: opposingFlights)
        {
            balloonDefenseFlight.getLinkedFlights().addLinkedFlight(opposingFlight);
        }
    }
}
