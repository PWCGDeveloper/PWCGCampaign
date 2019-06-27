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

        if (flightInformation.getTargetDefinition().getTargetType() == TacticalTarget.TARGET_FACTORY)
        {
            objective = "Bomb the factories near " + flightInformation.getTargetDefinition().getTargetName();
        }
        else if (flightInformation.getTargetDefinition().getTargetType() == TacticalTarget.TARGET_CITY)
        {
            objective = "Bomb available targets at " + flightInformation.getTargetDefinition().getTargetName();
        }
        else if (flightInformation.getTargetDefinition().getTargetType() == TacticalTarget.TARGET_RAIL)
        {
            objective = "Bomb the rail station at " + flightInformation.getTargetDefinition().getTargetName();
        }
        else if (flightInformation.getTargetDefinition().getTargetType() == TacticalTarget.TARGET_AIRFIELD)
        {
            objective = "Bomb the airfield at " + flightInformation.getTargetDefinition().getTargetName();
        }
        else if (flightInformation.getTargetDefinition().getTargetType() == TacticalTarget.TARGET_PORT)
        {
            objective = "Bomb the port facilities at " + flightInformation.getTargetDefinition().getTargetName();
        }
        else if (flightInformation.getTargetDefinition().getTargetType() == TacticalTarget.TARGET_SHIPPING)
        {
            objective = "Bomb shipping that has been detected.  Expected shipping types include: " + flightInformation.getTargetDefinition().getTargetName() + ".";
        }

        return objective;
    }
}
