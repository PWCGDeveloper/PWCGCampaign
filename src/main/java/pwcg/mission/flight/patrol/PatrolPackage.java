package pwcg.mission.flight.patrol;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class PatrolPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;

    public PatrolPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.PATROL);

        PatrolFlight patrolFlight = new PatrolFlight (flightInformation);
        patrolFlight.createFlight();
        return patrolFlight;
	}
}
