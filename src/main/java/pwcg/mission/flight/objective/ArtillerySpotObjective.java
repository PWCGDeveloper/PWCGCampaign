package pwcg.mission.flight.objective;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;

public class ArtillerySpotObjective
{
    static String getMissionObjective(IFlight flight) throws PWCGException 
    {
        String objective = "";

        String objectiveName =  MissionObjectiveLocation.formMissionObjectiveLocation(flight.getCampaign(), flight.getTargetDefinition().getPosition().copy());
        if (!objectiveName.isEmpty())
        {
            objective = "Spot for friendlya artillery near " + objectiveName + 
                            ".  Direct fire onto enemy positions.";
        }
        else
        {
            objective = "Spot for friendlya artillery.  Direct fire onto enemy positions.";                
        }
        
        return objective;
    }

}
