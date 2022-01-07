package pwcg.mission.flight.cap;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToAir;

public class CAPPackage implements IFlightPackage
{	
    private FlightInformation flightInformation;
    private TargetDefinition targetDefinition;
    private List<IFlight> packageFlights = new ArrayList<>();

    public CAPPackage()
    {
    }

    @Override
    public List<IFlight> createFlightPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation);
        this.targetDefinition = buildTargetDefintion();

        CAPFlight interceptFlight = new CAPFlight (flightInformation, targetDefinition);
        interceptFlight.createFlight();
        
        packageFlights.add(interceptFlight);
        return packageFlights;
    }

    private TargetDefinition buildTargetDefintion() throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderAirToAir(flightInformation);
        return  targetDefinitionBuilder.buildTargetDefinition();
    }
}
