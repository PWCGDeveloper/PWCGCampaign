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
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.builder.AAAUnitBuilder;
import pwcg.mission.ground.builder.SearchLightBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderStrategic;

public class StrategicBombingPackage implements IFlightPackage
{
    private List<IFlight> packageFlights = new ArrayList<>();

    public StrategicBombingPackage()
    {
    }
    
    
    @Override
    public List<IFlight> createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {        
        FlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.STRATEGIC_BOMB);
        TargetDefinition targetDefinition = buildTargetDefinition(flightInformation);
        
        BombingFlight bombingFlight = new BombingFlight (flightInformation, targetDefinition);
        bombingFlight.createFlight();
        
        createAAA(flightInformation, targetDefinition, bombingFlight);
        createSearchlight(flightInformation, targetDefinition, bombingFlight);

        packageFlights.add(bombingFlight);
        return packageFlights;
    }
    
    private TargetDefinition buildTargetDefinition(FlightInformation flightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderStrategic(flightInformation);
        return targetDefinitionBuilder.buildTargetDefinition();
    }

    private void createAAA(FlightInformation flightInformation, TargetDefinition targetDefinition, BombingFlight bombingFlight) throws PWCGException
    {
        AAAUnitBuilder groundUnitBuilder = new AAAUnitBuilder(flightInformation.getCampaign(), targetDefinition);
        GroundUnitCollection aaaArty = groundUnitBuilder.createAAAArtilleryBattery(GroundUnitSize.GROUND_UNIT_SIZE_HIGH);
        flightInformation.getMission().getGroundUnitBuilder().addFlightSpecificGroundUnit(aaaArty);
    }

    private void createSearchlight(FlightInformation flightInformation, TargetDefinition targetDefinition, BombingFlight bombingFlight) throws PWCGException
    {
        if (flightInformation.getMission().isNightMission())
        {
            SearchLightBuilder groundUnitBuilder =  new SearchLightBuilder(flightInformation.getCampaign());
            GroundUnitCollection searchLightGroup = groundUnitBuilder.createSearchLightGroup(targetDefinition);
            flightInformation.getMission().getGroundUnitBuilder().addFlightSpecificGroundUnit(searchLightGroup);
        }
    }
}
