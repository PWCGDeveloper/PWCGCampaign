package pwcg.mission.flight.spy;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightPackage;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.GroundUnitCollection;

public class SpyExtractPackage extends FlightPackage
{
    public SpyExtractPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = FlightTypes.SPY_EXTRACT;
    }

    public Flight createPackage () throws PWCGException 
	{
        // Create a target ground defense package
        GroundUnitCollection groundUnitCollection = createGroundUnitsForFlight();
        Coordinate targetCoordinates = groundUnitCollection.getTargetCoordinatesFromGroundUnits(squadron.determineEnemySide());
		
		// Move the infantry a little bit away from the target
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
        targetCoordinates = moveTargetAwayFromEnemy(startCoords.copy(), targetCoordinates.copy());

		// Now the actual artillery spot mission
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());            
        FlightInformation flightInformation = createFlightInformation(targetCoordinates);
		SpyExtractFlight spyFlight = new SpyExtractFlight (flightInformation, missionBeginUnit);
		spyFlight.linkGroundUnitsToFlight(groundUnitCollection);
		
		spyFlight.createUnitMission();
		
		return spyFlight;
	}

	private Coordinate moveTargetAwayFromEnemy(Coordinate referenceCoord, Coordinate targetCoord) throws PWCGException 
	{
		double angle = MathUtils.calcAngle(referenceCoord, targetCoord);
		double distance = 1000.0 + RandomNumberGenerator.getRandom(5000);
		Coordinate newTargetCoord = MathUtils.calcNextCoord(targetCoord, angle, distance);

		return newTargetCoord;
	}
}
