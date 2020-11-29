package pwcg.mission.options;

import java.util.Date;

import pwcg.campaign.context.PWCGContext;
import pwcg.product.bos.map.IMapClimate;

public class MissionWeatherTemperatature
{
    public static int calculateTemperature (Date date, int hour)
    {
        IMapClimate climate = PWCGContext.getInstance().getCurrentMap().getMapClimate();
        int meanTemperature = climate.getTemperature(date);
        int temperatureOffsetforTime = determineTemperatureOffsetForTimeOFDay(hour);
        return (meanTemperature + temperatureOffsetforTime);
    }
    
    private static int determineTemperatureOffsetForTimeOFDay (int hour)
    {
        if (hour == 0 || hour == 24)
        {
            return -7;
        }
        else if (hour == 1)
        {
            return -8;
        }
        else if (hour == 2)
        {
            return -9;
        }
        else if (hour == 3)
        {
            return -10;
        }
        else if (hour == 4)
        {
            return -9;
        }
        else if (hour == 5)
        {
            return -8;
        }
        else if (hour == 6)
        {
            return -7;
        }
        else if (hour == 7)
        {
            return -5;
        }
        else if (hour == 8)
        {
            return -3;
        }
        else if (hour == 9)
        {
            return -1;
        }
        else if (hour == 10)
        {
            return 0;
        }
        else if (hour == 11)
        {
            return 1;
        }
        else if (hour == 12)
        {
            return 2;
        }
        else if (hour == 13)
        {
            return 4;
        }
        else if (hour == 14)
        {
            return 5;
        }
        else if (hour == 15)
        {
            return 6;
        }
        else if (hour == 16)
        {
            return 4;
        }
        else if (hour == 17)
        {
            return 3;
        }
        else if (hour == 18)
        {
            return 2;
        }
        else if (hour == 19)
        {
            return 0;
        }
        else if (hour == 20)
        {
            return -2;
        }
        else if (hour == 21)
        {
            return -4;
        }
        else if (hour == 22)
        {
            return -5;
        }
        else if (hour == 23)
        {
            return -6;
        }

        
        return 0;
    }
}
