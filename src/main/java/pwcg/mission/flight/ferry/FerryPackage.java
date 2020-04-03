package pwcg.mission.flight.ferry;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class FerryPackage implements IFlightPackage

{
    private IFlightInformation flightInformation;

    public FerryPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.FERRY);

		FerryFlight ferry = new FerryFlight (flightInformation);
		ferry.createFlight();
		return ferry;
	}
}
