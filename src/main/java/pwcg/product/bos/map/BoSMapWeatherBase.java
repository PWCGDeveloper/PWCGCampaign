package pwcg.product.bos.map;

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


public abstract class BoSMapWeatherBase extends MapWeather
{
    abstract public Season getSeason(Date date);
;
    
	public BoSMapWeatherBase()
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

    /**
     * @param month
     * @param frontMap
     * @return
     */
    protected void setTemperature(Date date, FrontMapIdentifier frontMap)
    {
        temperature = 25;
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;

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
