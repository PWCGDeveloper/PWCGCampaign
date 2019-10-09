package pwcg.mission.ambient;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;

public abstract class AmbientUnitBuilder
{
    protected Mission mission = null;
    protected Campaign campaign = null;

    public AmbientUnitBuilder (Campaign campaign, Mission mission)
    {
        this.mission = mission;
        this.campaign = campaign;
    }
 
    protected boolean isBehindFrontLines(Coordinate position, Side targetSide) throws PWCGException
    {
        Coordinate closestFrontCoordinate = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate()).findClosestFrontCoordinateForSide(position, targetSide);            
        if (MathUtils.calcDist(closestFrontCoordinate, position) > 5000.0)
        {
            return true;
        }
        
        return false;
    }

}
