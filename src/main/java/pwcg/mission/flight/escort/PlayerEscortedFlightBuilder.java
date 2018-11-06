package pwcg.mission.flight.escort;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
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
		MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(friendlyBombSquadron.determineCurrentPosition(campaign.getDate()));

        FlightInformation escortedFlightInformation = FlightInformationFactory.buildAiFlightInformation(friendlyBombSquadron, mission, FlightTypes.BOMB, enemyGroundUnitCoordinates.copy());
        escortedFlightInformation.setEscortedByPlayerFlight(true);
        bombingFlightEscortedByPlayer = new BombingFlight (escortedFlightInformation, missionBeginUnit);
		bombingFlightEscortedByPlayer.createUnitMission();
        moveEscortedFlightCloseToPlayer(enemyGroundUnitCoordinates);
        return bombingFlightEscortedByPlayer;
	}

	private void moveEscortedFlightCloseToPlayer(Coordinate targetCoordinates) throws PWCGException
	{
        Coordinate playerSquadronPosition = playerSquadron.determineCurrentPosition(campaign.getDate());

        FrontLinesForMap frontMap = PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        Coordinate closestFrontToTarget = frontMap.findClosestFrontCoordinateForSide(targetCoordinates, playerSquadron.determineSide());
                        
        double angleToTarget = MathUtils.calcAngle(closestFrontToTarget, playerSquadronPosition);
		Coordinate rendevousCoords = MathUtils.calcNextCoord(closestFrontToTarget, angleToTarget, 10000.0);
				
        Coordinate firstDestinationCoordinate = bombingFlightEscortedByPlayer.findIngressWaypointPosition();
        rendevousCoords.setYPos(firstDestinationCoordinate.getYPos());
        
        bombingFlightEscortedByPlayer.resetFlightForPlayerEscort(rendevousCoords, targetCoordinates);
	}

}
