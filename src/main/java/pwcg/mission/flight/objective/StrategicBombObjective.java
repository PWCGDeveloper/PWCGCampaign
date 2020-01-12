package pwcg.mission.flight.objective;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.target.TargetType;

public class StrategicBombObjective
{
    static String getMissionObjective(IFlight flight) throws PWCGException 
    {
        IFlightInformation flightInformation = flight.getFlightData().getFlightInformation();
        String objective = "Bomb the specified objective.  ";

        if (flightInformation.getTargetDefinition().getTargetType() == TargetType.TARGET_FACTORY)
        {
            objective = "Bomb the factories near " + flightInformation.getTargetDefinition().getTargetName();
        }
        else if (flightInformation.getTargetDefinition().getTargetType() == TargetType.TARGET_CITY)
        {
            objective = "Bomb available targets at " + flightInformation.getTargetDefinition().getTargetName();
        }
        else if (flightInformation.getTargetDefinition().getTargetType() == TargetType.TARGET_RAIL)
        {
            objective = "Bomb the rail station at " + flightInformation.getTargetDefinition().getTargetName();
        }
        else if (flightInformation.getTargetDefinition().getTargetType() == TargetType.TARGET_AIRFIELD)
        {
            objective = "Bomb the airfield at " + flightInformation.getTargetDefinition().getTargetName();
        }
        else if (flightInformation.getTargetDefinition().getTargetType() == TargetType.TARGET_PORT)
        {
            objective = "Bomb the port facilities at " + flightInformation.getTargetDefinition().getTargetName();
        }
        else if (flightInformation.getTargetDefinition().getTargetType() == TargetType.TARGET_SHIPPING)
        {
            objective = "Bomb shipping that has been detected.  Expected shipping types include: " + flightInformation.getTargetDefinition().getTargetName() + ".";
        }

        return objective;
    }

}
