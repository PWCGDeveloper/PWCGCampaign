package pwcg.mission.flight.scramble;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightPackage;
import pwcg.mission.flight.FlightTypes;

public class ScramblePackage extends FlightPackage
{
    public ScramblePackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = FlightTypes.SCRAMBLE;
    }

    public Flight createPackage () throws PWCGException 
	{        		
		// Get the target waypoint. 
        Coordinate startCoords = squadron.determineCurrentPosition(campaign.getDate());
        
		Coordinate targetWaypoint = getTargetWaypoint(mission, startCoords);
		
		// Now the actual mission
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(startCoords.copy());        
        FlightInformation flightInformation = createFlightInformation(targetWaypoint);
		PlayerScrambleFlight scramble = new PlayerScrambleFlight (flightInformation, missionBeginUnit);
		scramble.createUnitMission();

		// Only fighters for now
		List<Role> acceptableRoles = new ArrayList<Role>();
		acceptableRoles.add(Role.ROLE_FIGHTER);
		
		// Generate an opposing fighter squadron
		List<Squadron> opposingSquads = null;
		Side enemySide = squadron.determineSquadronCountry(campaign.getDate()).getSide().getOppositeSide();
        opposingSquads =  PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsByRole(campaign, targetWaypoint, 1, 50000.0, acceptableRoles, enemySide, campaign.getDate());
		
		if (opposingSquads != null && opposingSquads.size() != 0)
		{
			int index= RandomNumberGenerator.getRandom(opposingSquads.size());
			Squadron opposingSquad = opposingSquads.get(index);

			// And the opposing mission
			// Target is the start of the opposing flight while the player field 
			// is the target for the opposing flight.
            Coordinate opposingTargetCoords = squadron.determineCurrentPosition(campaign.getDate());
            Coordinate opposingStartCoords = targetWaypoint.copy();

            // Move closer to the player
            double distanceToTargetWP = MathUtils.calcDist(opposingStartCoords, opposingTargetCoords);
            double angleToTargetWP = MathUtils.calcAngle(opposingStartCoords, opposingTargetCoords);
            opposingStartCoords = MathUtils.calcNextCoord(opposingStartCoords, angleToTargetWP, (distanceToTargetWP/2));
	        MissionBeginUnit missionBeginUnitOpposing = new MissionBeginUnit(opposingTargetCoords.copy());        

	        FlightInformation opposingFlightInformation = FlightInformationFactory.buildAiFlightInformation(opposingSquad, mission, FlightTypes.SCRAMBLE_OPPOSE, opposingTargetCoords);
			ScrambleOpposingFlight scrambleOpposing = new ScrambleOpposingFlight (opposingFlightInformation, missionBeginUnitOpposing, opposingStartCoords);
			scrambleOpposing.createUnitMission();
			
			// Link the opposing flight to the scramble
			scramble.addLinkedUnit(scrambleOpposing);
		}
		
		
		return scramble;
	}
	
	
	/**
	 * Create mission
	 * @throws PWCGException 
	 * 
	 * @
	 */
	protected Coordinate getTargetWaypoint(Mission mission, Coordinate targetPosition) throws PWCGException 
	{ 
		double InitialWaypointDistance = 5000.0;
		double angle = RandomNumberGenerator.getRandom(360);

		Coordinate targetCoords = MathUtils.calcNextCoord(targetPosition, angle, InitialWaypointDistance);


		return targetCoords;
	}

}
