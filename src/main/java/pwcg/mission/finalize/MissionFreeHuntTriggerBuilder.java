package pwcg.mission.finalize;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.target.TargetType;

public class MissionFreeHuntTriggerBuilder
{
    private Mission mission;
    
    public MissionFreeHuntTriggerBuilder(Mission mission)
    {
        this.mission = mission;
    }

    public void buildFreeHuntTriggers() throws PWCGException
    {
        for (IFlight playerFlight : mission.getFlights().getPlayerFlights())
        {
            if (playerFlight.getFlightInformation().getFlightType() == FlightTypes.GROUND_HUNT)
            {
                addTargetSequenceToGroundUnits(playerFlight);
            }
        }
    }

    private void addTargetSequenceToGroundUnits(IFlight playerFlight) throws PWCGException
    {
        Side enemySide = playerFlight.getSquadron().determineEnemySide();
        TargetType targetType = playerFlight.getTargetDefinition().getTargetType();
        for (GroundUnitCollection groundUnitCollection : mission.getGroundUnitBuilder().getGroundUnitsForSide(enemySide))
        {
            groundUnitCollection.addFreeHuntTargetingFlight(playerFlight, targetType);
        }
    }

}
