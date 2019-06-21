package pwcg.mission.flight.bomb;

import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.flight.bomb.BombingWaypoints.BombingAltitudeLevel;
import pwcg.mission.ground.GroundUnitFactory;
import pwcg.mission.ground.factory.AAAUnitFactory;
import pwcg.mission.ground.unittypes.GroundUnitSpawning;
import pwcg.mission.ground.unittypes.SpotLightGroup;

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
        createSpotlight(flightInformation.getTargetDefinition(), strategicBombingFlight);
        return strategicBombingFlight;
    }

    private StrategicBombingFlight createStrategicBombingFlight() throws PWCGException
    {
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
	    MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());	        
        StrategicBombingFlight strategicBombingFlight = new StrategicBombingFlight(flightInformation, missionBeginUnit);
        strategicBombingFlight.setBombingAltitudeLevel(BombingAltitudeLevel.HIGH);
        strategicBombingFlight.createUnitMission();

        return strategicBombingFlight;
    }

    private void createAAA(TargetDefinition targetDefinition, StrategicBombingFlight strategicBombingFlight) throws PWCGException
    {
        AAAUnitFactory groundUnitFactory = new AAAUnitFactory(flightInformation.getCampaign(), targetDefinition.getTargetCountry(), targetDefinition.getTargetPosition());
        GroundUnitSpawning aaaArty = groundUnitFactory.createAAAArtilleryBattery(3, 3);
        strategicBombingFlight.addLinkedUnit(aaaArty);
    }

    private GroundUnitFactory createSpotlight(TargetDefinition targetDefinition, StrategicBombingFlight strategicBombingFlight) throws PWCGException
    {
        GroundUnitFactory groundUnitFactory =  new GroundUnitFactory(flightInformation.getCampaign(), targetDefinition.getTargetPosition(), targetDefinition.getTargetCountry());
        if (flightInformation.getSquadron().determineIsNightSquadron())
        {
            if (flightInformation.getSquadron().determineIsNightSquadron())
            {
                SpotLightGroup spotLightGroup = groundUnitFactory.createSpotLightGroup();
                strategicBombingFlight.addLinkedUnit(spotLightGroup);
            }
        }
        return groundUnitFactory;
    }
}
