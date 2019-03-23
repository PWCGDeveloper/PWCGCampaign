package pwcg.mission.flight.bomb;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightPackage;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.bomb.BombingWaypoints.BombingAltitudeLevel;
import pwcg.mission.ground.GroundUnitCollection;

public class BombingPackage extends FlightPackage
{
    public BombingPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = FlightTypes.BOMB;
    }

    public Flight createPackage () throws PWCGException 
	{
	    BombingFlight bombingFlight = createPackageTacticalTarget ();
		return bombingFlight;
	}

	public BombingFlight createPackageTacticalTarget () throws PWCGException 
	{
        GroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(squadron.determineEnemySide());

        BombingFlight bombingFlight = makeBombingFlight(targetCoordinates);
        bombingFlight.linkGroundUnitsToFlight(groundUnitCollection);
        addPossibleEnemyScramble(bombingFlight, groundUnitCollection);

        return bombingFlight;
	}

    private BombingFlight makeBombingFlight(Coordinate targetCoordinates)
                    throws PWCGException
    {
        // Now the actual bombing mission
	    Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
	    MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
	    missionBeginUnit.initialize(startCoords.copy());
	        
        FlightInformation flightInformation = createFlightInformation(targetCoordinates);
        BombingFlight bombingFlight = new BombingFlight (flightInformation, missionBeginUnit);

	    // Set the altitude based on the aircraft type
	    BombingAltitudeLevel bombingAltitude = BombingAltitudeLevel.MED;
	    bombingFlight.setBombingAltitudeLevel(bombingAltitude);	        
	    bombingFlight.createUnitMission();
	    
	    return bombingFlight;
    }
}
