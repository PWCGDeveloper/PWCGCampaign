package pwcg.mission.flight.escort;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.unit.TargetBuilder;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.ground.GroundUnitCollection;

public class EscortPackage implements IFlightPackage
{
    private FlightInformation flightInformation;    
    public EscortPackage(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public Flight createPackage () throws PWCGException 
    {
	    if(!flightInformation.isPlayerFlight())
        {
	        throw new PWCGMissionGenerationException ("Attempt to create non player escort package");
        }
	    	    
        GroundUnitCollection enemyGroundUnits = createTargetForPlayerEscortedFlight();

        Squadron friendlyBombSquadron = getSquadronToBeEscorted();
        PlayerEscortedFlightBuilder playerEscortedFlightBuilder = new PlayerEscortedFlightBuilder(flightInformation, friendlyBombSquadron);
        
        Coordinate targetCoordinates = enemyGroundUnits.getTargetCoordinatesFromGroundUnits(flightInformation.getSquadron().determineEnemySide());
        Flight flightEscortedByPlayer = playerEscortedFlightBuilder.createEscortedFlight(targetCoordinates);
        flightEscortedByPlayer.linkGroundUnitsToFlight(enemyGroundUnits);

		PlayerEscortFlight playerEscortFlight = createPlayerEscortMission(flightEscortedByPlayer);
		return playerEscortFlight;
	}

    private Squadron getSquadronToBeEscorted() throws PWCGException
    {
        Squadron friendlyBombSquadron = PWCGContextManager.getInstance().getSquadronManager().getSquadronByProximityAndRoleAndSide(
                flightInformation.getCampaign(), 
                flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate()), 
                Role.ROLE_BOMB, 
                flightInformation.getSquadron().determineSquadronCountry(flightInformation.getCampaign().getDate()).getSide());
        
        if (friendlyBombSquadron == null)
        {
            throw new PWCGMissionGenerationException ("Escort mission with no viable squadrons to be escorted - please create another mission");
        }
        return friendlyBombSquadron;
    }

	private PlayerEscortFlight createPlayerEscortMission(Flight flightEscortedByPlayer) throws PWCGException
	{
        Coordinate squadronPosition = flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
	    MissionBeginUnit missionBeginUnit = new MissionBeginUnit(squadronPosition.copy());	        
		PlayerEscortFlight playerEscort = new PlayerEscortFlight(flightInformation, missionBeginUnit, flightEscortedByPlayer);
		playerEscort.addLinkedUnit(flightEscortedByPlayer);
		playerEscort.createUnitMission();
		return playerEscort;
	}

    private GroundUnitCollection createTargetForPlayerEscortedFlight() throws PWCGException
    {
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }
}
