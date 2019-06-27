package pwcg.mission.flight.escort;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.unit.TargetBuilder;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.bomb.BombingFlight;
import pwcg.mission.ground.GroundUnitCollection;

public class PlayerEscortedFlightBuilder
{
    private FlightInformation escortFlightInformation;
    private FlightInformation escortedFlightInformation;
	
	public PlayerEscortedFlightBuilder () throws PWCGException 
	{
	}
	
	public Flight createEscortedFlight(PlayerEscortFlight escortFlight) throws PWCGException
    {
        this.escortFlightInformation = escortFlight.getFlightInformation();
        MissionBeginUnit missionBeginUnit = buildEscortedFlightInformation(escortFlight);
        BombingFlight bombingFlightEscortedByPlayer = buildEscortedFlight(missionBeginUnit);        
        return bombingFlightEscortedByPlayer;
	}

    private BombingFlight buildEscortedFlight(MissionBeginUnit missionBeginUnit) throws PWCGException
    {
        BombingFlight bombingFlightEscortedByPlayer = new BombingFlight (escortedFlightInformation, missionBeginUnit);
		bombingFlightEscortedByPlayer.createUnitMission();
		
        GroundUnitCollection targetUnit = createTargetForPlayerEscortedFlight();
        bombingFlightEscortedByPlayer.linkGroundUnitsToFlight(targetUnit);
        bombingFlightEscortedByPlayer.resetFlightForPlayerEscort(escortFlightInformation.getTargetCoords().copy(), escortedFlightInformation.getTargetCoords());
        
        return bombingFlightEscortedByPlayer;
    }

    private MissionBeginUnit buildEscortedFlightInformation(PlayerEscortFlight escortFlight) throws PWCGException
    {
        Squadron friendlyBomberSquadron = determineSquadronToBeEscorted();
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(friendlyBomberSquadron.determineCurrentPosition(escortFlightInformation.getCampaign().getDate()));     
        
        this.escortedFlightInformation = FlightInformationFactory.buildEscortedByPlayerFlightInformation(
                escortFlight.getFlightInformation(), friendlyBomberSquadron);
        return missionBeginUnit;
    }

    private Squadron determineSquadronToBeEscorted() throws PWCGException
    {
        Squadron friendlyBombSquadron = PWCGContextManager.getInstance().getSquadronManager().getSquadronByProximityAndRoleAndSide(
                escortFlightInformation.getCampaign(), 
                escortFlightInformation.getSquadron().determineCurrentPosition(escortFlightInformation.getCampaign().getDate()), 
                Role.ROLE_BOMB, 
                escortFlightInformation.getSquadron().determineSquadronCountry(escortFlightInformation.getCampaign().getDate()).getSide());
        
        if (friendlyBombSquadron == null)
        {
            throw new PWCGMissionGenerationException ("Escort mission with no viable squadrons to be escorted - please create another mission");
        }
        return friendlyBombSquadron;
    }

    private GroundUnitCollection createTargetForPlayerEscortedFlight() throws PWCGException
    {
        // TODO this will not use the precise target info from the previously created ground units ... build the ground units after the bombing flight?
        TargetBuilder targetBuilder = new TargetBuilder(escortedFlightInformation);
        targetBuilder.buildTarget();
        return targetBuilder.getGroundUnits();
    }

}
