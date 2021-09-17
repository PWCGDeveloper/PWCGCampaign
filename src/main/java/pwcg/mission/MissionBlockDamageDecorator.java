package pwcg.mission;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.FixedPosition;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class MissionBlockDamageDecorator
{
    private static final int DISTANCE_TO_FRONT_LINE_FOR_DAMAGE = 20000;
    private static final int SAFE_DISTANCE_TO_AIRFIELD = 3000;

    public List<FixedPosition> setDamageToFixedPositions(List<FixedPosition> fixedPositions, Date date) throws PWCGException
    {
        List<FixedPosition> fixedPositionCloseToFront = new ArrayList<>();
        for (FixedPosition fixedPosition : fixedPositions)
        {
            if (isCloseToFront(fixedPosition, date))
            {
                if (!isCloseToAirfield(fixedPosition))
                {
                    if (!(fixedPosition instanceof Bridge))
                    {
                        damageFixedPositionsCloseToFront(fixedPosition);
                        fixedPositionCloseToFront.add(fixedPosition);
                    }
                }
            }
        }
        
        return fixedPositionCloseToFront;
    }
    
    private boolean isCloseToFront(FixedPosition fixedPosition,Date date) throws PWCGException
    {
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(date);
        
        Coordinate closestAllied = frontLinesForMap.findClosestFrontCoordinateForSide(fixedPosition.getPosition(), Side.ALLIED);
        if (MathUtils.calcDist(fixedPosition.getPosition(), closestAllied) < DISTANCE_TO_FRONT_LINE_FOR_DAMAGE)
        {
            return true;
        }
        
        Coordinate closestAxis = frontLinesForMap.findClosestFrontCoordinateForSide(fixedPosition.getPosition(), Side.AXIS);
        if (MathUtils.calcDist(fixedPosition.getPosition(), closestAxis) < DISTANCE_TO_FRONT_LINE_FOR_DAMAGE)
        {
            return true;
        }

        return false;
    }
    
    private boolean isCloseToAirfield(FixedPosition fixedPosition) throws PWCGException
    {
        AirfieldManager airfieldManager = PWCGContext.getInstance().getCurrentMap().getAirfieldManager();
        Airfield field = airfieldManager.getAirfieldFinder().findClosestAirfield(fixedPosition.getPosition());
        double distanceFromField = MathUtils.calcDist(fixedPosition.getPosition(), field.getPosition());
        if (distanceFromField < SAFE_DISTANCE_TO_AIRFIELD)
        {
            return true;
        }

        return false;
    }

    private void damageFixedPositionsCloseToFront(FixedPosition fixedPosition) throws PWCGException
    {
        Map<Integer, Double> damaged = new HashMap<>();;
        for (int i = 0; i < 10; ++i)
        {
            damaged.put(i, 0.6);
        }
        fixedPosition.setDamaged(damaged);
    }
}
