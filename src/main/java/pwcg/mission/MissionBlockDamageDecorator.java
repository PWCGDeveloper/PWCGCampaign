package pwcg.mission;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.NoMansLand;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.ScriptedFixedPosition;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class MissionBlockDamageDecorator
{
    private static final int DISTANCE_TO_FRONT_LINE_FOR_DAMAGE = 15000;
    private static final int SAFE_DISTANCE_TO_AIRFIELD = 3000;

    private Campaign campaign;

    MissionBlockDamageDecorator (Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public List<ScriptedFixedPosition> setDamageToFixedPositions(List<ScriptedFixedPosition> fixedPositions, Date date) throws PWCGException
    {
        List<ScriptedFixedPosition> fixedPositionCloseToFront = new ArrayList<>();
        for (ScriptedFixedPosition fixedPosition : fixedPositions)
        {
            double distanceToFront = isCloseToFront(fixedPosition, date);
            if (distanceToFront < DISTANCE_TO_FRONT_LINE_FOR_DAMAGE)
            {
                if (!isCloseToAirfield(fixedPosition))
                {
                    if (!(fixedPosition instanceof Bridge))
                    {
                        if (isInNoMansLand(fixedPosition))
                        {
                            damageFixedPositionsInNoMansLand(fixedPosition);                            
                        }
                        else
                        {
                            damageFixedPositionsCloseToFront(fixedPosition, distanceToFront);
                        }
                        fixedPositionCloseToFront.add(fixedPosition);
                    }
                }
            }
        }
        
        return fixedPositionCloseToFront;
    }
    
    private boolean isInNoMansLand(ScriptedFixedPosition fixedPosition) throws PWCGException
    {
        return NoMansLand.inNoMansLand(campaign, fixedPosition.getPosition());
    }

    private double isCloseToFront(ScriptedFixedPosition fixedPosition,Date date) throws PWCGException
    {
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getFrontLinesForMap(date);
        
        Coordinate closestAllied = frontLinesForMap.findClosestFrontCoordinateForSide(fixedPosition.getPosition(), Side.ALLIED);
        double distanceAllied = MathUtils.calcDist(fixedPosition.getPosition(), closestAllied);
        if (distanceAllied < DISTANCE_TO_FRONT_LINE_FOR_DAMAGE)
        {
            return distanceAllied;
        }
        
        Coordinate closestAxis = frontLinesForMap.findClosestFrontCoordinateForSide(fixedPosition.getPosition(), Side.AXIS);
        double distanceAxis = MathUtils.calcDist(fixedPosition.getPosition(), closestAxis);
        if (distanceAxis < DISTANCE_TO_FRONT_LINE_FOR_DAMAGE)
        {
            return distanceAxis;
        }

        return DISTANCE_TO_FRONT_LINE_FOR_DAMAGE + 1000000.0;
    }
    
    private boolean isCloseToAirfield(ScriptedFixedPosition fixedPosition) throws PWCGException
    {
        AirfieldManager airfieldManager = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getAirfieldManager();
        Airfield field = airfieldManager.getAirfieldFinder().findClosestAirfield(fixedPosition.getPosition());
        double distanceFromField = MathUtils.calcDist(fixedPosition.getPosition(), field.getPosition());
        if (distanceFromField < SAFE_DISTANCE_TO_AIRFIELD)
        {
            return true;
        }

        return false;
    }

    private void damageFixedPositionsCloseToFront(ScriptedFixedPosition fixedPosition, double distanceToFront) throws PWCGException
    {
        if(distanceToFront > DISTANCE_TO_FRONT_LINE_FOR_DAMAGE)
        {
            distanceToFront = DISTANCE_TO_FRONT_LINE_FOR_DAMAGE;
        }
        
        double distanceToFrontInverted = DISTANCE_TO_FRONT_LINE_FOR_DAMAGE - distanceToFront;
        
        double damageLevel = distanceToFrontInverted / DISTANCE_TO_FRONT_LINE_FOR_DAMAGE;
        damageLevel = Math.round(damageLevel * 10.0) / 10.0;
        if (damageLevel > 1.0)
        {
            damageLevel = 1.0;
        }
                
        Map<Integer, Double> damaged = new HashMap<>();
        damaged.put(-1, damageLevel);
        fixedPosition.setDamaged(damaged);
    }

    private void damageFixedPositionsInNoMansLand(ScriptedFixedPosition fixedPosition) throws PWCGException
    {
        Map<Integer, Double> damaged = new HashMap<>();
        damaged.put(-1, 1.0);
        fixedPosition.setDamaged(damaged);
    }    
}
