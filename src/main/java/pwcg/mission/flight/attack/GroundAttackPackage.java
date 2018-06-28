package pwcg.mission.flight.attack;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightPackage;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.GroundUnitCollection;

public class GroundAttackPackage extends FlightPackage
{
    public GroundAttackPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = FlightTypes.GROUND_ATTACK;
    }

	public Flight createPackage () throws PWCGException 
	{
        GroundUnitCollection groundUnits = createGroundUnitsForFlight();
        GroundAttackFlight groundAttackFlight = createFlight(groundUnits);
        addPossibleEscort(groundAttackFlight);        
        addPossibleEnemyScramble(groundAttackFlight, groundUnits);
		return groundAttackFlight;
	}

    private GroundAttackFlight createFlight(GroundUnitCollection groundUnitCollection) throws PWCGException
    {
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(startCoords.copy());

        GroundAttackFlight groundAttackFlight = new GroundAttackFlight ();
        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(squadron.determineEnemySide(campaign.getDate()));
		groundAttackFlight.initialize(mission, campaign, targetCoordinates, squadron, missionBeginUnit, isPlayerFlight);

		groundAttackFlight.linkGroundUnitsToFlight(groundUnitCollection);
		groundAttackFlight.createUnitMission();
        return groundAttackFlight;
    }
}
