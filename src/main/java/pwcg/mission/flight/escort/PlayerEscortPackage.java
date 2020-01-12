package pwcg.mission.flight.escort;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class PlayerEscortPackage implements IFlightPackage
{
    private IFlightInformation playerFlightInformation;    
    public PlayerEscortPackage(IFlightInformation playerFlightInformation)
    {
        this.playerFlightInformation = playerFlightInformation;
    }

    public IFlight createPackage () throws PWCGException 
    {
	    if(!playerFlightInformation.isPlayerFlight())
        {
	        throw new PWCGMissionGenerationException ("Attempt to create non player escort package");
        }
	    
	    PlayerEscortedFlightBuilder escortedFlightBuilder = new PlayerEscortedFlightBuilder();
	    IFlight escortedFlight = escortedFlightBuilder.createEscortedFlight(playerFlightInformation);

		PlayerEscortFlight playerEscort = new PlayerEscortFlight(playerFlightInformation, escortedFlight);
		playerEscort.createFlight();
		
		playerEscort.getFlightData().getLinkedFlights().addLinkedFlight(escortedFlight);
		
		return playerEscort;
	}
}
