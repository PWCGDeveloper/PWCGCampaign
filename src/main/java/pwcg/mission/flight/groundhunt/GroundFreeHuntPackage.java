package pwcg.mission.flight.groundhunt;

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

public class GroundFreeHuntPackage implements IFlightPackage
{
    private List<IFlight> packageFlights = new ArrayList<>();

    @Override
    public List<IFlight> createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {        
        FlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.GROUND_HUNT);
        TargetDefinition targetDefinition = buildTargetDefinition(flightInformation);
        
        GroundFreeHuntFlight groundAttackFlight = new GroundFreeHuntFlight (flightInformation, targetDefinition);
		groundAttackFlight.createFlight();

        packageFlights.add(groundAttackFlight);
        return packageFlights;
    }

    private TargetDefinition buildTargetDefinition(FlightInformation flightInformation) throws PWCGException
    {
        return GroundTargetDefinitionFactory.buildTargetDefinition(flightInformation);
    }
}
