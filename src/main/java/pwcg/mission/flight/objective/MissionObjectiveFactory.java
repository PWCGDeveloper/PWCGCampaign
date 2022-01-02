package pwcg.mission.flight.objective;

import java.util.Date;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.offensive.OffensiveFlight;

public class MissionObjectiveFactory
{
    public static String formMissionObjective(IFlight flight, Date date) throws PWCGException
    {
        if (flight.getFlightType() == FlightTypes.CARGO_DROP)
        {
            return getCargoDropMissionObjective(flight);
        }
        else if (flight.getFlightType() == FlightTypes.BOMB ||
                 flight.getFlightType() == FlightTypes.GROUND_ATTACK ||
                 flight.getFlightType() == FlightTypes.RAID ||
                 flight.getFlightType() == FlightTypes.DIVE_BOMB ||
                 flight.getFlightType() == FlightTypes.LOW_ALT_BOMB)
        {
            return GroundAttackObjective.getMissionObjective(flight);
        }
        else if (flight.getFlightType() == FlightTypes.GROUND_HUNT)
        {
            return GroundFreeHuntObjective.getMissionObjective(flight);
        }
        else if (flight.getFlightType() == FlightTypes.INTERCEPT)
        {
            return getInterceptMissionObjective(flight);
        }
        else if (flight.getFlightType() == FlightTypes.OFFENSIVE)
        {
            return OffensivePatrolObjective.getMissionObjective((OffensiveFlight) flight);
        }
        else if (flight.getFlightType() == FlightTypes.PARATROOP_DROP)
        {
            return getParatroopDropMissionObjective(flight);
        }
        else if (flight.getFlightType() == FlightTypes.PATROL ||
                flight.getFlightType() == FlightTypes.LOW_ALT_PATROL)
        {
            return getPatrolMissionObjective(flight);
        }
        else if (flight.getFlightType() == FlightTypes.LOW_ALT_CAP)
        {
            return getCAPMissionObjective(flight);
        }

        return "Do something useful for God and Country!";
    }

    private static String getInterceptMissionObjective(IFlight flight) throws PWCGException 
    {
        String objective = "Intercept enemy aircraft" + MissionObjectiveLocation.formMissionObjectiveLocation(flight.getTargetDefinition().getPosition().copy()) + ".";      
        
        return objective;
    }
    
    private static String getCargoDropMissionObjective(IFlight flight) throws PWCGException
    {
        String objective = "Perform a cargo drop" + MissionObjectiveLocation.formMissionObjectiveLocation(flight.getTargetDefinition().getPosition().copy()) + ".";     
        return objective;
    }
    
    private static String getParatroopDropMissionObjective(IFlight flight) throws PWCGException
    {
        String objective = "Drop our paratroops" + MissionObjectiveLocation.formMissionObjectiveLocation(flight.getTargetDefinition().getPosition().copy()) + ".";       
        return objective;
    }

    private static String getPatrolMissionObjective(IFlight flight) throws PWCGException
    {
        String objective = "Patrol aircpace at the specified front location.  " + 
                "Engage any enemy aircraft that you encounter.  ";
        String objectiveName =  MissionObjectiveLocation.formMissionObjectiveLocation(flight.getTargetDefinition().getPosition().copy()) + "."; 
        if (!objectiveName.isEmpty())
        {
            objective = "Patrol airspace " + objectiveName + 
                    "  Engage any enemy aircraft that you encounter."; 
        }
        
        return objective;
    }

    private static String getCAPMissionObjective(IFlight flight) throws PWCGException
    {
        String objective = "Close Air Patrol over our troops.  " + 
                "Engage any enemy aircraft that you encounter as a first priority.  " +
                "Attack targets of opportunity on the ground.  ";
        String objectiveName =  MissionObjectiveLocation.formMissionObjectiveLocation(flight.getTargetDefinition().getPosition().copy()) + "."; 
        if (!objectiveName.isEmpty())
        {
            objective = "Close Air Patrol over our troops " + objectiveName +
                    "  Engage any enemy aircraft that you encounter as a first priority." +
                    "  Attack targets of opportunity on the ground.  ";
        }
        
        return objective;
    }
}
