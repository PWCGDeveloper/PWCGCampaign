package pwcg.mission.flight;

import pwcg.core.exception.PWCGException;

public interface IFlightPackage
{
    IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException;
}
