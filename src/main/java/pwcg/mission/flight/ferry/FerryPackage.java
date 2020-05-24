package pwcg.mission.flight.ferry;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToAir;

public class FerryPackage implements IFlightPackage

{
    public FerryPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        IFlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.FERRY);
        TargetDefinition targetDefinition = buildTargetDefintion(flightInformation);

		FerryFlight ferry = new FerryFlight (flightInformation, targetDefinition);
		ferry.createFlight();
		return ferry;
	}
    
    private TargetDefinition buildTargetDefintion(IFlightInformation flightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderAirToAir(flightInformation);
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinition();
        return targetDefinition;
    }
}
