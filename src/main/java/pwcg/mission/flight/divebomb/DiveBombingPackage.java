package pwcg.mission.flight.divebomb;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetBuilder;

public class DiveBombingPackage implements IFlightPackage
{
    public DiveBombingPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {        
        IFlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.DIVE_BOMB);
        TargetDefinition targetDefinition = buildTargetDefinition(flightInformation);
        
        DiveBombingFlight bombingFlight = new DiveBombingFlight (flightInformation, targetDefinition);
        bombingFlight.createFlight();
        return bombingFlight;
    }
    
    private TargetDefinition buildTargetDefinition(IFlightInformation flightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetBuilder(flightInformation);
        return targetDefinitionBuilder.buildTargetDefinition();
    }
}
