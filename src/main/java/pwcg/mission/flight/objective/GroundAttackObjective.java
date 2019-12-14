package pwcg.mission.flight.objective;

import pwcg.core.exception.PWCGException;
import pwcg.mission.IUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.unittypes.staticunits.AirfieldTargetGroup;
import pwcg.mission.target.TacticalTarget;

public class GroundAttackObjective
{
    static String getMissionObjective(Flight flight) throws PWCGException 
    {
        FlightInformation flightInformation = flight.getFlightInformation();
        
        String objective = "Attack the specified objective using all available means.";
        for (IUnit linkedUnit : flight.getLinkedUnits())
        {
            if (linkedUnit instanceof IGroundUnitCollection)
            {
                IGroundUnitCollection groundUnitCollection = (IGroundUnitCollection)linkedUnit;
                for (IGroundUnit groundUnit : groundUnitCollection.getGroundUnits())
                {
                    if (!groundUnit.getCountry().isSameSide(flight.getCountry()))
                    {
                        objective = getObjectiveFromEnemyUnit(groundUnit, flightInformation);
                        break;
                    }
                }
            }
        }
        
        return objective;
    }

    private static String getObjectiveFromEnemyUnit(IGroundUnit enemyGroundUnit, FlightInformation flightInformation) throws PWCGException
    {
        String objectiveLocation =  MissionObjective.getMissionObjectiveLocation(
                flightInformation.getSquadron(), flightInformation.getCampaign().getDate(), enemyGroundUnit);
        
        String objective = "Attack the specified objective using all available means.";
        TacticalTarget targetType = enemyGroundUnit.getTargetType();
        
        
        if (targetType == TacticalTarget.TARGET_ASSAULT)
        {
            objective = "Attack assaulting enemy troops" + objectiveLocation; 
        }
        else if (targetType == TacticalTarget.TARGET_DEFENSE)
        {
            objective = "Attack defending enemy troops" + objectiveLocation; 
        }
        else if (targetType == TacticalTarget.TARGET_INFANTRY)
        {
            objective = "Attack enemy troops" + objectiveLocation; 
        }
        else if (targetType == TacticalTarget.TARGET_AIRFIELD)
        {
            AirfieldTargetGroup target = (AirfieldTargetGroup)enemyGroundUnit;
            objective = "Attack the airfield at " + target.getAirfield().getName();
        }
        else if (targetType == TacticalTarget.TARGET_TRAIN)
        {
            objective = "Attack the trains and rail facilities" + objectiveLocation;
        }
        else if (targetType == TacticalTarget.TARGET_ARTILLERY)
        {
            objective = "Attack the artillery battery" + objectiveLocation; 
        }
        else if (targetType == TacticalTarget.TARGET_TRANSPORT)
        {
            objective = "Attack the transport and road facilities" + objectiveLocation; 
        }
        else if (targetType == TacticalTarget.TARGET_SHIPPING)
        {
            objective = "Attack the shipping" + objectiveLocation; 
        }
        else if (targetType == TacticalTarget.TARGET_BALLOON)
        {
            objective = "Attack the balloons" + objectiveLocation; 
        }
        else if (targetType == TacticalTarget.TARGET_DRIFTER)
        {
            objective = "Attack the light shipping" + objectiveLocation; 
        }
        else if (targetType == TacticalTarget.TARGET_AAA)
        {
            objective = "Attack AAA" + objectiveLocation; 
        }
        else
        {
            objective = "Target" + objectiveLocation; 
        }
        return objective;

    }
}
