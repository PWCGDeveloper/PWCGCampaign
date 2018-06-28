package pwcg.mission.flight.escort;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.bomb.BombingFlight;

public class PlayerEscortedFlightBuilder
{
	private Campaign campaign;
	private Mission mission;
    private Squadron playerSquadron;
    private Squadron friendlyBombSquadron;
	private BombingFlight bombingFlightEscortedByPlayer;
	
	public PlayerEscortedFlightBuilder (Campaign campaign, Mission mission, Squadron playerSquadron, Squadron friendlyBombSquadron) throws PWCGException 
	{
		this.campaign = campaign;
		this.mission = mission;
        this.playerSquadron = playerSquadron;
        this.friendlyBombSquadron = friendlyBombSquadron;
	}
	
	public Flight createEscortedFlight (Coordinate enemyGroundUnitCoordinates) throws PWCGException 
	{
        bombingFlightEscortedByPlayer = new BombingFlight ();
        bombingFlightEscortedByPlayer.setVirtual(false);

        createFlightToEscort(enemyGroundUnitCoordinates);			
		moveEscortedFlightCloseToPlayer(enemyGroundUnitCoordinates);

		return bombingFlightEscortedByPlayer;
	}

	private void createFlightToEscort(Coordinate enemyGroundUnitCoordinates) throws PWCGException
	{
		Coordinate playerSquadronPosition = playerSquadron.determineCurrentPosition(campaign.getDate());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(playerSquadronPosition);

        bombingFlightEscortedByPlayer.initialize(mission, campaign, FlightTypes.BOMB, enemyGroundUnitCoordinates, friendlyBombSquadron, missionBeginUnit, false);
        
        String friendlyBombSquadronFieldName = friendlyBombSquadron.determineCurrentAirfieldName(campaign.getDate());
        IAirfield friendlyBombSquadronField =  PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirfield(friendlyBombSquadronFieldName);

		bombingFlightEscortedByPlayer.setSquad(friendlyBombSquadron);
		bombingFlightEscortedByPlayer.setAirfield(friendlyBombSquadronField);
		bombingFlightEscortedByPlayer.setPlayerFlight(false);
		bombingFlightEscortedByPlayer.setEscortedByPlayerFlight(true);

		bombingFlightEscortedByPlayer.createUnitMission();
	}

	private void moveEscortedFlightCloseToPlayer(Coordinate targetCoordinates) throws PWCGException
	{
        Coordinate playerSquadronPosition = playerSquadron.determineCurrentPosition(campaign.getDate());

        double angleToTarget = MathUtils.calcAngle(playerSquadronPosition, targetCoordinates);
		Coordinate rendevousCoords = MathUtils.calcNextCoord(playerSquadronPosition, angleToTarget, 20000.0);
				
        Coordinate firstDestinationCoordinate = bombingFlightEscortedByPlayer.findIngressWaypointPosition();
        rendevousCoords.setYPos(firstDestinationCoordinate.getYPos());
        
        bombingFlightEscortedByPlayer.resetFlightForPlayerEscort(rendevousCoords, targetCoordinates);
	}

}
