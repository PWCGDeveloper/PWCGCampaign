package pwcg.mission.flight.objective;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.unittypes.staticunits.AirfieldTargetGroup;
import pwcg.mission.target.TargetType;

public class GroundAttackObjective
{
    static String getMissionObjective(IFlight flight) throws PWCGException 
    {
        IFlightInformation flightInformation = flight.getFlightInformation();
        
        String objective = "Attack the specified objective using all available means.";
        for (IGroundUnitCollection linkedUnit : flight.getLinkedGroundUnits().getLinkedGroundUnits())
        {
            IGroundUnitCollection groundUnitCollection = (IGroundUnitCollection)linkedUnit;
            for (IGroundUnit groundUnit : groundUnitCollection.getGroundUnits())
            {
                if (!groundUnit.getCountry().isSameSide(flight.getFlightInformation().getCountry()))
                {
                    objective = getObjectiveFromEnemyUnit(groundUnit, flightInformation);
                    break;
                }
            }
        }
        
        return objective;
    }

    private static String getObjectiveFromEnemyUnit(IGroundUnit enemyGroundUnit, IFlightInformation flightInformation) throws PWCGException
    {
        String objectiveLocation =  MissionObjective.getMissionObjectiveLocation(
                flightInformation.getSquadron(), flightInformation.getCampaign().getDate(), enemyGroundUnit);
        
        String objective = "Attack the specified objective using all available means.";
        TargetType targetType = enemyGroundUnit.getTargetType();
        
        
        if (targetType == TargetType.TARGET_ASSAULT)
        {
            objective = "Attack assaulting enemy troops" + objectiveLocation; 
        }
        else if (targetType == TargetType.TARGET_DEFENSE)
        {
            objective = "Attack defending enemy troops" + objectiveLocation; 
        }
        else if (targetType == TargetType.TARGET_INFANTRY)
        {
            objective = "Attack enemy troops" + objectiveLocation; 
        }
        else if (targetType == TargetType.TARGET_AIRFIELD)
        {
            AirfieldTargetGroup target = (AirfieldTargetGroup)enemyGroundUnit;
            objective = "Attack the airfield at " + target.getAirfield().getName();
        }
        else if (targetType == TargetType.TARGET_TRAIN)
        {
            objective = "Attack the trains and rail facilities" + objectiveLocation;
        }
        else if (targetType == TargetType.TARGET_ARTILLERY)
        {
            objective = "Attack the artillery battery" + objectiveLocation; 
        }
        else if (targetType == TargetType.TARGET_TRANSPORT)
        {
            objective = "Attack the transport and road facilities" + objectiveLocation; 
        }
        else if (targetType == TargetType.TARGET_SHIPPING)
        {
            objective = "Attack the shipping" + objectiveLocation; 
        }
        else if (targetType == TargetType.TARGET_BALLOON)
        {
            objective = "Attack the balloons" + objectiveLocation; 
        }
        else if (targetType == TargetType.TARGET_DRIFTER)
        {
            objective = "Attack the light shipping" + objectiveLocation; 
        }
        else if (targetType == TargetType.TARGET_AAA)
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
