package pwcg.mission.flight.paradrop;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilder;

public class ParaDropPackage implements IFlightPackage
{
    public ParaDropPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        FlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.PARATROOP_DROP);
        TargetDefinition targetDefinition = buildTargetDefintion(flightInformation);

        ParaDropFlight paraDropFlight = new ParaDropFlight (flightInformation, targetDefinition);
        paraDropFlight.createFlight();
        return paraDropFlight;
    }

    private TargetDefinition buildTargetDefintion(FlightInformation flightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder(flightInformation);
        return targetDefinitionBuilder.buildTargetDefinition();
    }
}
