package pwcg.mission.flight.objective;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.offensive.OffensiveFlight;
import pwcg.mission.flight.offensive.OffensiveFlight.OffensiveFlightTypes;

public class OffensivePatrolObjective
{
    static String getMissionObjective(OffensiveFlight flight) throws PWCGException 
    {
        String objective = "";

        String objectiveName =  MissionObjective.formMissionObjectiveLocation(flight.getFlightData().getFlightInformation().getTargetPosition().copy());
        if (flight.getOffensiveFlightType() == OffensiveFlightTypes.OFFENSIVE_FLIGHT_TRANSPORT)
        {
            if (!objectiveName.isEmpty())
            {
                objective = "Penetrate enemy airspace at the specified transport hubs" + objectiveName + 
                                ".  Engage any enemy aircraft that you encounter.  ";
            }
            else
            {
                objective = "Penetrate enemy airspace at the specified transport hubs" + 
                                ".  Engage any enemy aircraft that you encounter.  ";
            }
        }
        else if (flight.getOffensiveFlightType() == OffensiveFlightTypes.OFFENSIVE_FLIGHT_AIRFIELD)
        {
            if (!objectiveName.isEmpty())
            {
                objective = "Penetrate enemy airspace at the specified airfields" + objectiveName + 
                                ".  Engage any enemy aircraft that you encounter.  ";
            }
            else
            {
                objective = "Penetrate enemy airspace at the specified airfields" + 
                                ".  Engage any enemy aircraft that you encounter.  ";
            }
        }
        else
        {
            if (!objectiveName.isEmpty())
            {
                objective = "Penetrate enemy airspace" + objectiveName + 
                                ".  Engage any enemy aircraft that you encounter.  ";
            }
            else
            {
                objective = "Penetrate enemy airspace at the specified front location" + 
                                ".  Engage any enemy aircraft that you encounter.  ";
            }
        }
        
        return objective;
    }

}
