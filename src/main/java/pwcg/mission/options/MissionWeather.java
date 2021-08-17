package pwcg.mission.options;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.options.MapSeasonalParameters.Season;

public class MissionWeather
{
    private String weatherDescription = "";
    private String cloudConfig = "Oops";
    private Campaign campaign;
    private int timeHours;

    private int cloudLevel = 2100;
    private int cloudDensity = 600;
    private int precLevel = 0;
    private PrecipitationType precType = PrecipitationType.CLEAR;
    private double turbulence = 1.0;
    private int tempPressLevel = 0;
    private int temperature = 15;
    private int pressure = 760;
    private int windDirection = 0;
    private double haze = 0.0;
    private int seaState = 0;

    private List<WindLayer> windLayers = new ArrayList<WindLayer>();

    public MissionWeather(Campaign campaign, int timeHours)
    {
        this.campaign = campaign;
        this.timeHours = timeHours;
    }

    public void createMissionWeather() throws PWCGException
    {
        createCloud();
        createWindDirection();
        createWind();
        createHaze();
        createTemperature();
    }

    public boolean isWeatherFlightTypeImpactful(FlightTypes flightType)
    {
        if (cloudConfig.toLowerCase().contains("heavy") || cloudConfig.toLowerCase().contains("overcast"))
        {
            if (flightType == FlightTypes.DIVE_BOMB)
            {
                if (cloudLevel < 3500)
                {
                    return true;
                }
            }
            else
            {
                if (cloudLevel < 2000)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public int recalculateAltitudeForCloudBase(int altitude)
    {
        if (cloudConfig.toLowerCase().contains("heavy") || cloudConfig.toLowerCase().contains("overcast"))
        {
            if (altitude > cloudLevel)
            {
                altitude = cloudLevel - RandomNumberGenerator.getRandom(500);
                if (altitude < 600)
                {
                    altitude = 600;
                }
            }
        }
        return altitude;
    }

    private void createWindDirection()
    {
        int eastOrWest = RandomNumberGenerator.getRandom(100);
        if (eastOrWest > 80)
        {
            windDirection = 180 + RandomNumberGenerator.getRandom(180);
        }
        else
        {
            windDirection = RandomNumberGenerator.getRandom(180);
        }
    }

    private void createCloud() throws PWCGException
    {
        if (campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.UseRealisticWeatherKey) == 1)
        {
            createRealWeather();
        }
        else
        {
            createClearWeather();
        }

        setTurbulenceForMaximum();
        weatherDescription += "\n    Cloud layer is " + cloudLevel + " meters.";
    }

    private void createClearWeather() throws PWCGException
    {
        int weatherAverageClouds = 30;
        int weatherLightsClouds = weatherAverageClouds + 30;
        int weatherClearClouds = weatherLightsClouds + 30;

        int sky = RandomNumberGenerator.getRandom(weatherClearClouds);
        if (sky < weatherAverageClouds)
        {
            averageWeather();
        }
        else if (sky < weatherLightsClouds)
        {
            lightWeather();
        }
        else
        {
            clearWeather();
        }
    }

    private void createRealWeather() throws PWCGException
    {
        int chanceOfRain = PWCGContext.getInstance().getCurrentMap().getRainChances();
        int overcastRoll = RandomNumberGenerator.getRandom(100);
        if (overcastRoll < chanceOfRain)
        {
            overcastWeather();
        }
        else
        {
            int weatherHeavyClouds = 15;
            int weatherAverageClouds = weatherHeavyClouds + 30;
            int weatherLightsClouds = weatherAverageClouds + 30;
            int weatherClearClouds = weatherLightsClouds + 30;

            int sky = RandomNumberGenerator.getRandom(weatherClearClouds);
            if (sky < weatherHeavyClouds)
            {
                heavyWeather();
            }
            else if (sky < weatherAverageClouds)
            {
                averageWeather();
            }
            else if (sky < weatherLightsClouds)
            {
                lightWeather();
            }
            else
            {
                clearWeather();
            }
        }
    }

    private void clearWeather() throws PWCGException
    {
        cloudConfig = SeasonStringBuilder.getClearSkys(campaign.getDate());
        cloudLevel = 2500 + RandomNumberGenerator.getRandom(5000);
        cloudDensity = 400 + RandomNumberGenerator.getRandom(300);
        precLevel = 0;
        precType = PrecipitationType.CLEAR;
        turbulence = 1;

        if (cloudLevel < 1500)
        {
            weatherDescription = "Mostly clear, some low altitude clouds.";
        }
        else if (cloudLevel < 4000)
        {
            weatherDescription = "Mostly clear, some medium altitude clouds.";
        }
        else
        {
            weatherDescription = "Mostly clear, some high altitude clouds.";
        }
    }

    private void lightWeather() throws PWCGException
    {
        cloudConfig = SeasonStringBuilder.getLightSkys(campaign.getDate());
        cloudLevel = 2000 + RandomNumberGenerator.getRandom(4000);
        cloudDensity = 300 + RandomNumberGenerator.getRandom(300);
        precType = PrecipitationType.CLEAR;
        precLevel = 0;
        turbulence = 1 + RandomNumberGenerator.getRandom(2);

        if (cloudLevel < 1500)
        {
            weatherDescription = "Light cloud cover, low altitude.";
        }
        else if (cloudLevel < 4000)
        {
            weatherDescription = "Light cloud cover, medium altitude.";
        }
        else
        {
            weatherDescription = "Light cloud cover, high altitude.";
        }
    }

    private void averageWeather() throws PWCGException
    {
        cloudConfig = SeasonStringBuilder.getAverageSkys(campaign.getDate());
        cloudLevel = 1500 + RandomNumberGenerator.getRandom(4000);
        cloudDensity = 500 + RandomNumberGenerator.getRandom(400);
        precType = PrecipitationType.CLEAR;
        precLevel = 0;
        turbulence = 1 + RandomNumberGenerator.getRandom(2);

        if (cloudLevel < 1500)
        {
            weatherDescription = "Medium cloud cover, low altitude.";
        }
        else if (cloudLevel < 4000)
        {
            weatherDescription = "Medium cloud cover, medium altitude.";
        }
        else
        {
            weatherDescription = "Medium cloud cover, high altitude.";
        }
    }

    private void heavyWeather() throws PWCGException
    {
        cloudConfig = SeasonStringBuilder.getHeavySkys(campaign.getDate());
        cloudLevel = 1000 + RandomNumberGenerator.getRandom(3000);
        cloudDensity = 600 + RandomNumberGenerator.getRandom(600);
        precType = PrecipitationType.CLEAR;
        precLevel = 0;
        turbulence = 1 + RandomNumberGenerator.getRandom(3);

        if (cloudLevel < 1500)
        {
            weatherDescription = "Heavy cloud cover, low altitude.";
        }
        else if (cloudLevel < 3000)
        {
            weatherDescription = "Heavy cloud cover, medium altitude.";
        }
        else
        {
            weatherDescription = "Heavy cloud cover, high altitude.";
        }
    }

    private void overcastWeather() throws PWCGException
    {
        cloudConfig = SeasonStringBuilder.getOvercastSkys(campaign.getDate());
        cloudLevel = 1000 + RandomNumberGenerator.getRandom(1000);
        cloudDensity = 600 + RandomNumberGenerator.getRandom(1000);
        precLevel = 1 + RandomNumberGenerator.getRandom(8);
        turbulence = 1 + RandomNumberGenerator.getRandom(3);

        String precipitationType = "rain";
        if (PWCGContext.getInstance().getCurrentMap().getMapClimate().getSeason(campaign.getDate()) == Season.WINTER)
        {
            precType = PrecipitationType.SNOW;
            precipitationType = "snow";
        }
        else
        {
            precType = PrecipitationType.RAIN;
            precipitationType = "rain";
        }

        if (cloudLevel < 1500)
        {
            weatherDescription = "Overcast at low altitude with " + precipitationType + ".  Ground covering fog possible";
        }
        else if (cloudLevel < 3000)
        {
            weatherDescription = "Overcast at medium altitude with " + precipitationType + ".  Ground covering fog possible";
        }
        else
        {
            weatherDescription = "Overcast at high altitude with " + precipitationType + ".  Ground covering fog possible";
        }
    }

    private void createWind() throws PWCGException
    {
        windLayers = MissionWeatherWind.createWind(campaign, this);
    }

    private void createHaze() throws PWCGException
    {
        haze = MissionWeatherHaze.createHaze(campaign, this);
    }

    private void setTurbulenceForMaximum() throws PWCGException
    {
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        double maxTurbulence = configManager.getDoubleConfigParam(ConfigItemKeys.MaxTurbulenceKey);
        if (turbulence > maxTurbulence)
        {
            turbulence = maxTurbulence;
        }
    }

    private void createTemperature()
    {
        temperature = MissionWeatherTemperatature.calculateTemperature(campaign.getDate(), timeHours);
    }

    public int getCloudLevel()
    {
        return cloudLevel;
    }

    public int getCloudHeight()
    {
        return cloudDensity;
    }

    public int getPrecLevel()
    {
        return precLevel;
    }

    public int getPrecType()
    {
        return precType.getPrecipitationValue();
    }

    public String getCloudConfig()
    {
        return cloudConfig;
    }

    public double getTurbulence()
    {
        return turbulence;
    }

    public String getWeatherDescription()
    {
        return weatherDescription;
    }

    public List<WindLayer> getWindLayers()
    {
        return windLayers;
    }

    public int getTempPressLevel()
    {
        return tempPressLevel;
    }

    public int getTemperature()
    {
        return temperature;
    }

    public void setTemperature(int temperature)
    {
        this.temperature = temperature;
    }

    public int getPressure()
    {
        return pressure;
    }

    public int getWindDirection()
    {
        return windDirection;
    }

    public void setWindDirection(int windDirection)
    {
        this.windDirection = windDirection;
    }

    public double getHaze()
    {
        return haze;
    }

    public int getSeaState()
    {
        return seaState;
    }
}
