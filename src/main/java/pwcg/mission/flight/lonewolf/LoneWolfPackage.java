package pwcg.mission.flight.lonewolf;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;

public class LoneWolfPackage implements IFlightPackage
{
    protected IFlightInformation flightInformation;

    public LoneWolfPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.LONE_WOLF);

        LoneWolfFlight patrolFlight = new LoneWolfFlight (flightInformation);
        patrolFlight.createFlight();
        return patrolFlight;
    }
}
