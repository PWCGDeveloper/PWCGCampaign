package pwcg.mission.flight.recon;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class ReconPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;

    public ReconPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.RECON);

        ReconFlight reconFlight = new ReconFlight (flightInformation);
        reconFlight.createFlight();
        return reconFlight;
    }
}
