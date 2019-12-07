package pwcg.mission.flight.objective;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.target.TacticalTarget;

public class StrategicBombObjective
{
    static String getMissionObjective(Flight flight) throws PWCGException 
    {
        FlightInformation flightInformation = flight.getFlightInformation();
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
