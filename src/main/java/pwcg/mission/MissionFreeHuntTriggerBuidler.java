package pwcg.mission;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.target.TargetType;

public class MissionFreeHuntTriggerBuidler
{
    private Mission mission;
    
    public MissionFreeHuntTriggerBuidler(Mission mission)
    {
        this.mission = mission;
    }

    public void buildFreeHuntTriggers() throws PWCGException
    {
        for (IFlight playerFlight : mission.getMissionFlights().getPlayerFlights())
        {
            if (playerFlight.getFlightInformation().getFlightType() == FlightTypes.GROUND_HUNT)
            {
                addTargetSequenceToGroundUnits(playerFlight);
            }
        }
    }

    private void addBingoOrdnanceSequence(IFlight playerFlight) throws PWCGException
    {
        playerFlight.buildBingoOrdnanceSequence();
    }

    private void addTargetSequenceToGroundUnits(IFlight playerFlight) throws PWCGException
    {
        Side enemySide = playerFlight.getSquadron().determineEnemySide();
        TargetType targetType = playerFlight.getTargetDefinition().getTargetType();
        for (GroundUnitCollection groundUnitCollection : mission.getMissionGroundUnitBuilder().getGroundUnitsForSide(enemySide))
        {
            groundUnitCollection.addFreeHuntTargetingFlight(playerFlight, targetType);
        }
    }

}
