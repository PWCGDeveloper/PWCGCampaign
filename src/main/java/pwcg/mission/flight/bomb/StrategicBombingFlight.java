package pwcg.mission.flight.bomb;

import pwcg.campaign.target.TacticalTarget;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.bomb.BombingWaypoints.BombingAltitudeLevel;

public class StrategicBombingFlight extends BombingFlight
{
    public StrategicBombingFlight()
    {
        super();
        bombingAltitudeLevel = BombingAltitudeLevel.HIGH;
    }

    @Override
    public int calcNumPlanes()
    {
        int numPlanes = 1;

        if (!squadron.determineIsNightSquadron())
        {
            numPlanes = 3 + RandomNumberGenerator.getRandom(2);
        }

        return modifyNumPlanes(numPlanes);
    }

    public String getMissionObjective()
    {
        String objective = "Bomb the specified objective.  ";

        if (targetDefinition.getTargetType() == TacticalTarget.TARGET_FACTORY)
        {
            objective = "Bomb the factories near " + targetDefinition.getTargetName();
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_CITY)
        {
            objective = "Bomb available targets at " + targetDefinition.getTargetName();
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_RAIL)
        {
            objective = "Bomb the rail station at " + targetDefinition.getTargetName();
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_AIRFIELD)
        {
            objective = "Bomb the airfield at " + targetDefinition.getTargetName();
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_PORT)
        {
            objective = "Bomb the port facilities at " + targetDefinition.getTargetName();
        }
        else if (targetDefinition.getTargetType() == TacticalTarget.TARGET_SHIPPING)
        {
            objective = "Bomb shipping that has been detected.  Expected shipping types include: " + targetDefinition.getTargetName() + ".";
        }

        return objective;
    }
}
