package pwcg.mission.flight.bomb;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightPackage;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.bomb.BombingWaypoints.BombingAltitudeLevel;
import pwcg.mission.ground.GroundUnitCollection;

public class LowAltBombingPackage extends FlightPackage
{
    public LowAltBombingPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = FlightTypes.LOW_ALT_BOMB;
    }

    public Flight createPackage () throws PWCGException 
    {
        LowAltBombingFlight bombingFlight = createPackageTacticalTarget();
        return bombingFlight;
    }

    public LowAltBombingFlight createPackageTacticalTarget () throws PWCGException 
    {
        GroundUnitCollection groundUnitCollection = createSpecificGroundUnitsForFlight(TacticalTarget.TARGET_ASSAULT);
        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(squadron.determineEnemySide(campaign.getDate()));
        
        LowAltBombingFlight bombingFlight = makeBombingFlight(targetCoordinates);
        bombingFlight.linkGroundUnitsToFlight(groundUnitCollection);

        return bombingFlight;
    }

    private LowAltBombingFlight makeBombingFlight(Coordinate targetCoordinates)
                    throws PWCGException
    {
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(startCoords.copy());
            
        LowAltBombingFlight bombingFlight = new LowAltBombingFlight ();
        bombingFlight.initialize(mission, campaign, FlightTypes.LOW_ALT_BOMB, targetCoordinates, squadron, missionBeginUnit, isPlayerFlight);

        BombingAltitudeLevel bombingAltitude = BombingAltitudeLevel.LOW;
        bombingFlight.setBombingAltitudeLevel(bombingAltitude);         
        bombingFlight.createUnitMission();
        
        return bombingFlight;
    }

}
