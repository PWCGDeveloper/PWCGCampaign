package pwcg.mission.flight.objective;

import pwcg.core.exception.PWCGException;
import pwcg.mission.IUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.TacticalTarget;

public class ArtillerySpotObjective
{
    static String getMissionObjective(Flight flight) throws PWCGException 
    {
        FlightInformation flightInformation = flight.getFlightInformation();

        String objective = "Correct artillery fire at the specified objective.";
        
        for (IUnit linkedUnit : flight.getLinkedUnits())
        {
            String objectiveLocation =  MissionObjective.getMissionObjectiveLocation(flightInformation.getSquadron(), flightInformation.getCampaign().getDate(), linkedUnit);
            
            if (linkedUnit instanceof IGroundUnitCollection)
            {
                IGroundUnitCollection groundUnit = (IGroundUnitCollection)linkedUnit;                             
                TacticalTarget targetType = groundUnit.getTargetType();
                if (targetType == TacticalTarget.TARGET_ASSAULT)
                {
                    objective = "Spot artillery against assaulting enemy troops " + objectiveLocation; 
                    break;
                }
                else if (targetType == TacticalTarget.TARGET_DEFENSE)
                {
                    objective = "Spot artillery against defending enemy troops " + objectiveLocation; 
                    break;
                }
                else if (targetType == TacticalTarget.TARGET_ARTILLERY)
                {
                    objective = "Spot artillery against the artillery battery " + objectiveLocation; 
                    // Artillery might be overridden by something else
                }
                else if (targetType == TacticalTarget.TARGET_TRANSPORT)
                {
                    objective = "Spot artillery against the transport and road facilities " + objectiveLocation; 
                    break;
                }
                else if (targetType == TacticalTarget.TARGET_DRIFTER)
                {
                    objective = "Spot artillery against the light shipping " + objectiveLocation; 
                    break;
                }
                else if (targetType == TacticalTarget.TARGET_TROOP_CONCENTRATION)
                {
                    objective = "Spot artillery against the troop concentrations " + objectiveLocation; 
                    // Troop concentration might be overridden by something else
                }
            }
        }
        
        return objective;
    }

}
