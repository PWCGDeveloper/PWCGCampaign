package pwcg.mission.flight.offensive;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class OffensivePackage implements IFlightPackage
{
    
    protected IFlightInformation flightInformation;

    public OffensivePackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.OFFENSIVE);

        OffensiveFlight offensivePatrolFlight = new OffensiveFlight (flightInformation);
        offensivePatrolFlight.createFlight();
        return offensivePatrolFlight;
    }
}
