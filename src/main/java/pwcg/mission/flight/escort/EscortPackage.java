package pwcg.mission.flight.escort;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightPackage;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.GroundUnitCollection;

public class EscortPackage extends FlightPackage
{
    private Squadron friendlyBombSquadron;
    
    public EscortPackage(Mission mission, Campaign campaign, Squadron squadron, boolean isPlayerFlight)
    {
        super(mission, campaign, squadron, isPlayerFlight);
        this.flightType = FlightTypes.ESCORT;
    }

    public Flight createPackage () throws PWCGException 
    {
	    if(!isPlayerFlight)
        {
	        throw new PWCGMissionGenerationException ("Attempt to create non player escort package");
        }
	    getSquadronToBeEscorted();
	    
        GroundUnitCollection enemyGroundUnits = createTargetForPlayerEscortedFlight();
        
        PlayerEscortedFlightBuilder playerEscortedFlightBuilder = new PlayerEscortedFlightBuilder(campaign, mission, squadron, friendlyBombSquadron);
        
        Coordinate targetCoordinates = enemyGroundUnits.getTargetCoordinatesFromGroundUnits(squadron.determineEnemySide());
        Flight flightEscortedByPlayer = playerEscortedFlightBuilder.createEscortedFlight(targetCoordinates);
        flightEscortedByPlayer.linkGroundUnitsToFlight(enemyGroundUnits);

		PlayerEscortFlight playerEscortFlight = createPlayerEscortMission(flightEscortedByPlayer);
		
		return playerEscortFlight;
	}

    private Squadron getSquadronToBeEscorted() throws PWCGException
    {
        friendlyBombSquadron = PWCGContextManager.getInstance().getSquadronManager().getSquadronByProximityAndRoleAndSide(
                        campaign, 
                        squadron.determineCurrentPosition(campaign.getDate()), 
                        Role.ROLE_BOMB, 
                        squadron.determineSquadronCountry(campaign.getDate()).getSide());
        
        if (friendlyBombSquadron == null)
        {
            throw new PWCGMissionGenerationException ("Escort mission with no viable squadrons to be escorted - please create another mission");
        }
        return friendlyBombSquadron;
    }

	private GroundUnitCollection createTargetForPlayerEscortedFlight() throws PWCGException
	{
	    GroundUnitCollection enemyGroundUnits = createGroundUnitsForFlight();
		return enemyGroundUnits;
	}

	private PlayerEscortFlight createPlayerEscortMission(Flight flightEscortedByPlayer) throws PWCGException
	{
		MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        Coordinate playerSquadronPosition = squadron.determineCurrentPosition(campaign.getDate());
        missionBeginUnit.initialize(playerSquadronPosition.copy());

        FlightInformation flightInformation = createFlightInformation(flightEscortedByPlayer.getPlanes().get(0).getPosition().copy());
		PlayerEscortFlight playerEscort = new PlayerEscortFlight(flightInformation, missionBeginUnit, flightEscortedByPlayer);
		playerEscort.addLinkedUnit(flightEscortedByPlayer);
		playerEscort.createUnitMission();
		return playerEscort;
	}
}
