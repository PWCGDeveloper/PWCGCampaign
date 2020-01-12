package pwcg.mission.flight.offensive;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class OffensivePackage implements IFlightPackage
{
    
    protected IFlightInformation flightInformation;

    public OffensivePackage(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public IFlight createPackage () throws PWCGException 
    {
        OffensiveFlight offensivePatrolFlight = new OffensiveFlight (flightInformation);
        offensivePatrolFlight.createFlight();
        return offensivePatrolFlight;
    }
}
