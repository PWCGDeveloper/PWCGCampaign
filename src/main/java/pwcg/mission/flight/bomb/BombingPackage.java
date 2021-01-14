package pwcg.mission.flight.bomb;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetBuilder;

public class BombingPackage implements IFlightPackage
{
    private FlightTypes flightType;

    public BombingPackage(FlightTypes flightType)
    {
        this.flightType = flightType;
    }
    
    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {        
        FlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, flightType);
        TargetDefinition targetDefinition = buildTargetDefinition(flightInformation);
        
        BombingFlight bombingFlight = new BombingFlight (flightInformation, targetDefinition);
        bombingFlight.createFlight();
        return bombingFlight;
    }
    
    private TargetDefinition buildTargetDefinition(FlightInformation flightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetBuilder(flightInformation);
        return targetDefinitionBuilder.buildTargetDefinition();
    }
}
