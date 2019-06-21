package pwcg.mission.flight.bomb;

import pwcg.campaign.target.unit.TargetBuilder;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.flight.bomb.BombingWaypoints.BombingAltitudeLevel;
import pwcg.mission.ground.GroundUnitCollection;

public class LowAltBombingPackage implements IFlightPackage
{
    private FlightInformation flightInformation;

    public LowAltBombingPackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public Flight createPackage () throws PWCGException 
    {
        LowAltBombingFlight bombingFlight = createPackageTacticalTarget();
        return bombingFlight;
    }

    public LowAltBombingFlight createPackageTacticalTarget () throws PWCGException 
    {
        GroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(flightInformation.getSquadron().determineEnemySide());
        
        LowAltBombingFlight bombingFlight = makeBombingFlight(targetCoordinates);
        bombingFlight.linkGroundUnitsToFlight(groundUnitCollection);

        return bombingFlight;
    }

    private LowAltBombingFlight makeBombingFlight(Coordinate targetCoordinates)
                    throws PWCGException
    {
        Coordinate startCoords = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
	    MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());	        
        LowAltBombingFlight bombingFlight = new LowAltBombingFlight (flightInformation, missionBeginUnit);
        BombingAltitudeLevel bombingAltitude = BombingAltitudeLevel.LOW;
        bombingFlight.setBombingAltitudeLevel(bombingAltitude);         
        bombingFlight.createUnitMission();
        
        return bombingFlight;
    }

    private GroundUnitCollection createGroundUnitsForFlight() throws PWCGException
    {
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }

}
