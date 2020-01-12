package pwcg.mission.flight.patrol;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class PatrolPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;

    public PatrolPackage(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public IFlight createPackage () throws PWCGException 
    {
        PatrolFlight patrolFlight = new PatrolFlight (flightInformation);
        patrolFlight.createFlight();
        return patrolFlight;
	}
}
