package pwcg.mission.flight.objective;

import pwcg.core.exception.PWCGException;
import pwcg.mission.IUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.unittypes.staticunits.AirfieldTargetGroup;
import pwcg.mission.ground.unittypes.transport.GroundTrainUnit;
import pwcg.mission.target.TacticalTarget;

public class GroundAttackObjective
{
    static String getMissionObjective(Flight flight) throws PWCGException 
    {
        FlightInformation flightInformation = flight.getFlightInformation();
        
        String objective = "Attack the specified objective using all available means.";
        for (IUnit linkedUnit : flight.getLinkedUnits())
        {
            String objectiveLocation =  MissionObjective.getMissionObjectiveLocation(flightInformation.getSquadron(), flightInformation.getCampaign().getDate(), linkedUnit);
            
            if (!linkedUnit.getCountry().isSameSide(flight.getCountry()))
            {
                if (linkedUnit instanceof AirfieldTargetGroup)
                {
                    AirfieldTargetGroup target = (AirfieldTargetGroup)linkedUnit;
                    objective = "Attack the airfield at " + target.getAirfield().getName();
                    break;
                }
                else if (linkedUnit instanceof GroundTrainUnit)
                {
                    objective = "Attack the trains and rail facilities " + objectiveLocation;
                    break;
                }
                else if (linkedUnit instanceof IGroundUnitCollection)
                {
                    IGroundUnitCollection groundUnit = (IGroundUnitCollection)linkedUnit;             

                    TacticalTarget targetType = groundUnit.getTargetType();
                    if (targetType == TacticalTarget.TARGET_INFANTRY)
                    {
                        objective = "Attack enemy troops " + objectiveLocation; 
                        break;
                    }
                    if (targetType == TacticalTarget.TARGET_ASSAULT)
                    {
                        objective = "Attack assaulting enemy troops " + objectiveLocation; 
                        break;
                    }
                    else if (targetType == TacticalTarget.TARGET_DEFENSE)
                    {
                        objective = "Attack defending enemy troops " + objectiveLocation; 
                        break;
                    }
                    else if (targetType == TacticalTarget.TARGET_ARTILLERY)
                    {
                        objective = "Attack the artillery battery " + objectiveLocation; 
                    }
                    else if (targetType == TacticalTarget.TARGET_TRANSPORT)
                    {
                        objective = "Attack the transport and road facilities " + objectiveLocation; 
                        break;
                    }
                    else if (targetType == TacticalTarget.TARGET_SHIPPING)
                    {
                        objective = "Attack the shipping " + objectiveLocation; 
                        break;
                    }
                    else if (targetType == TacticalTarget.TARGET_BALLOON)
                    {
                        objective = "Attack the balloons " + objectiveLocation; 
                        break;
                    }
                    else if (targetType == TacticalTarget.TARGET_DRIFTER)
                    {
                        objective = "Attack the light shipping " + objectiveLocation; 
                        break;
                    }
                    else if (targetType == TacticalTarget.TARGET_TROOP_CONCENTRATION)
                    {
                        objective = "Attack troop concentrations " + objectiveLocation; 
                    }
                }
            }
        }
        
        return objective;
    }

}
