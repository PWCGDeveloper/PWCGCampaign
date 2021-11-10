package pwcg.aar.inmission.phase2.logeval.victory;

import java.util.List;

import pwcg.core.location.Coordinate;
import pwcg.core.logfiles.event.AType3;
import pwcg.core.logfiles.event.IAType17;
import pwcg.core.logfiles.event.IAType3;
import pwcg.core.logfiles.event.IATypeBase;
import pwcg.core.utils.MathUtils;

public class AARRandomAssignmentCalculator
{
    private AARAreaOfCombat areaOfCombat;

    public AARRandomAssignmentCalculator (AARAreaOfCombat areaOfCombat)
    {
        this.areaOfCombat = areaOfCombat;
    }
    
    public boolean shouldBeMarkedForRandomAssignment(List<IATypeBase> chronologicalAType, String victimId)
    {
        IAType17 closestWP = null;
        for (IATypeBase atype : chronologicalAType)
        {
            if (atype instanceof IAType3)
            {
                boolean crashWasClose = didTheCrashHappenCloseEnoughToPlayer(victimId, closestWP, atype);
                if (crashWasClose)
                {
                    return true;
                }
            }
            else if (atype instanceof IAType17)
            {
                closestWP = setClosestWaypoint(atype);
            }
        }
        
        return false;
    }

    private IAType17 setClosestWaypoint(IATypeBase atype)
    {
        IAType17 closestWP;
        closestWP = (IAType17)atype;
        return closestWP;
    }

    private boolean didTheCrashHappenCloseEnoughToPlayer(String victimId, IAType17 closestWP, IATypeBase atype)
    {
        AType3 thisVictory = (AType3)atype;
        if (victimId.equals(thisVictory.getVictim()))
        {
            if (determineIfCloseEnoughToWaypoint(closestWP, thisVictory))
            {
                return true;
            }

            if (determineIfInAreaOfCombat(thisVictory))
            {
                return true;
            }
        }
        
        return false;
    }

    private boolean determineIfCloseEnoughToWaypoint(IAType17 closestWP, IAType3 thisVictory)
    {
        if (closestWP != null)
        {
            Coordinate closestWpLocation = closestWP.getLocation();
            Coordinate crashLocation = thisVictory.getLocation();
            double distance = MathUtils.calcDist(closestWpLocation, crashLocation);
            if (distance < 5000.0)
            {
                return true;
            }
        }
        
        return false;
    }
    

    private boolean determineIfInAreaOfCombat(IAType3 thisVictory)
    {
        Coordinate crashLocation = thisVictory.getLocation();
        if (areaOfCombat.isNearAreaOfCombat(crashLocation))
        {
            return true;
        }
        
        return false;
    }
}
