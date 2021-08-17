package pwcg.mission.options;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class MissionWeatherWind
{

    public static List<WindLayer> createWind(Campaign campaign, MissionWeather missionWeather) throws PWCGException
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

        Double turbulenceFactor = missionWeather.getTurbulence();

        WindLayer windLayer0 = new WindLayer(campaign, 0, missionWeather.getWindDirection(), RandomNumberGenerator.getRandom(3) + windSpeedModifier);

        WindLayer windLayer500 = new WindLayer(campaign, 500, missionWeather.getWindDirection(), 1 + turbulenceFactor.intValue() + RandomNumberGenerator.getRandom(3) + windSpeedModifier);

        WindLayer windLayer1000 = new WindLayer(campaign, 1000, missionWeather.getWindDirection(), 2 + turbulenceFactor.intValue() + RandomNumberGenerator.getRandom(4) + windSpeedModifier);

        WindLayer windLayer3000 = new WindLayer(campaign, 3000, missionWeather.getWindDirection(), 3 + turbulenceFactor.intValue() + RandomNumberGenerator.getRandom(5) + windSpeedModifier);

        WindLayer windLayer5000 = new WindLayer(campaign, 5000, missionWeather.getWindDirection(), 4 + turbulenceFactor.intValue() + RandomNumberGenerator.getRandom(5) + windSpeedModifier);

        List<WindLayer> windLayers = new ArrayList<WindLayer>();
        windLayers.add(windLayer0);
        windLayers.add(windLayer500);
        windLayers.add(windLayer1000);
        windLayers.add(windLayer3000);
        windLayers.add(windLayer5000);
        
        return windLayers;
    }
}
