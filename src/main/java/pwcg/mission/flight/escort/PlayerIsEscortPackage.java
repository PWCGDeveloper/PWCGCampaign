package pwcg.mission.flight.escort;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.flight.scramble.AirfieldAttackScrambleFlightBuilder;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToAir;

public class PlayerIsEscortPackage implements IFlightPackage
{
    private FlightInformation playerFlightInformation;    
    private TargetDefinition targetDefinition;
    private List<IFlight> packageFlights = new ArrayList<>();

    public PlayerIsEscortPackage()
    {    }

    @Override
    public List<IFlight> createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.playerFlightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.ESCORT);

	    if(!playerFlightInformation.isPlayerFlight())
        {
	        throw new PWCGMissionGenerationException ("Attempt to create non player escort package");
        }
	    
        this.targetDefinition = buildTargetDefintion();

	    EscortedByPlayerFlightBuilder escortedFlightBuilder = new EscortedByPlayerFlightBuilder(playerFlightInformation, targetDefinition);
	    EscortedByPlayerFlight escortedFlight = escortedFlightBuilder.createEscortedFlight();

		PlayerIsEscortFlight playerEscort = new PlayerIsEscortFlight(playerFlightInformation, targetDefinition, escortedFlight);
		playerEscort.createFlight();
        
        PlayerIsEscortFlightConnector connector = new PlayerIsEscortFlightConnector(playerEscort, escortedFlight);
        connector.connectEscortAndEscortedFlight();
		
        playerEscort.setAssociatedFlight(escortedFlight);
        
        IFlight scrambleFlight = addScrambleFlight(escortedFlight);
        if (scrambleFlight != null)
        {
            packageFlights.add(scrambleFlight);
        }

        packageFlights.add(playerEscort);
        packageFlights.add(escortedFlight);
		
		return packageFlights;
	}

    private IFlight addScrambleFlight(IFlight flight) throws PWCGException
    {
        if (flight.isPlayerFlight())
        {
            return AirfieldAttackScrambleFlightBuilder.addAirfieldScrambleToFlight(flight);
        }
        return null;
    }

    private TargetDefinition buildTargetDefintion() throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderAirToAir(playerFlightInformation);
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinition();
        return targetDefinition;
    }
}
