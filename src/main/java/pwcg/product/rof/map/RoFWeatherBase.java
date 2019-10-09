package pwcg.product.rof.map;

import java.util.Calendar;
import java.util.Date;

import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.options.MapSeasonalParameters.Season;
import pwcg.mission.options.MapWeather;

public abstract class RoFWeatherBase extends MapWeather
{
    public RoFWeatherBase()
    {
        super();
    }
    
    @Override
    protected String getClearSkys()
    {
        return "clear\\sky.ini";
    }

    @Override
    protected String getLightSkys()
    {
        return "clear\\sky.ini";
    }

    @Override
    protected String getAverageSkys()
    {
        return "average\\sky.ini";
    }

    @Override
    protected String getHeavySkys()
    {
        return "heavy\\sky.ini";
    }

    @Override
    protected String getOvercastSkys()
    {
        return "precipitation\\sky.ini";
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
            season = Season.SUMMER;
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

    protected void setTemperature(int month, FrontMapIdentifier frontMap)
    {
        temperature = 25;
        
        if (month == 1)
        {
            temperature = -10 +  RandomNumberGenerator.getRandom(15);
        }
        else if (month == 2)
        {
            temperature = -5 +  RandomNumberGenerator.getRandom(10);
        }
        else if (month == 3)
        {
            temperature = 5 +  RandomNumberGenerator.getRandom(15);
        }
        else if (month == 4)
        {
            temperature = 10 +  RandomNumberGenerator.getRandom(10);
        }
        else if (month == 5)
        {
            temperature = 15 +  RandomNumberGenerator.getRandom(10);
        }
        else if (month == 6 || month == 7)
        {
            temperature = 15 +  RandomNumberGenerator.getRandom(10);
        }
        else if (month == 8)
        {
            temperature = 20 +  RandomNumberGenerator.getRandom(10);
        }
        else if (month == 9)
        {
            temperature = 15 +  RandomNumberGenerator.getRandom(10);
        }
        else if (month == 10)
        {
            temperature = 10 +  RandomNumberGenerator.getRandom(10);
        }       
        else if (month == 11)
        {
            temperature = 5 +  RandomNumberGenerator.getRandom(10);
        }       
        else if (month == 12)
        {
            temperature = -5 +  RandomNumberGenerator.getRandom(15);
        }
    }

}
