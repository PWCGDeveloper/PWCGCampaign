package pwcg.mission.flight.bomb;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.target.GroundTargetDefinitionFactory;
import pwcg.mission.target.TargetDefinition;

public class BombingPackage implements IFlightPackage
{
    private FlightTypes flightType;
    private List<IFlight> packageFlights = new ArrayList<>();

    public BombingPackage(FlightTypes flightType)
    {
        this.flightType = flightType;
    }
    
    @Override
    public List<IFlight> createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {        
        FlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, flightType);
        TargetDefinition targetDefinition = buildTargetDefinition(flightInformation);
        
        BombingFlight bombingFlight = new BombingFlight (flightInformation, targetDefinition);
        bombingFlight.createFlight();
 
        packageFlights.add(bombingFlight);
        return packageFlights;
    }

    private TargetDefinition buildTargetDefinition(FlightInformation flightInformation) throws PWCGException
    {
        return GroundTargetDefinitionFactory.buildTargetDefinition(flightInformation);
    }
}
