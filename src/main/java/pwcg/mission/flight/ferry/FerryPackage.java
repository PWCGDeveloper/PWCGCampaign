package pwcg.mission.flight.ferry;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class FerryPackage implements IFlightPackage

{
    private IFlightInformation flightInformation;

    public FerryPackage(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

	public IFlight createPackage () throws PWCGException 
	{
		FerryFlight ferry = new FerryFlight (flightInformation);
		ferry.createFlight();
		return ferry;
	}
}
