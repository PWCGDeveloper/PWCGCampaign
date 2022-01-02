package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class SquadronRange
{
    public static boolean positionIsInRange(Campaign campaign, Company squadron, Coordinate requestedTargetPosition) throws PWCGException
    {
        double distanceToTarget = MathUtils.calcDist(requestedTargetPosition, squadron.determineCurrentPosition(campaign.getDate()));
        if (distanceToTarget < squadron.determineSquadronRange(campaign.getDate()))
        {
            return true;
        }

        return false;
    }

}
