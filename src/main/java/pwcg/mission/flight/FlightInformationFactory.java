package pwcg.mission.flight;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.plane.FlightPlaneBuilder;

public class FlightInformationFactory
{
    public static FlightInformation buildFlightInformation(FlightBuildInformation flightBuildInformation) throws PWCGException
    {
        FlightInformation flightInformation = new FlightInformation(flightBuildInformation);
        FlightPlaneBuilder.buildPlanes (flightInformation);
        flightInformation.calculateAltitude();
        
        return flightInformation;
    }
}
