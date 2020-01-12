package pwcg.mission.flight.recon;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class ReconPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;

    public ReconPackage(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public IFlight createPackage () throws PWCGException 
    {
        ReconFlight reconFlight = new ReconFlight (flightInformation);
        reconFlight.createFlight();
        return reconFlight;
    }
}
