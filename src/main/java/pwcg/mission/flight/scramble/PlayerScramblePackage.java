package pwcg.mission.flight.scramble;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightSpotterBuilder;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToAir;
import pwcg.mission.target.TargetDefinitionBuilderOpposing;

public class PlayerScramblePackage implements IFlightPackage
{
    private FlightInformation flightInformation;
    private TargetDefinition targetDefinition;

    public PlayerScramblePackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.SCRAMBLE);
        this.targetDefinition = buildTargetDefintion();
        
		PlayerScrambleFlight playerFlight = createPlayerFlight();
        FlightSpotterBuilder.createSpotters(playerFlight, flightInformation);

		return playerFlight;
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

        TargetDefinition targetDefinition =  targetDefinitionBuilder.buildTargetDefinition();
        
        if (targetDefinition.getPosition().getYPos() < 1000)
        {
            targetDefinition.getPosition().setYPos(1000);
        }
        return targetDefinition;
    }

    private PlayerScrambleFlight createPlayerFlight() throws PWCGException
    {
        PlayerScrambleFlight playerFlight = new PlayerScrambleFlight (flightInformation, targetDefinition);
        playerFlight.createFlight();
        return playerFlight;
    }
}
