package pwcg.mission;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class MissionCheckZoneTriggerBuilder
{
    private Mission mission;
    
    public MissionCheckZoneTriggerBuilder(Mission mission)
    {
        this.mission = mission;
    }
    
    public void triggerGroundUnitsOnPlayerProximity() throws PWCGException
    {
        triggerPlayerTargetsOnAnyPlayer();
        triggerGroundUnitsOnAnyPlayer();
    }

    private void triggerPlayerTargetsOnAnyPlayer() throws PWCGException
    {
        for (IFlight playerFlight : mission.getMissionFlightBuilder().getPlayerFlights())
        {
            for (IGroundUnitCollection playerTarget : playerFlight.getLinkedGroundUnits().getLinkedGroundUnits())
            {
                playerTarget.triggerOnPlayerProximity(mission);
            }
        }
    }

    private void triggerGroundUnitsOnAnyPlayer() throws PWCGException
    {
        for (IGroundUnitCollection groundUnit: mission.getMissionGroundUnitBuilder().getAllMissionGroundUnits())
        {
            groundUnit.triggerOnPlayerProximity(mission);
        }
    }
}
