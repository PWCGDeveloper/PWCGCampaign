package pwcg.mission;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.IGroundUnit;
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

    public void addBingoOrdnanceSequence(IFlight playerFlight) throws PWCGException
    {
        playerFlight.buildBingoOrdnanceSequence();
    }

    private void addTargetSequenceToGroundUnits(IFlight playerFlight) throws PWCGException
    {
        TargetType targetType = playerFlight.getTargetDefinition().getTargetType();
        for (GroundUnitCollection groundUnitCollection : mission.getMissionGroundUnitBuilder().getAllMissionGroundUnits())
        {
            for (IGroundUnit groundUnit : groundUnitCollection.getGroundUnits())
            {
                if (groundUnit.getTargetType() == targetType)
                {
                    groundUnitCollection.setCheckZoneTriggerDistance(150000);
                    groundUnit.addFreeHuntTargetingFlight(playerFlight);
                }
            }
        }
    }

}
