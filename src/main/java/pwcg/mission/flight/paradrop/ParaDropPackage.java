package pwcg.mission.flight.paradrop;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.GroundTargetDefinitionFactory;

public class ParaDropPackage implements IFlightPackage
{
    private List<IFlight> packageFlights = new ArrayList<>();

    @Override
    public List<IFlight> createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        FlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.PARATROOP_DROP);
        TargetDefinition targetDefinition = buildTargetDefintion(flightInformation);

        ParaDropFlight paraDropFlight = new ParaDropFlight (flightInformation, targetDefinition);
        paraDropFlight.createFlight();

        packageFlights.add(paraDropFlight);
        return packageFlights;
    }

    private TargetDefinition buildTargetDefintion(FlightInformation flightInformation) throws PWCGException
    {
        return GroundTargetDefinitionFactory.buildTargetDefinition(flightInformation);
    }
}
