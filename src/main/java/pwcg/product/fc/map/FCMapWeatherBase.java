package pwcg.product.fc.map;

import java.util.Calendar;
import java.util.Date;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.options.MapSeasonalParameters;
import pwcg.mission.options.MapSeasonalParameters.Season;
import pwcg.mission.options.MapWeather;
import pwcg.mission.options.MissionOptions;


public class FCMapWeatherBase extends MapWeather
{
	public FCMapWeatherBase()
	{
	    super();
	}
	
    @Override
    protected String getClearSkys() throws PWCGException
    {
        String skys = addCloudPattern("00_clear_");
        return skys;
    }

    @Override
    protected String getLightSkys() throws PWCGException
    {
        String skys = addCloudPattern("01_Light_");
        return skys;
    }

    @Override
    protected String getAverageSkys() throws PWCGException
    {
        String skys = addCloudPattern("03_Heavy_");
        return skys;
    }

    @Override
    protected String getHeavySkys() throws PWCGException
    {
        String skys = addCloudPattern("03_Heavy_");
        return skys;
    }

    @Override
    protected String getOvercastSkys() throws PWCGException
    {
        String skys = addCloudPattern("04_Overcast_");
        return skys;
    }

    protected String addCloudPattern(String skys) throws PWCGException
    {
        MissionOptions missionOptions = PWCGContext.getInstance().getCurrentMap().getMissionOptions();
        MapSeasonalParameters mapSeasonalParameters = missionOptions.getSeasonBasedParameters(determineMapDate());
        String seasonString = mapSeasonalParameters.getSeason();
        String weather = seasonString + "\\" + skys;
        
        int cloudPattern = determineCloudPattern();
        
        weather += "0" + cloudPattern + "\\sky.ini";
        return weather;
    }

    @Override
    public Season getSeason(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;

        Season season = Season.SUMMER;
        if (month == 11 || month == 12 || month == 1 || month == 2 || month == 3)
        {
            season = Season.WINTER;
        }
        else if (month == 4 | month == 5)
        {
            season = Season.SPRING;
        }
        else if (month == 6 || month == 7 || month == 8)
        {
            season = Season.SUMMER;
        }
        else if (month == 9 || month == 10)
        {
            season = Season.AUTUMN;
        }

        return season;
    }

    /**
     * @param month
     * @param frontMap
     * @return
     */
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
            temperature = 20 +  RandomNumberGenerator.getRandom(10);
        }
        else if (month == 8)
        {
            temperature = 20 +  RandomNumberGenerator.getRandom(15);
        }
        else if (month == 9)
        {
            temperature = 20 +  RandomNumberGenerator.getRandom(10);
        }
        else if (month == 10)
        {
            temperature = 10 +  RandomNumberGenerator.getRandom(10);
        }       
        else if (month == 11)
        {
            temperature = 5 +  RandomNumberGenerator.getRandom(15);
        }       
        else if (month == 12)
        {
            temperature = 5 +  RandomNumberGenerator.getRandom(10);
        }
        
        if (frontMap == FrontMapIdentifier.MOSCOW_MAP)
        {
            if (temperature > 28)
            {
                temperature = 28;
            }
        }
    }
}
