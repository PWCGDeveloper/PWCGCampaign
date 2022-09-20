package pwcg.product.bos.map.normandy;

import java.util.Calendar;
import java.util.Date;

import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.options.MapSeasonalParameters.Season;
import pwcg.product.bos.map.IMapClimate;


public class NormandyMapClimate implements IMapClimate
{
	public NormandyMapClimate()
	{
	    super();
	}	
	
    @Override
    public Season getSeason(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;

        Season season = Season.SUMMER;
        if (month == 12 || month == 1 || month == 2)
        {
            season = Season.WINTER;
        }
        else if (month == 3 || month == 4 | month == 5)
        {
            season = Season.SPRING;
        }
        else if (month == 6 || month == 7 || month == 8)
        {
            season = Season.SUMMER;
        }
        else if (month == 9 || month == 10 || month == 11)
        {
            season = Season.AUTUMN;
        }

        return season;
    }
    
    
    @Override
    public int getTemperature(Date date)
    {
        int temperature = 25;
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;

        if (month == 1)
        {
            temperature = -3 +  RandomNumberGenerator.getRandom(10);
        }
        else if (month == 2)
        {
            temperature = -1 +  RandomNumberGenerator.getRandom(10);
        }
        else if (month == 3)
        {
            temperature = 2 +  RandomNumberGenerator.getRandom(10);
        }
        else if (month == 4)
        {
            temperature = 6 +  RandomNumberGenerator.getRandom(10);
        }
        else if (month == 5)
        {
            temperature = 12 +  RandomNumberGenerator.getRandom(10);
        }
        else if (month == 6)
        {
            temperature = 20 +  RandomNumberGenerator.getRandom(10);
        }
        else if (month == 7)
        {
            temperature = 24 +  RandomNumberGenerator.getRandom(10);
        }
        else if (month == 8)
        {
            temperature = 20 +  RandomNumberGenerator.getRandom(15);
        }
        else if (month == 9)
        {
            temperature = 14 +  RandomNumberGenerator.getRandom(10);
        }
        else if (month == 10)
        {
            temperature = 8 +  RandomNumberGenerator.getRandom(10);
        }       
        else if (month == 11)
        {
            temperature = 2 +  RandomNumberGenerator.getRandom(10);
        }       
        else if (month == 12)
        {
            temperature = -2 +  RandomNumberGenerator.getRandom(10);
        }
        return temperature;
    }
}
