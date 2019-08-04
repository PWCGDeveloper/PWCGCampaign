package pwcg.campaign.context;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class FrontLinesDirectionFinder
{ 
    public enum FrontLinesDirection
    {
        DIRECTION_NS,
        DIRECTION_EW,
    }
    
    public static FrontLinesDirection findFrontLinesDirection(Campaign campaign, Coordinate referenceLocation) throws PWCGException
    {
        FrontLinesForMap frontLinesForMap = PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        FrontLinesDirection frontLinesDirection = FrontLinesDirection.DIRECTION_NS;
        
        List<FrontLinePoint> frontLinePoints = frontLinesForMap.findClosestFrontPositionsForSide(referenceLocation, 20000, Side.AXIS);
        if (frontLinePoints.size() > 1)
        {
            FrontLinePoint firstPosition = frontLinePoints.get(0);
            FrontLinePoint lastPosition = frontLinePoints.get(frontLinePoints.size()-1);
            frontLinesDirection = determineDirectionBetweenfrontLinePoints(firstPosition, lastPosition);
        }   
        
        return frontLinesDirection;
    }

    private static FrontLinesDirection determineDirectionBetweenfrontLinePoints(FrontLinePoint firstPosition, FrontLinePoint lastPosition) throws PWCGException
    {
        double angle = MathUtils.calcAngle(firstPosition.getPosition(), lastPosition.getPosition());
        if (angle > 230 && angle < 310)
        {
            return FrontLinesDirection.DIRECTION_EW;
        }
        if (angle > 50 && angle < 130)
        {
            return FrontLinesDirection.DIRECTION_EW;
        }
        return FrontLinesDirection.DIRECTION_NS;
    }
    
}
