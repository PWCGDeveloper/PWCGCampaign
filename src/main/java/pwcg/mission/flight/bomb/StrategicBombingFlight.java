package pwcg.mission.flight.bomb;

import pwcg.campaign.target.TacticalTarget;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.bomb.BombingWaypoints.BombingAltitudeLevel;

public class StrategicBombingFlight extends BombingFlight
{
    public StrategicBombingFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
    {
        super (flightInformation, missionBeginUnit);
        bombingAltitudeLevel = BombingAltitudeLevel.HIGH;
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
