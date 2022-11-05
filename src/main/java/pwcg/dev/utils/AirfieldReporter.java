package pwcg.dev.utils;

import java.util.Date;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class AirfieldReporter
{
    public static void main(String[] args)
    {
        try
        {
        	Date date = DateUtils.getDateYYYYMMDD("19420101");
            AirfieldManager manager = new AirfieldManager();
            
            PWCGContext.getInstance();
            
            manager.configure(FrontMapIdentifier.STALINGRAD_MAP);
            
            PWCGLogger.log(LogLevel.DEBUG, "\n\n\n\n\nAllied");
            for (Airfield field: manager.getAirFieldsForSide(FrontMapIdentifier.NORMANDY_MAP, date, Side.ALLIED))
            {
                int distanceToFront = getDistanceToFront(field, Side.ALLIED, date);
                PWCGLogger.log(LogLevel.DEBUG, field.getName() + "   Km to front: " + distanceToFront);
            }
            
            PWCGLogger.log(LogLevel.DEBUG, "\n\n\n\n\nAxis");
            for (Airfield field: manager.getAirFieldsForSide(FrontMapIdentifier.NORMANDY_MAP, date, Side.AXIS))
            {
                int distanceToFront = getDistanceToFront(field, Side.AXIS, date);
                PWCGLogger.log(LogLevel.DEBUG, field.getName() + "   Km to front: " + distanceToFront);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static int getDistanceToFront(Airfield field, Side side, Date date) throws PWCGException
    {
        Double distanceToFront = 0.0;
        
        FrontLinesForMap frontLines =  PWCGContext.getInstance().getMap(FrontMapIdentifier.NORMANDY_MAP).getFrontLinesForMap(date);
        FrontLinePoint closestFront = frontLines.findClosestFrontPositionForSide(field.getPosition(), side);
        
        distanceToFront = MathUtils.calcDist(field.getPosition(), closestFront.getPosition());
        
        return distanceToFront.intValue() / 1000;
    }

    
}
