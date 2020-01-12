package pwcg.mission.flight.lonewolf;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class LoneWolfPackage implements IFlightPackage
{
    protected IFlightInformation flightInformation;

    public LoneWolfPackage(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public IFlight createPackage () throws PWCGException 
    {
        LoneWolfFlight patrolFlight = new LoneWolfFlight (flightInformation);
        patrolFlight.createFlight();
        return patrolFlight;
    }
}
