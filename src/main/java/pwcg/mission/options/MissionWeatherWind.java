package pwcg.mission.options;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.MissionProfile;

public class MissionWeatherWind
{

    public static List<WindLayer> createWind(Campaign campaign, MissionProfile missionProfile, MissionWeather missionWeather) throws PWCGException
    {
        int windSpeedModifier = 1;
        if (missionWeather.getCloudConfig().toLowerCase().contains("clear"))
        {
            windSpeedModifier = 1;
        }
        if (missionWeather.getCloudConfig().toLowerCase().contains("light"))
        {
            windSpeedModifier = 2;
        }
        else if (missionWeather.getCloudConfig().toLowerCase().contains("medium"))
        {
            windSpeedModifier = 3;
        }
        else if (missionWeather.getCloudConfig().toLowerCase().contains("heavy"))
        {
            windSpeedModifier = 5;
        }
        else if (missionWeather.getCloudConfig().toLowerCase().contains("overcast"))
        {
            windSpeedModifier = 5;
        }

        int turbulenceFactor = 2 * missionWeather.getTurbulence();

        int weatherDivisor = 1;
        if (missionProfile.isNightMission())
        {
            weatherDivisor = 2;
        }

        WindLayer windLayer0 = new WindLayer(campaign, 0, missionWeather.getWindDirection(), RandomNumberGenerator.getRandom(3) / weatherDivisor);

        WindLayer windLayer500 = new WindLayer(campaign, 500, missionWeather.getWindDirection(), 1 + turbulenceFactor + RandomNumberGenerator.getRandom(3 + windSpeedModifier) / weatherDivisor);

        WindLayer windLayer1000 = new WindLayer(campaign, 1000, missionWeather.getWindDirection(), 1 + turbulenceFactor + RandomNumberGenerator.getRandom(4 + windSpeedModifier) / weatherDivisor);

        WindLayer windLayer3000 = new WindLayer(campaign, 3000, missionWeather.getWindDirection(), 1 + turbulenceFactor + RandomNumberGenerator.getRandom(5 + windSpeedModifier) / weatherDivisor);

        WindLayer windLayer5000 = new WindLayer(campaign, 5000, missionWeather.getWindDirection(), 1 + turbulenceFactor + RandomNumberGenerator.getRandom(5 + windSpeedModifier) / weatherDivisor);

        List<WindLayer> windLayers = new ArrayList<WindLayer>();
        windLayers.add(windLayer0);
        windLayers.add(windLayer500);
        windLayers.add(windLayer1000);
        windLayers.add(windLayer3000);
        windLayers.add(windLayer5000);
        
        return windLayers;
    }
}
