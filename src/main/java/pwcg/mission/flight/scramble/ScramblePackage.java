package pwcg.mission.flight.scramble;

import java.util.ArrayList;
import java.util.List;

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

public class ScramblePackage implements IFlightPackage
{
    private FlightInformation flightInformation;
    private TargetDefinition targetDefinition;
    private List<IFlight> packageFlights = new ArrayList<>();

    @Override
    public List<IFlight> createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.SCRAMBLE);        
        this.targetDefinition = buildTargetDefintion();

        IFlight scrambleFlight = null;
        if (flightBuildInformation.isPlayerFlight())
        {
            adjustAltitudeToMatchOpposingFlight();
            PlayerScrambleFlight playerFlight = createPlayerFlight();
            FlightSpotterBuilder.createSpotters(playerFlight, flightInformation);
            scrambleFlight = playerFlight;
        }
        else
        {
            AiScrambleFlight aiFlight = createAiFlight();
            scrambleFlight = aiFlight;
        }

        packageFlights.add(scrambleFlight);
        return packageFlights;
	}

    private TargetDefinition buildTargetDefintion() throws PWCGException
    {
        ITargetDefinitionBuilder opposingTargetDefinitionBuilder;
        if (this.flightInformation.isPlayerFlight())
        {
            opposingTargetDefinitionBuilder = new TargetDefinitionBuilderOpposing(flightInformation);
        }
        else
        {
            opposingTargetDefinitionBuilder = new TargetDefinitionBuilderAirToAir(flightInformation);
        }

        TargetDefinition targetDefinition =  opposingTargetDefinitionBuilder.buildTargetDefinition();
        if (targetDefinition.getPosition().getYPos() < ScrambleWaypointFactory.SCRAMBLE_MINIMUM_ALTITUDE)
        {
            targetDefinition.getPosition().setYPos(ScrambleWaypointFactory.SCRAMBLE_MINIMUM_ALTITUDE);
        }
        return targetDefinition;
    }

    private PlayerScrambleFlight createPlayerFlight() throws PWCGException
    {
        PlayerScrambleFlight playerFlight = new PlayerScrambleFlight (flightInformation, targetDefinition);
        playerFlight.createFlight();
        return playerFlight;
    }

    private AiScrambleFlight createAiFlight() throws PWCGException
    {
        AiScrambleFlight aiFlight = new AiScrambleFlight (flightInformation, targetDefinition);
        aiFlight.createFlight();
        return aiFlight;
    }

    private void adjustAltitudeToMatchOpposingFlight()
    { 
        if (targetDefinition.getPosition().getYPos() > ScrambleWaypointFactory.SCRAMBLE_MINIMUM_ALTITUDE)
        {
            int numAltitudeChunks = Double.valueOf(targetDefinition.getPosition().getYPos()).intValue() / 500;
            ++numAltitudeChunks;
            int interceptAltitude = numAltitudeChunks * 500;
            flightInformation.setAltitude(interceptAltitude);
        }
        else
        {
            flightInformation.setAltitude(ScrambleWaypointFactory.SCRAMBLE_MINIMUM_ALTITUDE);
        }
    }
}
