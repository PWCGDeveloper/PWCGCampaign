package pwcg.mission;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.groundhunt.GroundFreeHuntFlight;
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
            if (playerFlight instanceof GroundFreeHuntFlight)
            {
                addTargetSequenceToGroundUnits(playerFlight);
            }
        }
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
                    groundUnit.addTargetingFlight(playerFlight);
                }
            }
        }
    }

}
