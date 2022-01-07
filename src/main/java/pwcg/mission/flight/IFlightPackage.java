package pwcg.mission.flight;

import java.util.List;

import pwcg.core.exception.PWCGException;

public interface IFlightPackage
{
    List<IFlight> createFlightPackage(FlightBuildInformation flightBuildInformation) throws PWCGException;
}
