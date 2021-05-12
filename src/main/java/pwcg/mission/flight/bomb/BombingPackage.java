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
import pwcg.mission.flight.scramble.AirfieldAttackScrambleFlightBuilder;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilder;

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
        
        IFlight scrambleFlight = addScrambleFlight(bombingFlight);
        if (scrambleFlight != null)
        {
            packageFlights.add(scrambleFlight);
        }
        
        packageFlights.add(bombingFlight);
        return packageFlights;
    }

    private IFlight addScrambleFlight(IFlight flight) throws PWCGException
    {
        if (flight.isPlayerFlight())
        {
            return AirfieldAttackScrambleFlightBuilder.addAirfieldScrambleToFlight(flight);
        }
        return null;
    }

    private TargetDefinition buildTargetDefinition(FlightInformation flightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder(flightInformation);
        return targetDefinitionBuilder.buildTargetDefinition();
    }
}
