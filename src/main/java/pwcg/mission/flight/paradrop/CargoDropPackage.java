package pwcg.mission.flight.paradrop;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;

public class CargoDropPackage implements IFlightPackage
{
    public CargoDropPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        FlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.CARGO_DROP);
        TargetDefinition targetDefinition = buildTargetDefintion(flightInformation);

        CargoDropFlight cargoDropFlight = new CargoDropFlight (flightInformation, targetDefinition);
        cargoDropFlight.createFlight();
        return cargoDropFlight;
    }

    private TargetDefinition buildTargetDefintion(FlightInformation flightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder(flightInformation);
        return targetDefinitionBuilder.buildTargetDefinition();
    }
}
