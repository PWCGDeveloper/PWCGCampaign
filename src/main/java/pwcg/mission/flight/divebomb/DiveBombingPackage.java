package pwcg.mission.flight.divebomb;

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

public class DiveBombingPackage extends FlightPackage
{
    public DiveBombingPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = FlightTypes.DIVE_BOMB;
    }

    public Flight createPackage () throws PWCGException 
	{
        GroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(squadron.determineEnemySide());

		// Now the actual artillery spot mission
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(startCoords.copy());
        
        DiveBombingFlight diveBombingFlight = createDiveBombingFlight();
		diveBombingFlight.initialize(mission, campaign, FlightTypes.DIVE_BOMB, targetCoordinates, squadron, missionBeginUnit, isPlayerFlight);
		diveBombingFlight.linkGroundUnitsToFlight(groundUnitCollection);
		
        diveBombingFlight.createUnitMission();

        addPossibleEscort(diveBombingFlight);		
        addPossibleEnemyScramble(diveBombingFlight, groundUnitCollection);

		return diveBombingFlight;
	}

    protected DiveBombingFlight createDiveBombingFlight()
    {
        DiveBombingFlight diveBombingFlight = new DiveBombingFlight ();
        return diveBombingFlight;
    }
}
