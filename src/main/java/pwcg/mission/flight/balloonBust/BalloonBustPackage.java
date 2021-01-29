package pwcg.mission.flight.balloonBust;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToAir;
import pwcg.mission.target.TargetDefinitionBuilderOpposing;

public class BalloonBustPackage implements IFlightPackage
{
    private FlightInformation flightInformation;
    private TargetDefinition targetDefinition;

    public BalloonBustPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.BALLOON_BUST);
        this.targetDefinition = buildTargetDefintion();

        BalloonBustFlight balloonBustFlight = buildBalloonBustFlight();		
		return balloonBustFlight;
	}

    private BalloonBustFlight buildBalloonBustFlight() throws PWCGException
    {
        BalloonBustFlight balloonBust = new BalloonBustFlight (flightInformation, targetDefinition);
        balloonBust.createFlight();
        return balloonBust;
    }

    private TargetDefinition buildTargetDefintion() throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder;
        if (this.flightInformation.isPlayerFlight())
        {
            targetDefinitionBuilder = new TargetDefinitionBuilderOpposing(flightInformation);
        }
        else
        {
            targetDefinitionBuilder = new TargetDefinitionBuilderAirToAir(flightInformation);
        }

        return  targetDefinitionBuilder.buildTargetDefinition();
    }
}
