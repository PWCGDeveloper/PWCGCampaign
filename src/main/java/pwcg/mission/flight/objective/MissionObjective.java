package pwcg.mission.flight.objective;

import java.util.Date;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.unittypes.staticunits.AirfieldTargetGroup;

public class MissionObjective
{
    static String getMissionObjectiveLocation(Squadron squadron, Date date, IGroundUnit enemyGroundUnit) throws PWCGException 
    {
        String objectiveLocation = "";
        if (enemyGroundUnit.getCountry().isEnemy(squadron.determineSquadronCountry(date)))
        {
            if (enemyGroundUnit instanceof AirfieldTargetGroup)
            {
                AirfieldTargetGroup target = (AirfieldTargetGroup)enemyGroundUnit;
                if (target != null)
                {
                    
                    String airfieldName = target.getAirfield().getName();
                    if (!airfieldName.contains("Group"))
                    {
                        objectiveLocation = " at " + airfieldName;
                    }
                    else
                    {
                        objectiveLocation = getObjectiveName((IGroundUnit)enemyGroundUnit);
                    }
                }
            }
            else if (enemyGroundUnit instanceof IGroundUnit)
            {
                objectiveLocation = getObjectiveName((IGroundUnit)enemyGroundUnit);
            }
        }
        
        return objectiveLocation;
    }

    private static String getObjectiveName(IGroundUnit groundUnit) throws PWCGException
    {
        String targetName =  PWCGContext.getInstance().getCurrentMap().getGroupManager().getTownFinder().findClosestTown(groundUnit.getPosition().copy()).getName();
        return " near " + targetName;
    }

    
    static String formMissionObjectiveLocation(Coordinate targetLocation) throws PWCGException 
    {
        String missionObjectiveLocation = "";
        String targetName =  PWCGContext.getInstance().getCurrentMap().getGroupManager().getTownFinder().findClosestTown(targetLocation).getName();
        if (!targetName.isEmpty())
        {
            missionObjectiveLocation =   " near " + targetName;
        }
        
        return missionObjectiveLocation;
    }

}
