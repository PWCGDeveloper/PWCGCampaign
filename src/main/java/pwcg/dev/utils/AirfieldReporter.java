package pwcg.dev.utils;

import java.util.Date;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.group.AirfieldManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;

public class AirfieldReporter
{
    public static void main(String[] args)
    {
        try
        {
        	Date date = DateUtils.getDateYYYYMMDD("19420101");
            AirfieldManager manager = new AirfieldManager();
            
            PWCGContext.getInstance();
            
            manager.configure(PWCGMap.STALINGRAD_MAP_NAME);
            
            System.out.println("\n\n\n\n\nAllied");
            for (IAirfield field: manager.getAirFieldsForSide(date, Side.ALLIED))
            {
                int distanceToFront = getDistanceToFront(field, Side.ALLIED, date);
                System.out.println(field.getName() + "   Km to front: " + distanceToFront);
            }
            
            System.out.println("\n\n\n\n\nAxis");
            for (IAirfield field: manager.getAirFieldsForSide(date, Side.AXIS))
            {
                int distanceToFront = getDistanceToFront(field, Side.AXIS, date);
                System.out.println(field.getName() + "   Km to front: " + distanceToFront);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static int getDistanceToFront(IAirfield field, Side side, Date date) throws PWCGException
    {
        Double distanceToFront = 0.0;
        
        FrontLinesForMap frontLines =  PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(date);
        FrontLinePoint closestFront = frontLines.findClosestFrontPositionForSide(field.getPosition(), side);
        
        distanceToFront = MathUtils.calcDist(field.getPosition(), closestFront.getPosition());
        
        return distanceToFront.intValue() / 1000;
    }

    
}
