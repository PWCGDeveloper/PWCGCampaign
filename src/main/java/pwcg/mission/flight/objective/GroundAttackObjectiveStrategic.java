package pwcg.mission.flight.objective;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetType;

public class GroundAttackObjectiveStrategic
{
    static String getMissionObjective(IFlight flight) throws PWCGException 
    {
        String objectiveLocation =  MissionObjectiveLocation.getMissionObjectiveLocation(
                flight.getFlightInformation().getSquadron(), 
                flight.getFlightInformation().getCampaign().getDate(), 
                flight.getTargetDefinition().getPosition().copy());
        
        String objective = "Attack the specified objective using all available means.";
        TargetType targetType = flight.getTargetDefinition().getTargetType();
        
        if (targetType == TargetType.TARGET_AIRFIELD)
        {
            Airfield target = PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getClosestAirfield(flight.getTargetDefinition().getPosition().copy());
            objective = "Attack the airfield at " + target.getName();
        }
        else if (targetType == TargetType.TARGET_BRIDGE)
        {
            objective = "Attack the bridge" + objectiveLocation; 
        }
        else if (targetType == TargetType.TARGET_DEPOT)
        {
            objective = "Attack the depot" + objectiveLocation; 
        }
        else if (targetType == TargetType.TARGET_FACTORY)
        {
            objective = "Attack the factory" + objectiveLocation; 
        }
        else if (targetType == TargetType.TARGET_FUEL)
        {
            objective = "Attack the fuel depot" + objectiveLocation; 
        }
        else if (targetType == TargetType.TARGET_PORT)
        {
            objective = "Attack the port facilities" + objectiveLocation; 
        }
        else if (targetType == TargetType.TARGET_RAIL)
        {
            objective = "Attack the rail facilities" + objectiveLocation; 
        }
        else if (targetType == TargetType.TARGET_CITY)
        {
            objective = "Attack the population center" + objectiveLocation; 
        }

        return objective;

    }
}
