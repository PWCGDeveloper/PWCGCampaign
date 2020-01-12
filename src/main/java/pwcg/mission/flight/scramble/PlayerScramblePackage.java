package pwcg.mission.flight.scramble;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class PlayerScramblePackage implements IFlightPackage
{
    private IFlightInformation flightInformation;

    public PlayerScramblePackage(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public IFlight createPackage () throws PWCGException 
	{        		
		PlayerScrambleFlight scramble = new PlayerScrambleFlight (flightInformation);
		scramble.createFlight();
		return scramble;
	}
}
