package pwcg.mission.flight.bomb;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.builder.AAAUnitBuilder;
import pwcg.mission.ground.builder.SearchLightBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.TargetDefinition;

public class StrategicBombingPackage implements IFlightPackage
{
    private FlightInformation flightInformation;

    public StrategicBombingPackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public Flight createPackage() throws PWCGException
    {
        StrategicBombingFlight strategicBombingFlight = createStrategicBombingFlight();

        createAAA(flightInformation.getTargetDefinition(), strategicBombingFlight);
        createSearchlight(flightInformation.getTargetDefinition(), strategicBombingFlight);
        return strategicBombingFlight;
    }

    private StrategicBombingFlight createStrategicBombingFlight() throws PWCGException
    {
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
	    MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());	        
        StrategicBombingFlight strategicBombingFlight = new StrategicBombingFlight(flightInformation, missionBeginUnit);
        strategicBombingFlight.createUnitMission();

        return strategicBombingFlight;
    }

    private void createAAA(TargetDefinition targetDefinition, StrategicBombingFlight strategicBombingFlight) throws PWCGException
    {
        AAAUnitBuilder groundUnitBuilder = new AAAUnitBuilder(flightInformation.getCampaign(), targetDefinition.getTargetCountry(), targetDefinition.getTargetPosition());
        IGroundUnitCollection aaaArty = groundUnitBuilder.createAAAArtilleryBattery(GroundUnitSize.GROUND_UNIT_SIZE_HIGH);
        strategicBombingFlight.addLinkedUnit(aaaArty);
    }

    private void createSearchlight(TargetDefinition targetDefinition, StrategicBombingFlight strategicBombingFlight) throws PWCGException
    {
        SearchLightBuilder groundUnitBuilder =  new SearchLightBuilder(flightInformation.getCampaign());
        if (flightInformation.getMission().isNightMission())
        {
            IGroundUnitCollection searchLightGroup = groundUnitBuilder.createSearchLightGroup(targetDefinition);
            strategicBombingFlight.addLinkedUnit(searchLightGroup);
        }
    }
}
