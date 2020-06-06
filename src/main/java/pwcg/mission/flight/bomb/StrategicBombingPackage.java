package pwcg.mission.flight.bomb;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.builder.AAAUnitBuilder;
import pwcg.mission.ground.builder.SearchLightBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderStrategic;

public class StrategicBombingPackage implements IFlightPackage
{
    public StrategicBombingPackage()
    {
    }
    
    
    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {        
        IFlightInformation flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.STRATEGIC_BOMB);
        TargetDefinition targetDefinition = buildTargetDefinition(flightInformation);
        
        BombingFlight bombingFlight = new BombingFlight (flightInformation, targetDefinition);
        bombingFlight.createFlight();
        
        createAAA(flightInformation, targetDefinition, bombingFlight);
        createSearchlight(flightInformation, targetDefinition, bombingFlight);

        return bombingFlight;
    }
    
    private TargetDefinition buildTargetDefinition(IFlightInformation flightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderStrategic(flightInformation);
        return targetDefinitionBuilder.buildTargetDefinition();
    }

    private void createAAA(IFlightInformation flightInformation, TargetDefinition targetDefinition, BombingFlight bombingFlight) throws PWCGException
    {
        AAAUnitBuilder groundUnitBuilder = new AAAUnitBuilder(flightInformation.getCampaign(), targetDefinition);
        IGroundUnitCollection aaaArty = groundUnitBuilder.createAAAArtilleryBattery(GroundUnitSize.GROUND_UNIT_SIZE_HIGH);
        flightInformation.getMission().getMissionGroundUnitBuilder().addFlightSpecificGroundUnit(aaaArty);
    }

    private void createSearchlight(IFlightInformation flightInformation, TargetDefinition targetDefinition, BombingFlight bombingFlight) throws PWCGException
    {
        if (flightInformation.getMission().isNightMission())
        {
            SearchLightBuilder groundUnitBuilder =  new SearchLightBuilder(flightInformation.getCampaign());
            IGroundUnitCollection searchLightGroup = groundUnitBuilder.createSearchLightGroup(targetDefinition);
            flightInformation.getMission().getMissionGroundUnitBuilder().addFlightSpecificGroundUnit(searchLightGroup);
        }
    }
}
