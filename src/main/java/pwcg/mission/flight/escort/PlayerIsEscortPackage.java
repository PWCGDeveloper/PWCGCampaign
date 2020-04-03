package pwcg.mission.flight.escort;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class PlayerIsEscortPackage implements IFlightPackage
{
    private IFlightInformation playerFlightInformation;    
    public PlayerIsEscortPackage()
    {    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.playerFlightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.ESCORT);

	    if(!playerFlightInformation.isPlayerFlight())
        {
	        throw new PWCGMissionGenerationException ("Attempt to create non player escort package");
        }
	    
	    EscortedByPlayerFlightBuilder escortedFlightBuilder = new EscortedByPlayerFlightBuilder(playerFlightInformation);
	    EscortedByPlayerFlight escortedFlight = escortedFlightBuilder.createEscortedFlight();

		PlayerIsEscortFlight playerEscort = new PlayerIsEscortFlight(playerFlightInformation, escortedFlight);
		playerEscort.createFlight();
        
        PlayerIsEscortFlightConnector connector = new PlayerIsEscortFlightConnector(playerEscort, escortedFlight);
        connector.connectEscortAndEscortedFlight();

		playerEscort.getLinkedFlights().addLinkedFlight(escortedFlight);
		
		return playerEscort;
	}
}
