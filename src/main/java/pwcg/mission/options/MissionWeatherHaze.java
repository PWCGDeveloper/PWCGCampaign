package pwcg.mission.options;

import pwcg.campaign.Campaign;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class MissionWeatherHaze
{

    public static double createHaze(Campaign campaign, MissionWeather missionWeather) throws PWCGException
    {
        int hazeValue = hazeValueByAtmosphericConditions(missionWeather);
        hazeValue = hazeLimits(campaign, hazeValue);
        double inMissionHazeValue = convertToInMissionValue(hazeValue);
        return inMissionHazeValue;
    }

    private static int hazeValueByAtmosphericConditions(MissionWeather missionWeather)
    {
        int minimumHaze = 0;
        int maximumHaze = 10;
        if (missionWeather.getCloudConfig().toLowerCase().contains("clear"))
        {
            minimumHaze = 0;
            maximumHaze = 4;
        }
        else if (missionWeather.getCloudConfig().toLowerCase().contains("light"))
        {
            minimumHaze = 0;
            maximumHaze = 6;
        }
        else if (missionWeather.getCloudConfig().toLowerCase().contains("medium"))
        {
            minimumHaze = 3;
            maximumHaze = 8;
        }
        else if (missionWeather.getCloudConfig().toLowerCase().contains("heavy"))
        {
            minimumHaze = 5;
            maximumHaze = 10;
        }
        else if (missionWeather.getCloudConfig().toLowerCase().contains("overcast"))
        {
            minimumHaze = 6;
            maximumHaze = 10;
        }
        
        int difference = maximumHaze - minimumHaze + 1;
        int hazeValue = minimumHaze + RandomNumberGenerator.getRandom(difference);
        return hazeValue;
    }

    private static int hazeLimits(Campaign campaign, int hazeValue) throws PWCGException
    {
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        int minimumConfiguredHaze = configManager.getIntConfigParam(ConfigItemKeys.MinHazeKey);
        int maximumConfiguredHaze = configManager.getIntConfigParam(ConfigItemKeys.MaxHazeKey);

        if (hazeValue < minimumConfiguredHaze)
        {
            hazeValue = minimumConfiguredHaze;
        }

        if (hazeValue > maximumConfiguredHaze)
        {
            hazeValue = maximumConfiguredHaze;
        }

        if (hazeValue < 0)
        {
            hazeValue = 0;
        }

        if (hazeValue > 10)
        {
            hazeValue = 10;
        }
        
        return hazeValue;
    }

    private static double convertToInMissionValue(int hazeValue)
    {
        double inMissionHazeValue =Integer.valueOf(hazeValue).doubleValue();
        inMissionHazeValue = inMissionHazeValue / 10;
        return inMissionHazeValue;
    }
}
